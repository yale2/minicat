package com.yale.minicat;

/**
 * @author yale
 */
public interface Servlet {

    void init();

    void destroy();

    void service(Request request,Response response);
}
