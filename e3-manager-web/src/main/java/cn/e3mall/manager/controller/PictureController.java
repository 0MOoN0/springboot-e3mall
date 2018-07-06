package cn.e3mall.manager.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {

    @Value("${IMAGE_SERVER_URL}")     //从配置文件中注入图片服务器路径
    private String IMAGE_SERVER_URL;

    @RequestMapping(value="/pic/upload",produces = MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile){
        try {
            //创建fastDFS客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
            //获取文件拓展名
            String filename = uploadFile.getOriginalFilename();
            String exName = filename.substring(filename.indexOf(".") + 1);
            //上传文件
            String uri = fastDFSClient.uploadFile(uploadFile.getBytes(), exName);
            //拼接成完整URL
            String completeURL=IMAGE_SERVER_URL+uri;
            //生成Map
            Map result=new HashMap();
            result.put("error",0);
            result.put("url",completeURL);
            return JsonUtils.objectToJson(result);  //返回json格式的字符串，提高兼容性
        } catch (Exception e) {
            e.printStackTrace();
            //返回异常处理结果
            Map result=new HashMap();
            result.put("error",1);
            result.put("message","文件上传失败");
            return JsonUtils.objectToJson(result);
        }
    }
}
