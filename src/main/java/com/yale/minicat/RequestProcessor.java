package com.yale.minicat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @author yale
 */
public class RequestProcessor extends Thread{

    private Map<String,Class<?>> urlPatterns;

    private Socket socket;

    public RequestProcessor(Map<String,Class<?>> urlPatterns,Socket socket){
        this.urlPatterns=urlPatterns;
        this.socket=socket;
    }

    @Override
    public void run(){
        try {
            InputStream inputStream = socket.getInputStream();
            Request request=new Request(inputStream);
            Response response=new Response(socket.getOutputStream());
            //如果没有对应的servlet处理该url,则处理静态资源
            Class<?> object = urlPatterns.get(request.getUrl());

            if(null == object){
                if("favicon.ico".equalsIgnoreCase(request.getUrl())){
                    return;
                }
                response.outputHtml(request.getUrl());
            }else{
                Class<?> superclass = object.getSuperclass();
                Method service = superclass.getDeclaredMethod("service", Request.class, Response.class);
                service.invoke(object.newInstance(),request,response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != socket){
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
