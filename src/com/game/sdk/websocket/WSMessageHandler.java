package com.game.sdk.websocket;


import com.game.util.CompressUtil;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.nio.charset.Charset;

/**
 * Created by ping on 2019/6/19.
 * <p>
 * WS 消息处理器
 */
public class WSMessageHandler implements WebSocketListener {
    private static Logger logger = Logger.getLogger(WSMessageHandler.class);
    private Session session;

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {
        String jsonStr = new String(CompressUtil.decompressBytes(bytes), Charset.forName("utf-8"));
        WSExecutorManager.exec(session, jsonStr);
    }

    @Override
    public void onWebSocketText(String s) {
        WSExecutorManager.exec(session, s);
    }

    @Override
    public void onWebSocketClose(int i, String s) {
        WSExecutorManager.close(session);
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
    }

    @Override
    public void onWebSocketError(Throwable throwable) {
        session.close();
        logger.error(throwable);
    }
}
