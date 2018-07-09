package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//@Service
@com.alibaba.dubbo.config.annotation.Service
public class TokenServiceImpl implements TokenService{

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public E3Result getUserByToken(String token) {
        //从缓存中获取对象
        String jsonUser = stringRedisTemplate.opsForValue().get("SESSION:"+token);
        //判断对象是否过期
        if(StringUtils.isBlank(jsonUser)){
            return E3Result.build(201,"登陆已过期");
        }
        //设置超时，单位秒
        stringRedisTemplate.expire("SESSION:"+token,SESSION_EXPIRE,TimeUnit.SECONDS);
        TbUser tbUser= JsonUtils.jsonToPojo(jsonUser, TbUser.class);
        return E3Result.ok(tbUser);
    }
}
