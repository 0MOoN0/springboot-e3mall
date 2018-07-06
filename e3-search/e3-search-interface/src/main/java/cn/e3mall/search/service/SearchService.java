package cn.e3mall.search.service;

import cn.e3mall.common.pojo.E3SearchResult;

public interface SearchService {

    E3SearchResult search(String keyWord, int page, int rows) throws Exception;
}
