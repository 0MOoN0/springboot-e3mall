package cn.e3mall;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@MapperScan(basePackages = "cn.e3mall.mapper")//扫描cn.e3mall.mapper下的所有mapper
@EnableTransactionManagement
@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
@EnableCaching
public class ManagerService {

    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(ManagerService.class)
                .web(false)//非web项目
                .run(args);
        System.in.read();
    }
}
