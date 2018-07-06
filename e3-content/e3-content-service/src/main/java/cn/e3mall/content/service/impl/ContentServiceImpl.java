package cn.e3mall.content.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper tbContentMapper;

//    @Autowired
//    private JedisClient jedisClient;
    //注入redis操作类
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${CONTENT_LIST}")   //redis中的hash key
    private String CONTENT_LIST;

    /**
     * 添加内容
     * @param tbContent     要添加的内容
     * @return              返回添加的结果
     */
    @Override
    public E3Result addContent(TbContent tbContent) {
        //设置tbContent属性
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        tbContentMapper.insert(tbContent);
        //删除Content缓存
//        jedisClient.hdel(CONTENT_LIST,tbContent.getContent().toString()); //Jedis操作
        stringRedisTemplate.opsForHash().delete(CONTENT_LIST,tbContent.getContent().toString());
        return E3Result.ok();
    }

    @Override
    public List<TbContent> getContentListById(long cid) {
        //从redis中取出contentList
        try {
//            String list = jedisClient.hget(CONTENT_LIST, cid + "");
            Object obj = stringRedisTemplate.opsForHash().get(CONTENT_LIST, cid + "");
            String list = JsonUtils.objectToJson(obj);
            //判断list是否为空或长度为0或由空白符(whitespace)构成
            if(StringUtils.isNotBlank(list)){
                List<TbContent> tbContents = JsonUtils.jsonToList(list, TbContent.class);
                return tbContents;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //创建查询条件
        TbContentExample tbContentExample=new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        //通过分类ID（categoryId）查找对应的内容
        criteria.andCategoryIdEqualTo(cid);
        //使用WithBOLBs方法，使查找的结果含有Content
        List<TbContent> tbContentList = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);

        //将数据库中的contentList保存到redis中
        try {
//            jedisClient.hset(CONTENT_LIST,cid+"", JsonUtils.objectToJson(tbContentList));
            stringRedisTemplate.opsForHash().put(CONTENT_LIST,cid+"",JsonUtils.objectToJson(tbContentList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbContentList;
    }
}
