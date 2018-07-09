package cn.e3mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@EnableRabbit   //开启Rabbit
@MapperScan("cn.e3mall.search.mapper")      //扫描mapper
@ComponentScan("cn.e3mall.search")
@SpringBootApplication
public class SearchServiceStart {

    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(SearchServiceStart.class)
                .web(false)
                .run(args);
        System.in.read();
    }

}
