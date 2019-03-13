package com.game;

import com.game.sdk.http.HttpClient;
import com.game.util.JsonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class SysConfig {
    public static String host;// 服务器开服时间 yyyy-MM-dd HH:mm:ss
    public static String startUpDate;// 服务器开服时间 yyyy-MM-dd HH:mm:ss

    public static Date openDate;// 开服时间Date


    public static int httpPort;


    public static int httpsPort;
    public static int serverThread;
    public static int scheduledThread;
    public static int timerThread;
    public static String sdkServer;
    public static String oauthsecret;
    public static String dataPath;
    public static String httpsPwd;

    public static String wxAppid;
    public static String wxAppSecret;
    public static String wxAccessToken;

    public static void init() throws Exception {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(new File("config/sys.properties"))) {
            properties.load(fis);

            startUpDate = properties.getProperty("startUpDate");


            httpsPort = Integer.parseInt(properties.getProperty("httpsPort"));


            httpPort = Integer.parseInt(properties.getProperty("httpPort"));
            serverThread = Integer.parseInt(properties.getProperty("serverThread"));
            scheduledThread = Integer.parseInt(properties.getProperty("scheduledThread"));
            timerThread = Integer.parseInt(properties.getProperty("timerThread"));


            sdkServer = properties.getProperty("sdkServer");
            oauthsecret = properties.getProperty("oauthsecret");
            httpsPwd = properties.getProperty("httpsPwd");
            host = properties.getProperty("host");


            wxAppid = properties.getProperty("wxAppid");
            wxAppSecret = properties.getProperty("wxAppSecret");


            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            openDate = dataFormat.parse(startUpDate);

            dataPath = properties.getProperty("dataPath");
            System.setProperty("dataPath", dataPath);

            updateOpenDays();
//            getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateOpenDays() {
        Calendar open = Calendar.getInstance();
        open.setTime(openDate);
        open.set(Calendar.HOUR_OF_DAY, 0);
        open.set(Calendar.MINUTE, 0);
        open.set(Calendar.SECOND, 0);

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);

        long diff = (now.getTimeInMillis() - open.getTimeInMillis()) / 1000;

    }

    public static void getAccessToken() {
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxAppid + "&secret=" + wxAppSecret;
            String json = HttpClient.sendGetRequest(url);

            Map<String, Object> map = JsonUtils.string2Map(json);
            Integer errCode = (Integer) map.getOrDefault("errCode", 0);
            if (errCode != 0) {
                return;
            }

            wxAccessToken = (String) map.get("access_token");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
