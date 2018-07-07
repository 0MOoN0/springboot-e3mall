package cn.e3mall.manager.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {

    @Reference
    private SearchItemService searchItemService;

    @RequestMapping("index/item/import")
    @ResponseBody
    public E3Result importItemList(){
        E3Result e3Result = searchItemService.importAllItems();
        return e3Result;
    }

}
