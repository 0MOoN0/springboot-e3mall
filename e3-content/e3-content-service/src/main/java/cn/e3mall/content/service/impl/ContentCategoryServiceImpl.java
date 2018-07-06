package cn.e3mall.content.service.impl;


import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    /**
     * 商城内容分类
     * @param parendId  列表的父id
     * @return          EasyUITreeNode对象的集合
     */
    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parendId) {
        //创建返回List
        List<EasyUITreeNode> resultList=new ArrayList<>();
        //创建example
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parendId);
        //执行查询，获取内容列表的集合
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        //遍历内容列表
        for (TbContentCategory tbContentCategory: tbContentCategories) {
            //创建EasyUITreeNode
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setText(tbContentCategory.getName());
            easyUITreeNode.setId(tbContentCategory.getId());
            //查询是否为父节点
            easyUITreeNode.setState(tbContentCategory.getIsParent()?"closed":"open");
            //添加到返回List
            resultList.add(easyUITreeNode);
        }
        return resultList;
    }

    /**
     * 添加商城内容分类节点
     * @param parentId  节点的父ID
     * @param name      节点的名字
     * @return          返回节点 ID
     */
    @Override
    public E3Result addContentCategory(long parentId, String name) {
        //创建商城内容节点
        TbContentCategory tbContentCategory = new TbContentCategory();
        //完善节点内容
        tbContentCategory.setName(name);
        tbContentCategory.setParentId(parentId);
        //新建节点是子节点，不可能是父节点
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setSortOrder(1);
        //设置节点状态1(正常),2(删除)
        tbContentCategory.setStatus(1);
        //插入节点
        tbContentCategoryMapper.insert(tbContentCategory);
        //查询插入节点的父节点
        TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        //设置父节点的IsParent属性
        if(!parentNode.getIsParent()){
            parentNode.setIsParent(true);
            //更新数据库
            tbContentCategoryMapper.updateByPrimaryKey(parentNode);
        }
        return E3Result.ok(tbContentCategory);
    }
}
