package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Reference
    private LoginService loginService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/page/login")
    public String showLogin(String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @RequestMapping(value="/user/login",method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password,
    HttpServletRequest request, HttpServletResponse response){
        E3Result e3Result = loginService.userLogin(username, password);
        //判断是否登陆成功
        if(e3Result.getStatus()==200){
            //将token存入Cookie
            CookieUtils.setCookie(request,response,TOKEN_KEY,e3Result.getData().toString());
        }
        return e3Result;
    }
}
