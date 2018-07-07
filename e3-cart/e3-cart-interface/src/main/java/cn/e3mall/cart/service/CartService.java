package cn.e3mall.cart.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

import java.util.List;

public interface CartService {
    /**
     *  根据用户ID添加购物车列表
     * @param userId    用户ID
     * @param itemId    商品ID
     * @param num       商品数量
     * @return          添加结果
     */
    E3Result addCart(long userId, long itemId, int num);

    /**
     * 合并购物车
     * @param userId            合并购物车的用户ID
     * @param itemList          要合并的商品
     * @return                  合并的结果
     */
    E3Result mergeCart(long userId, List<TbItem> itemList);

    /**
     * 获取购物车列表
     * @param userId            购物车列表的用户ID
     * @return                  商品列表
     */
    List<TbItem> getCartList(long userId);

    /**
     * 更新购物车
     * @param userId        用户Id
     * @param itemId        商品ID
     * @param num           商品数量
     * @return              更新结果
     */
    E3Result updateCartNum(long userId, long itemId, int num);

    /**
     * 删除商品
     * @param userId        用户Id
     * @param itemId        商品ID
     * @return              删除的结果
     */
    E3Result deleteCartItem(long userId, long itemId);

    /**
     * 清空用户购物车
     * @param userId        用户ID
     * @return              返回清空的结果
     */
    E3Result clearCartItem(long userId);
}
