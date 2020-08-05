package com.yale.minicat;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class MyClassLoader extends ClassLoader {

    private String filePath;

    public MyClassLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    protected Class<?> findClass(String className) {
        File file = new File(filePath +"/"+ className.replaceAll("\\.", "/") + ".class");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel channel = fileInputStream.getChannel();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            WritableByteChannel writableByteChannel = Channels.newChannel(byteArrayOutputStream);
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            while (true) {
                int read = channel.read(allocate);
                if (read == 0 || read == -1) {
                    break;
                }
                allocate.flip();
                writableByteChannel.write(allocate);
                allocate.clear();
            }
            channel.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return this.defineClass(className, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

}
