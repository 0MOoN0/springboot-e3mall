package cn.e3mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

import java.io.IOException;

@EnableCaching
@SpringBootApplication
@MapperScan("cn.e3mall.mapper")
public class CartServiceStarter {

    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(CartServiceStarter.class)
                .web(false)
                .run(args);
        System.in.read();
    }

}
