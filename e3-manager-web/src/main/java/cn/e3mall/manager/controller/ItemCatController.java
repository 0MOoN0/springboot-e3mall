package cn.e3mall.manager.controller;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.manager.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {


    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    //@RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(
            @RequestParam(name="id",defaultValue = "0") Long id
    ){
        List<EasyUITreeNode> catList = itemCatService.getCatList(id);
        return catList;
    }
}
