package cn.e3mall.order;

import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    OrderInfo orderInfo;

    @Test
    public void fun(){
        orderInfo = new OrderInfo();
        orderInfo.setUserId(123123l);
        orderService.createOrder(orderInfo);
    }

}
