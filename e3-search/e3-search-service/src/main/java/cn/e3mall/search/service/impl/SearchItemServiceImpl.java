package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;
import com.alibaba.dubbo.config.annotation.Service;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
//@org.springframework.stereotype.Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private JestClient jestClient;

    /**
     * 添加商品到索引库
     * @return
     */
    public E3Result importAllItems() {
        try {
            //查询商品列表
            List<SearchItem> itemList = itemMapper.getItemList();
            //创建List<Index>
            List<Index> indexList=new ArrayList<Index>();
            //遍历商品列表
            for (SearchItem searchItem : itemList) {
                //创建索引
                Index index = new Index.Builder(searchItem).build();
                //将索引添加到List<Index>
                indexList.add(index);
            }
            //创建bulkBuilder
            Bulk build = new Bulk.Builder().defaultIndex("e3mall").defaultType("searchItem").addAction(indexList).build();
            //使用JestClient执行
            jestClient.execute(build);
            return E3Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500, "数据导入时发生异常");
        }
    }
}
