package com.game.sdk.websocket;

import com.game.SysConfig;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Created by lucky on 2019/6/19.
 */
public class WSServer {
    private static Logger logger = Logger.getLogger(WSServer.class);

    public static void start() {
        WSExecutorManager.onStart();

        Server server = new Server(SysConfig.httpPort);
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory webSocketServletFactory) {
                webSocketServletFactory.register(WSMessageHandler.class);
            }
        };

        ContextHandler context = new ContextHandler();
        context.setContextPath("/server");
        context.setHandler(wsHandler);
        server.setHandler(wsHandler);

        try {
            server.start();
            logger.warn("ws server start on " + SysConfig.httpPort);
            server.join();
        } catch (Exception e) {
            logger.error("server 启动失败", e);
        }
    }
}
