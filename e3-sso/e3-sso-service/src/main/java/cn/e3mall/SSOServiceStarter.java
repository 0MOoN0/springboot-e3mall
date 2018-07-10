package cn.e3mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootApplication
@EnableCaching
@MapperScan("cn.e3mall.mapper") //manager-dao
@EnableTransactionManagement    //开启事务
public class SSOServiceStarter {
    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(SSOServiceStarter.class)
                .web(false)
                .run(args);
        System.in.read();
    }
}
