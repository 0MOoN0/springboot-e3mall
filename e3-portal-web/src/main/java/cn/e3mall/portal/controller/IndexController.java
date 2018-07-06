package cn.e3mall.portal.controller;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Value("${CONTENT_LUNBO_ID}")
    private long CONTENT_LUNBO_ID;

    @Reference
    private ContentService contentService;

    /**
     * 首页展示
     * @return
     */
    @RequestMapping("/index")
    public String showIndex(Model model){
        List<TbContent> contentList = contentService.getContentListById(CONTENT_LUNBO_ID);
        model.addAttribute("ad1List",contentList);
        return "index";
    }
}
