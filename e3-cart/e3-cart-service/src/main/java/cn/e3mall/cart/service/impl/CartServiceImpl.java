package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

@com.alibaba.dubbo.config.annotation.Service
//@Service        //测试用
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;
    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public E3Result addCart(long userId, long itemId, int num) {
        //向redis中添加购物车。
        //数据类型是hash key：用户id field：商品id value：商品信息
        //判断商品是否存在
        Boolean hexists = stringRedisTemplate.opsForHash().hasKey(REDIS_CART_PRE + ":" + userId, itemId + "");
        //如果存在数量相加
        if (hexists) {
            //获取商品信息
            String itemJson = (String) stringRedisTemplate.opsForHash().get(REDIS_CART_PRE + ":" + userId, itemId + "");
            //json转成TbItem
            TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);
            //将当前商品数量和购物车商品数量相加
            tbItem.setNum(tbItem.getNum()+num);
            //存入Redis
            stringRedisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));

            return E3Result.ok();
        }
        //如果不存在，根据商品id取商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //设置购物车数据量
        item.setNum(num);
        //取一张图片
        String image = item.getImage();
        if (StringUtils.isNotBlank(image)) {
            item.setImage(image.split(",")[0]);
        }
        //添加到购物车列表
        stringRedisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    @Override
    public E3Result mergeCart(long userId, List<TbItem> itemList) {
        //遍历商品列表
        //把列表添加到购物车。
        //判断购物车中是否有此商品
        //如果有，数量相加
        //如果没有添加新的商品
        for (TbItem tbItem : itemList) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        //返回成功
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCartList(long userId) {
        //根据用户id查询购车列表
        List<Object> values = stringRedisTemplate.opsForHash().values(REDIS_CART_PRE + ":" + userId);
        List<TbItem> itemList = new ArrayList<>();
        for (Object json : values) {
            //创建一个TbItem对象
            TbItem item = JsonUtils.jsonToPojo((String) json, TbItem.class);
            //添加到列表
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public E3Result updateCartNum(long userId, long itemId, int num) {
        //从redis中取商品信息
        String json = (String) stringRedisTemplate.opsForHash().get(REDIS_CART_PRE + ":" + userId, itemId + "");
        //更新商品数量
        TbItem tbItem = JsonUtils.jsonToPojo( json, TbItem.class);
        tbItem.setNum(num);
        //写入redis
        stringRedisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCartItem(long userId, long itemId) {
        // 删除购物车商品
        stringRedisTemplate.opsForHash().delete(REDIS_CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }

    @Override
    public E3Result clearCartItem(long userId) {
        //删除购物车信息
        stringRedisTemplate.delete(REDIS_CART_PRE + ":" + userId);
        return E3Result.ok();
    }
}
