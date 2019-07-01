package com.game.sdk.websocket;

import com.game.SysConfig;
import com.game.sdk.net.Executor;
import com.game.sdk.net.Result;
import com.game.sdk.utils.EncoderHandler;
import com.game.sdk.utils.ErrorCode;
import com.game.sdk.utils.ExecutorManager;
import com.game.util.CompressUtil;
import com.game.util.JsonUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lucky on 2019/6/19.
 */
public class WSExecutorManager {
    private static Logger logger = Logger.getLogger(WSExecutorManager.class);
    private static ExecutorService[] executors;
    private static BiMap<Session, String> onlineCache = Maps.synchronizedBiMap(HashBiMap.create());

    private static int size = 0;

    private static int roundToPowerOfTwo(final int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    public static void onStart() {
        int count = roundToPowerOfTwo(Runtime.getRuntime().availableProcessors()) * 2;
        executors = new ExecutorService[count];
        size = count;
        for (int i = 0; i < count; i++) {
            ExecutorService executor = new ThreadPoolExecutor(1, 4, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100000), new ThreadPoolExecutor.DiscardPolicy());
            executors[i] = executor;
        }
        logger.info("thread init finish ,count = " + count);
    }

    /**
     * 发送数据
     *
     * @param session
     * @param cmd
     * @param code
     * @return
     */
    public static boolean send(Session session, int cmd, String code) {
        return send(session, cmd, code, "");
    }

    /**
     * 发送数据
     *
     * @param session
     * @param cmd
     * @param code
     * @param message
     * @return
     */
    public static boolean send(Session session, int cmd, String code, String message) {
        Map<String, Object> resp = new HashMap<>(3);
        resp.put("cmd", cmd);
        resp.put("code", code);
        resp.put("data", message);

        return send(session, resp);
    }

    /**
     * 发送数据
     *
     * @param session
     * @param map
     * @return
     */
    public static boolean send(Session session, Map<String, Object> map) {
        try {
            String s = JsonUtils.map2String(map);
            byte[] bytes = s.getBytes(Charset.forName("utf-8"));
            bytes = CompressUtil.compressBytes(bytes);

            if (session != null && session.isOpen()) {
                session.getRemote().sendBytes(ByteBuffer.wrap(bytes));
                return true;
            }
            return false;
        } catch (Throwable e) {
            logger.error("send error", e);
        }
        return false;
    }

    public static void exec(final Session session, String proto) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("proto = " + proto);
            }
            Map<String, Object> map = JsonUtils.string2Map(proto, String.class, Object.class);
            int cmd = (int) map.get("cmd"); // 指令
            String sign = (String) map.get("s"); // 签名
            String openId = (String) map.get("openId"); // openID
            String dataReq = (String) map.get("data"); // 请求参数

            if (logger.isInfoEnabled()) {
                logger.info("receive req, [data]= " + dataReq + ",[s] = " + sign + ",[openId] = " + openId + ",[cmd] = " + cmd);
            }

            if (sign == null ||
                    cmd < 0 ||
                    dataReq == null) {
                send(session, 0, ErrorCode.PARAM_ERROR);
                return;
            }

            //data = URLDecoder.decode(data, "UTF-8");
            final String data = new String(Base64.getDecoder().decode(dataReq), Charset.forName("UTF-8"));

            if (logger.isInfoEnabled()) {
                logger.info("decoder, [data]= " + dataReq + ",[s] = " + sign + ",[openId] = " + openId + ",[cmd] = " + cmd);
            }
            String md5Str = SysConfig.oauthsecret + "&" + data;
            String mySign = EncoderHandler.md5(md5Str);
            if (!sign.equals(mySign)) {
                send(session, 0, ErrorCode.SIGN_ERROR);
                return;
            }

            Executor executor = ExecutorManager.getExector(cmd);
            if (executor == null) {
                send(session, 0, ErrorCode.EXEC_ERROR);
                return;
            }

            int hash = session.hashCode();
            ExecutorService es = executors[hash & size];

            es.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Result result;
                        //TODO 登录需要单独处理
                        if (executor.paramType == null) { //无请求参数的处理
                            result = (Result) executor.invoke(openId);
                        } else {
                            Object paramObject = JsonUtils.string2Object(data, executor.paramType);
                            result = (Result) executor.invoke(openId, paramObject);
                        }

                        if (logger.isInfoEnabled()) {
                            logger.info("send data ==> " + JsonUtils.object2String(result));
                        }

                        send(session, cmd, result.code, result.data);
                    } catch (Throwable e) {
                        send(session, 0, ErrorCode.SERVER_INTERNAL_ERROR);
                        logger.error(e);
                    }
                }
            });
        } catch (Throwable e) {
            send(session, 0, ErrorCode.SIGN_ERROR);
            logger.error(e);
        }
    }

    /**
     * 下线处理
     */
    public static void close(Session session) {
        try {
            Executor executor = ExecutorManager.getExector(9999);
            //String openId = online.get(session);
            String openId = onlineCache.remove(session);
            executor.invoke(openId);
        } catch (Throwable throwable) {
            logger.error(throwable);
        }
    }

    /**
     * 判断是否在线
     *
     * @param openid
     * @return
     */
    public static boolean isOnline(String openid) {
        return onlineCache.inverse().containsKey(openid);
    }

    /**
     * 登录加入
     *
     * @param session
     * @param openid
     */
    public static void online(Session session, String openid) {
        onlineCache.put(session, openid);
    }
}
