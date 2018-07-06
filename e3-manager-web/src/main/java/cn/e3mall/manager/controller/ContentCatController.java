package cn.e3mall.manager.controller;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 网页内容控制器
 */
@Controller
public class ContentCatController {
    @Reference
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(
        @RequestParam(name="id",defaultValue = "0")Long parentId
    ){
        //获取List
        List<EasyUITreeNode> resultList = contentCategoryService.getContentCategoryList(parentId);
        return resultList;
    }

    /**
     * 增加节点
     * @param parentId  父节点Id
     * @param name      新增节点的名字
     * @return          返回结果
     */
    @RequestMapping(value="/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public E3Result createContentCat(Long parentId, String name){
        E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
        return e3Result;
    }

}
