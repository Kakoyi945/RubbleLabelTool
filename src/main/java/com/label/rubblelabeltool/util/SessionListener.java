package com.label.rubblelabeltool.util;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener{
    /**
     * session被销毁时触发
     * 将session信息从imageSessionMap中剔除
     * @param se
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSessionListener.super.sessionDestroyed(se);
        HttpSession session = se.getSession();
        String sessionId = session.getId();
        System.out.println("用户"+ sessionId +"被删除");
        BMap<Integer, String> imageSessionMap = BMap.getImageSessionMap();
        imageSessionMap.removeByValue(sessionId);
    }

}
