package cn.e3mall.manager.service.impl;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.manager.ItemService;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service	//对bubbo声明为service
@Component
public class ItemServiceImpl implements ItemService {

	@Autowired    //商品Mapper
	private TbItemMapper itemMapper;

	@Autowired    //商品描述Mapper
	private TbItemDescMapper tbItemDescMapper;

/*	@Autowired
	private JmsTemplate jmsTemplate;

	@Resource
	private Destination topicDestination;

	//注入reids客户端
	@Autowired
	private JedisClient jedisClient;*/

	//注入StringRedisTemplate
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	//注入Rabbittemplate操作rabbit
	@Autowired
	private RabbitTemplate rabbitTemplate;


	//商品缓存key前缀
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;

	//商品过期时间
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
	public TbItem getItemById(long itemId) {
		//从缓存中获取数据
		try {
//			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");		原版
			String json=stringRedisTemplate.opsForValue().get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
			if(StringUtils.isNotBlank(json)){
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			try {
				//向缓存中添加数据
				//jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));Jedis版
				stringRedisTemplate.opsForValue().set(REDIS_ITEM_PRE+":"+itemId+":BASE",JsonUtils.objectToJson(list.get(0)));
				//设置过期时间
//				jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",ITEM_CACHE_EXPIRE); Jedis版
				//1.设置超时的key。2.超时的时间。3.时间的单位，枚举类型
				stringRedisTemplate.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",ITEM_CACHE_EXPIRE, TimeUnit.SECONDS);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page,rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> tbItems = itemMapper.selectByExample(example);
		//设置结果
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(tbItems);
		//获取分页信息
		PageInfo<TbItem> info = new PageInfo<>(tbItems);
		result.setTotal(info.getTotal());

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public E3Result addItem(TbItem tbItem, String desc) {
		//创建id
		final long itemId = IDUtils.genItemId();
		//完善tbItem对象
		tbItem.setId(itemId);
		//设置tbItem状态	商品状态，1-正常，2-下架，3-删除
		tbItem.setStatus((byte) 1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		//保存tbItem对象
		itemMapper.insert(tbItem);
		//创建商品描述对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		tbItemDesc.setItemDesc(desc);
		//保存商品描述对象
		tbItemDescMapper.insert(tbItemDesc);
		//发送消息
/*		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});*/
		//发送消息
		//参数：1. 交换机	2.路由键	3。消息
		rabbitTemplate.convertAndSend("exchange.e3","itemId",itemId);

		//创建一个不带数据的返回结果
		return E3Result.ok();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
	public TbItemDesc getItemDescById(long itemId) {
		//从缓存中获取数据
		try {
			String json = stringRedisTemplate.opsForValue().get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
			if(StringUtils.isNotBlank(json)){
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		//向缓存添加数据
		try {
			//向缓存中添加数据
			stringRedisTemplate.opsForValue().set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(tbItemDesc));
			//设置过期时间
			stringRedisTemplate.expire(REDIS_ITEM_PRE+":"+itemId+":DESC",ITEM_CACHE_EXPIRE,TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}
}
