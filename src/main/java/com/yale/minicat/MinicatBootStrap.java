package com.yale.minicat;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author yale
 */
public class MinicatBootStrap {

    private int port=8080;

    private static final String SERVLET_PATH="D:/Users/webapps/";

    private Map<String,Class<?>> urlPatterns=new HashMap<>();

    public void start(){
        try {
            //先加载本地web.xml
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
            loadServlet(resourceAsStream,null);
            File file=new File(SERVLET_PATH);
            //加载D:/Users/webapps/webapps目录下的web.xml
            loadWebappsServlet(file);
            ServerSocket serverSocket=new ServerSocket(port);
            System.out.println("=====>>>Minicat start on port：" + port);

            ThreadPoolExecutor  threadPoolExecutor= new ThreadPoolExecutor
                    (10,100,60000,
                            TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(500));

            while(true){
                Socket socket = serverSocket.accept();
                RequestProcessor requestProcessor=new RequestProcessor(urlPatterns,socket);
                threadPoolExecutor.execute(requestProcessor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MinicatBootStrap minicatBootStrap=new MinicatBootStrap();
        minicatBootStrap.start();
    }

    /**
     * 加载web.xml中定义的servlet信息
     * @throws DocumentException
     */
    private void loadServlet(InputStream inputStream,String filePath) throws DocumentException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        SAXReader saxReader=new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//servlet");
        for (Element element : list) {
            Node nameNode = element.selectSingleNode("servlet-name");
            String servletName = nameNode.getText();
            Node classNode = element.selectSingleNode("servlet-class");
            String servletClass = classNode.getText();

            Node servletMappingNode = rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
            Node urlPatternNode = servletMappingNode.selectSingleNode("url-pattern");
            String urlPattern=urlPatternNode.getText();

            if(StringUtils.isEmpty(filePath)){
                urlPatterns.put(urlPattern,Class.forName(servletClass) );
            }else{
                MyClassLoader myClassLoader=new MyClassLoader(filePath);
                urlPatterns.put(urlPattern,myClassLoader.findClass( servletClass));
            }

        }
    }

    private void loadWebappsServlet(File file) throws FileNotFoundException, ClassNotFoundException, InstantiationException, DocumentException, IllegalAccessException {
        if(!file.exists()||file.isFile()){
            System.out.println("没有可部署项目");
            return;
        }
        File[] files = file.listFiles();
        for (File childFile : files) {
            if(childFile.isFile()){
                continue;
            }
            File webXmlFile=new File(childFile.getAbsolutePath()+"/web.xml");
            if(webXmlFile.exists()){
                loadServlet(new FileInputStream(webXmlFile),childFile.getAbsolutePath());
            }
        }
    }


}
