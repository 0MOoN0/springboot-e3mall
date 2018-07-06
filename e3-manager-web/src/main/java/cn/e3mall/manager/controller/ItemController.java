package cn.e3mall.manager.controller;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.manager.ItemService;
import cn.e3mall.pojo.TbItem;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class ItemController {

	@Reference
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}

	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows){
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}

	/**
	 *	保存商品及商品描述的Controller
	 * @param tbItem
	 * @param desc
	 * @return
	 */
	@RequestMapping(value = "/item/save",method = RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem tbItem, String desc){
		E3Result e3Result = itemService.addItem(tbItem, desc);
		return e3Result;
	}

}
