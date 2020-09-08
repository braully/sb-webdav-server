package com.github.braully.web;

import org.apache.jackrabbit.webdav.simple.SimpleWebdavServlet;
import javax.jcr.Repository;

public class WebdavServletWrapp extends SimpleWebdavServlet {

    public Repository getRepository() {
        return null;
    }
}
