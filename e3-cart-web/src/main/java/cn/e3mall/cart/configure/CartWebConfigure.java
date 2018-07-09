package cn.e3mall.cart.configure;

import cn.e3mall.cart.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
//需要继承WebMvcConfigurerAdapter，否则此类的WebMvcConfigurerAdapter会代替默认的WebMvcConfigurerAdapter
public class CartWebConfigure extends WebMvcConfigurerAdapter{

    //注入拦截器
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){

        WebMvcConfigurerAdapter webMvcConfigurerAdapter = new WebMvcConfigurerAdapter() {

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
//                super.addInterceptors(registry);
                registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
            }
        };
        return webMvcConfigurerAdapter;
    }
}
