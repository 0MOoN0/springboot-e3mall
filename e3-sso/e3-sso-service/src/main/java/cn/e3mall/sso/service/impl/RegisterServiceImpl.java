package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.RegisterService;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

//@Service
@com.alibaba.dubbo.config.annotation.Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public E3Result checkData(String param, int type) {
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria userExampleCriteria = tbUserExample.createCriteria();
        //判断检验类型
        if(type==1){
            userExampleCriteria.andUsernameEqualTo(param);
        }else if(type==2){
            userExampleCriteria.andPhoneEqualTo(param);
        }else if(type==3){
            userExampleCriteria.andEmailEqualTo(param);
        }else{
            return E3Result.build(400,"非法的参数");
        }
        //查询
        List<TbUser> tbUserList = tbUserMapper.selectByExample(tbUserExample);
        //判断查询的结果是否为空
        if(tbUserList!=null && tbUserList.size()>0){
            //数据已经存在
            return E3Result.ok(false);
        }
        //数据库中不存在该类型
        return E3Result.ok(true);
    }

    @Override
    public E3Result register(TbUser tbUser) {
        //再次校验数据
        if(StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPassword()) || StringUtils.isBlank(tbUser.getPhone())){
            return E3Result.build(400,"用户数据不完整，注册失败");
        }
        //再次校验用户名
        E3Result e3Result = checkData(tbUser.getUsername(), 1);
        if(!(boolean)e3Result.getData()){
            return E3Result.build(400,"用户名已存在");
        }
        e3Result = checkData(tbUser.getPhone(),2);
        if(!(boolean)e3Result.getData()){
            return E3Result.build(400,"此号码已被注册");
        }
        //完善tbUser
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //对密码进行md5加密
        String password = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(password);
        //更新数据库
        tbUserMapper.insert(tbUser);
        //返回成功
        return E3Result.ok();
    }
}
