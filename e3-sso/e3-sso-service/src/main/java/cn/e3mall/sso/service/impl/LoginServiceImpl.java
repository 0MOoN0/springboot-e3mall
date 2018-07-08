package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public E3Result userLogin(String username, String password) {
        //创建查询模板和查询条件
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        //进行查询
        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
        //判断用户是否存在
        if(tbUsers==null || tbUsers.size()==0){
            return E3Result.build(400,"用户名或密码错误");
        }
        //取出user
        TbUser tbUser = tbUsers.get(0);
        //判断用户密码是否正确，将password转为md5加密后进行比较
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())){
            return E3Result.build(400,"用户名或密码错误");
        }
        //用户名和密码正确，创建token
        String token = UUID.randomUUID().toString();
        //存入redis
        tbUser.setPassword(null);
        //将信息存入缓存
        stringRedisTemplate.opsForValue().set("SESSION:"+token, JsonUtils.objectToJson(tbUser));
        //设置过期时间，单位为秒
        stringRedisTemplate.expire("SESSION:"+token,SESSION_EXPIRE, TimeUnit.SECONDS);
        //把token返回
        return E3Result.ok(token);
    }
}
