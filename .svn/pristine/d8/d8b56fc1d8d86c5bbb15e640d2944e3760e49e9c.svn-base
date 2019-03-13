package com.game.sdk.web;

import com.game.sdk.annotation.WebHandler;
import com.game.service.PlayerService;
import com.game.util.BeanManager;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by lucky on 2019/1/4.
 */
@WebHandler(url = "/upload", description = "upload")
public class UploadServlet extends SdkServlet {
    private static Logger logger = Logger.getLogger(AdminHandlerServlet.class);

    @Override
    protected void doOptions(HttpServletRequest arg0, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");// 支持跨域请求
    }

    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement(".");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) {
            req.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);// 必须添加这一条才可以
        }
        String path = System.getProperty("user.dir") + File.separator + "icon" + File.separator;
        Part part = req.getPart("file");
        byte[] data = new byte[part.getInputStream().available()];
        req.getPart("file").getInputStream().read(data);
        Files.write(Paths.get(path + part.getSubmittedFileName()), data);


        Part openidPart = req.getPart("openid");
        byte[] openidByte = new byte[openidPart.getInputStream().available()];
        openidPart.getInputStream().read(openidByte);
        String openid = new String(openidByte);
        System.out.println(openid);

        BeanManager.getBean(PlayerService.class).updateGroupHeadicon(openid, part.getSubmittedFileName());

    }
}
