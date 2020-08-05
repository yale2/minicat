package com.yale.minicat;

import java.io.*;

/**
 * @author yale
 */
public class Response {

    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void outputHtml(String path) throws IOException {
        String absolutePath = Response.class.getResource("/").getPath();
        absolutePath = absolutePath.replaceAll("\\\\", "/") + path;
        File file = new File(absolutePath);
        if (file.exists() && file.isFile()) {
            outputHtml(new FileInputStream(file));
        }
    }

    private void outputHtml(FileInputStream fileInputStream) throws IOException {
        int readEachTime=1024;
        byte[] bytes = new byte[readEachTime];
        int count = 0;
        while (count == 0) {
            count = fileInputStream.available();
        }
        //输出http请求头
        outputStream.write(HttpProtocolUtil.reponse200(count).getBytes());
        long readLength = 0;
        while (readLength < count) {
            if(count - readLength < readEachTime){
                readEachTime=(int)(count-readLength);
                bytes=new byte[readEachTime];
            }
            fileInputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            readLength+=readEachTime;
        }
    }

    // 使用输出流输出指定字符串
    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }


}
