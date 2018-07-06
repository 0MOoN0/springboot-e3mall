package cn.e3mall.manager;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

	TbItem getItemById(long itemId);

	EasyUIDataGridResult getItemList(int page, int rows);

	/**
	 * 添加商品方法
	 * @param tbItem  商品对象
	 * @param desc	  商品描述
	 * @return       返回结果
	 */
	E3Result addItem(TbItem tbItem, String desc);

	/**
	 * 通过ItemId获取Item详情(ItemDesc)
	 * @param itemId	商品Id(ItemId)
	 * @return			商品详情(ItemDesc)
	 */
	TbItemDesc getItemDescById(long itemId);
}
