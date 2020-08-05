package com.yale.minicat;

import java.io.IOException;

public class MyServlet extends HttpServlet {

    @Override
    public void doGet(Request request, Response response) {
        String content="MyServlet GET method";
        try {
            response.output(HttpProtocolUtil.reponse200(content.getBytes().length)+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content="MyServlet POST method";
        try {
            response.output(HttpProtocolUtil.reponse200(content.getBytes().length)+content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
