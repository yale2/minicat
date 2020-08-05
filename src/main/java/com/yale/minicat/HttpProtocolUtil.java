package com.yale.minicat;

/**
 * @author yale
 */
public class HttpProtocolUtil {

    public static String reponse200(long contentLength){
        return "HTTP/1.1 200 ok \n"+
                "Content-Type: text/html \n"+
                "Content-Length :"+ contentLength +"\n"+
                "\r\n";
    }

    public static String reponse404(){
        String str404="<h1>404 NOT FOUND</h1>";
        return "HTTP/1.1 404 NOT FOUND \n"+
                "Content-Type: text/html \n"+
                "Content-Length :"+str404.getBytes().length+"\n"+
                "\r\n" +str404;
    }
}
