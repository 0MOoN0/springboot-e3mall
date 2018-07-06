package cn.e3mall.content;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

import java.io.IOException;

@SpringBootApplication
@EnableCaching
@MapperScan("cn.e3mall.mapper")
public class ContentServiceStart {

    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(ContentServiceStart.class)
                .web(false)//非web应用
                .run(args);
        //方法阻塞
        System.in.read();
    }

}
