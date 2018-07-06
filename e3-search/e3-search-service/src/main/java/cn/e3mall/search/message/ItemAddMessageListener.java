package cn.e3mall.search.message;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 监听商品添加消息，接收消息后，将对应的商品信息同步到索引库
 */
@Component
public class ItemAddMessageListener{
	
	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private JestClient jestClient;

	@RabbitListener(queues = "itemId")
	public void onMessage(Message msg) {
		try {
			String str=new String(msg.getBody());
			long itemId=Long.parseLong(str);

			Thread.sleep(1000);
			//根据商品id查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemId);
			//创建索引
			Index build = new Index.Builder(searchItem).index("e3mall").type("searchItem").build();
			//添加索引
			jestClient.execute(build);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
