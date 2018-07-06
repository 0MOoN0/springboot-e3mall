package cn.e3mall.content.service;


import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

import java.util.List;

public interface ContentService {
    /**
     * 添加内容
     * @param tbContent     要添加的内容
     * @return              添加的结果
     */
    E3Result addContent(TbContent tbContent);

    /**
     * 根据分类ID查找此分类下的所有内容
     * @param cid           分类ID
     * @return              分类ID下的内容集合
     */
    List<TbContent> getContentListById(long cid);
}
