package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface LoginService {

    /**
     * 用户登陆
     * @param username      用户名
     * @param password      密码
     * @return              登陆的结果
     */
    E3Result userLogin(String username, String password);

}
