package com.game;

import com.fasterxml.jackson.core.JsonParser;
import com.game.sdk.http.HttpClient;
import com.game.util.JsonUtils;
import org.apache.log4j.Logger;
import sun.font.CompositeGlyphMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StartGenerateWXACode {

    public static void main(String[] args) {
        System.out.println("Start");
        try{
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxb00112803f6343cc&secret=cef5c8335e35c10e503528a850087377";
            String json = HttpClient.sendGetRequest(url);

            Map<String, Object> map = JsonUtils.string2Map(json);

            System.out.println(json);
            Integer errCode = (Integer)map.getOrDefault("errCode", 0);
            if(errCode != 0){
                System.out.println(errCode);
                return;
            }

            String accessToken = (String)map.get("access_token");

            url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + accessToken;
//            url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + accessToken;

//            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wxb00112803f6343cc&secret=cef5c8335e35c10e503528a850087377&grant_type=authorization_code&js_code=" + code;
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");

            Map<String, String> content = new HashMap<>();
            content.put("path", "a=abc");
            String contentJson = JsonUtils.map2String(content);

            String result = HttpClient.sendPostRequestWithHeader(url, contentJson, header);
            if(result.length() > 0){
                System.out.println(result);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
