package cn.e3mall.search.dao;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.E3SearchResult;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SearchDao {

    @Autowired
    private JestClient jestClient;

/*    public SearchResult search(SolrQuery solrQuery) throws Exception {
        SearchResult searchResult = new SearchResult();

        //执行查询
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //获取结果
        SolrDocumentList results = queryResponse.getResults();
        //在searchResult中设置查询结果总条数
        long numFound = results.getNumFound();
        searchResult.setRecordCount(numFound);
        //遍历结果
        List<SearchItem> searchItemList = new ArrayList<SearchItem>();
        //获取高亮结果
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        for (SolrDocument solrDocument : results) {
            //设置查找到的商品信息
            SearchItem searchItem = new SearchItem();
            searchItem.setId(solrDocument.get("id") + "");
            searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
            searchItem.setImage((String) solrDocument.get("item_image"));
            searchItem.setPrice((Long) solrDocument.get("item_price"));
            searchItem.setSell_point("item_sell_point");
            //取高亮显示
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            searchItem.setTitle(title);
            //将商品信息存储到list中
            searchItemList.add(searchItem);
        }
        //将list件加到SearchResult中
        searchResult.setItemList(searchItemList);
        return searchResult;
    }*/

    /**
     * 查询商品
     * @param builder   用于查询的builder
     * @return          封装好结果的E3SearchResult
     * @throws Exception    jestClient.execute(builder)
     */
    public E3SearchResult search(Search builder) throws Exception {
        E3SearchResult E3SearchResult = new E3SearchResult();
        long total;
        //SearchItem集合
        List<SearchItem> searchItemList=new ArrayList<SearchItem>();

        //执行查询获取结果
        SearchResult searchResult = jestClient.execute(builder);
        //获取结果总条数
        //拆箱前判断Integer是否为null
        if(null==searchResult.getTotal()){
            String str=searchResult.getErrorMessage();
            total=0;
            E3SearchResult.setRecordCount(total);
            E3SearchResult.setItemList(searchItemList);
            return  E3SearchResult;
        }else {
            total=searchResult.getTotal();
        }
        //设置E3SearchResult的总条数
        E3SearchResult.setRecordCount(total);
        //获取searchResult内容
        List<SearchResult.Hit<SearchItem, Void>> searchResultHits = searchResult.getHits(SearchItem.class);
        SearchItem searchItem=null;
        //遍历
        for (SearchResult.Hit<SearchItem, Void> hit: searchResultHits
             ) {
            //取出searchItem
            searchItem=hit.source;
            //获取高亮信息
            Map<String, List<String>> allHighlight = hit.highlight;
            //遍历高亮信息
            String title="";
            for (Map.Entry<String, List<String>> entry :allHighlight.entrySet()
                    ) {
                List<String> highlight = entry.getValue();
                if (highlight != null && highlight.size() > 0) {
                    //如果标题被高亮了，则更新标题
                    searchItem.setTitle(highlight.get(0));
                }
                searchItemList.add(searchItem);
                break;
            }
        }
        E3SearchResult.setItemList(searchItemList);
        return  E3SearchResult;
    }
}
