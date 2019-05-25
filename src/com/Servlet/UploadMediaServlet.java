package com.Servlet;



import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 上传素材servlet
 */
@WebServlet(name = "UploadMediaServlet")
public class UploadMediaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UploadMediaApiUtil uploadMediaApiUtil = new UploadMediaApiUtil();
        String appId = "wx0b2c639821e8354a";
        String appSecret = "3c2353e58048ba8fa92be9ea6e477506";
        String accessToken = uploadMediaApiUtil.getAccessToken(appId,appSecret);

        String filePath = "/Users/zxg/Downloads/timg.jpeg";
        File file = new File(filePath);
        String type = "IMAGE";
        JSONObject jsonObject = uploadMediaApiUtil.uploadMedia(file,accessToken,type);
        System.out.println(jsonObject.toString());
        
        
    }
    
    
}

