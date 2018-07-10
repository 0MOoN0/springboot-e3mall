package cn.e3mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("cn.e3mall.mapper")
@EnableCaching
@EnableTransactionManagement
@SpringBootApplication
public class OrderServiceStarter {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceStarter.class,args);
    }

}
