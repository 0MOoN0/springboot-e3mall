package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

public interface RegisterService {

    /**
     * 检查用户名或密码
     * @param param     待检验的参数
     * @param type      检验的类型
     * @return          检验的结果
     */
    E3Result checkData(String param, int type);

    /**
     * 注册用户
     * @param tbUser       注册的用户信息
     * @return             注册的结果
     */
    E3Result register(TbUser tbUser);
}
