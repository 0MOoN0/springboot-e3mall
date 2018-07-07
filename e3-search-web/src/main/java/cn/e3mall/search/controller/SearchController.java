package cn.e3mall.search.controller;


import cn.e3mall.common.pojo.E3SearchResult;
import cn.e3mall.search.service.SearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Reference
    private SearchService searchService;

    @Value("${SEARCH_RESULT_ROWS}")
    private int SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public String search(
            String keyword,@RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {

        //字符编码转换，将iso8859-1的字符转为utf-8的字符
//        keyword=new String(keyword.getBytes("iso-8859-1"),"utf-8");

        //使用searchService查询
        E3SearchResult e3SearchResult = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
        //设置model数据
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", e3SearchResult.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("recourdCount", e3SearchResult.getRecordCount());
        model.addAttribute("itemList", e3SearchResult.getItemList());

        return "search";
    }

}
