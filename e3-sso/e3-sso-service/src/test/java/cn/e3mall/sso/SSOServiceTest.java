package cn.e3mall.sso;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.LoginService;
import cn.e3mall.sso.service.RegisterService;
import cn.e3mall.sso.service.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SSOServiceTest {
/*
    @Autowired
    TokenService tokenService;

    @Autowired
    RegisterService registerService;

    @Autowired
    LoginService loginService;

    //测试参数
    String token;

    TbUser tbUser;


    //token查询
    @Test
    public void tokenServiceTest(){
        token="068b045a-ad19-4fc0-9db7-364499b2068c";
        E3Result userByToken = tokenService.getUserByToken(token);
        System.out.println(userByToken.getMsg());
    }

    //注册服务
    @Test
    public void registerServiceTest(){
        tbUser=new TbUser();
        tbUser.setPassword("123456");
        //service自动填充
//        tbUser.setCreated(new Date());
//        tbUser.setUpdated(new Date());
        tbUser.setEmail("814429032@qq.com");
//        tbUser.setId("12345");    id自增长
        tbUser.setPhone("123123123");
        tbUser.setUsername("zhaoliu");
        registerService.register(tbUser);
    }

    //登陆服务
    @Test
    public void LoginServiceTest(){
        loginService.userLogin("zhaoliu","123456");
    }*/

}
