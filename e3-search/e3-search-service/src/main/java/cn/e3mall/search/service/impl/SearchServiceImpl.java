package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.E3SearchResult;
import cn.e3mall.search.dao.SearchDao;
import com.alibaba.dubbo.config.annotation.Service;
import io.searchbox.core.Search;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.service.SearchService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
//@org.springframework.stereotype.Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    /**
     * 基于Elasticsearch和jest的查找服务
     * @param keyWord       要查找的关键字
     * @param page          开始查找的页面
     * @param rows          要查询的条数
     * @return              封装查询结果的E3SearchResult
     * @throws Exception       searchDao.search(build)
     */
    @Override
    public E3SearchResult search(String keyWord, int page, int rows) throws Exception {
        //计算page
        if (page<=0) page=1;

        //创建json查询表达式，查询title中的关键字
        String json="{\n" +
                "    \"query\" : {\n" +
                "        \"match_phrase\" : { \"title\" : \""+keyWord+"\" }\n" +
                "    },\n" +
                "    \"highlight\": {\n" +
                "\t    \"fields\" : {\n" +
                "\t        \"title\" : {}\n" +
                "\t    },\n" +
                "\t    \"pre_tags\": [\"<em style='color=red'>\"],\n" +
                "       \"post_tags\": [\"</em>\"]\n" +
                "    },\n" +
                "    \"from\":"+(page-1)*rows+",\n" +
                "    \"size\":"+rows+"\n" +
                "}";
        //创建builder
        Search build = new Search.Builder(json)
                .addIndex("e3mall")
                .addType("searchItem")
                .build();
        //使用searchDao进行查询
        E3SearchResult e3SearchResult = searchDao.search(build);
        //设置searchResult总页数
        long count = e3SearchResult.getRecordCount();
        long totalPages;
        if(count%rows==0){
            totalPages=count/rows;
        }else{
            totalPages=count/rows+1;
        }
        e3SearchResult.setTotalPages((int) totalPages);
        return e3SearchResult;
    }
}
