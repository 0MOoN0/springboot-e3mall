package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

public interface TokenService {
    /**
     * 根据token获取用户信息
     * @param token         用于获取对应用户的字符串后缀
     * @return              返回获取的结果，如果用户登陆没有过期，则返回用户信息
     */
    E3Result getUserByToken(String token);
}
