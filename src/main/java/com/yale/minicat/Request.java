package com.yale.minicat;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yale
 */
public class Request {

    private InputStream inputStream;

    private String method;

    private String url;

    public Request(InputStream inputStream) throws IOException {
        this.inputStream=inputStream;

        int count=0;
        while(count==0){
            count=inputStream.available();
        }
        byte[] bytes=new byte[count];
        inputStream.read(bytes);
        String request=new String(bytes);
        String requestFirstLine=request.split("\\n")[0];
        String[] requestContent = requestFirstLine.split(" ");
        method=requestContent[0];
        url=requestContent[1];
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
