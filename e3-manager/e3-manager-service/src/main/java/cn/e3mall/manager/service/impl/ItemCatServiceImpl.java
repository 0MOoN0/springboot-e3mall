package cn.e3mall.manager.service.impl;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.manager.ItemCatService;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<EasyUITreeNode> getCatList(long parentId) {
        //创建查询模板
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        //设置查询条件
        TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //获取查询结果
        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(tbItemCatExample);
        //新建返回结果
        List<EasyUITreeNode> result=new ArrayList<>();
        //遍历结果
        for(TbItemCat tbItemcat:tbItemCats){
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemcat.getId());
            node.setText(tbItemcat.getName());
            node.setState(tbItemcat.getIsParent()?"closed":"open");
            result.add(node);
        }
        return result;
    }
}
