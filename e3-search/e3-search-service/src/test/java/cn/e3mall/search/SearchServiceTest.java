package cn.e3mall.search;

import cn.e3mall.common.pojo.E3SearchResult;
import cn.e3mall.search.service.SearchService;
import cn.e3mall.search.service.impl.SearchItemServiceImpl;
import cn.e3mall.search.service.impl.SearchServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.e3mall.search.service.SearchItemService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceTest {

    @Autowired
    SearchItemServiceImpl searchItemService;

    @Autowired
    SearchServiceImpl searchService;

    @Test
    public void testSearchItemService(){
        searchItemService.importAllItems();
    }

    @Test
    public void testSearchService() throws Exception {
        E3SearchResult macbook = searchService.search("macbook", 0, 5);
        System.out.println(macbook.getItemList());
    }



}
