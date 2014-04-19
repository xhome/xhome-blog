package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.Message;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:58:52 PM
 * @describe
 */
public class TestMessageManageListener implements MessageManageListener {

    private Logger logger = LoggerFactory.getLogger(TestMessageManageListener.class);

    @Override
    public boolean beforeMessageManage(User oper, short action,
                    Message message, Object... args) {
        logger.debug("TEST BEFORE MESSAGE MANAGE LISTENER {} {} {}",
                        oper.getName(), action,
                        message != null ? message.getContent() : "NULL");
        return true;
    }

    @Override
    public void afterMessageManage(User oper, short action, short result,
                    Message message, Object... args) {
        logger.debug("TEST AFTER MESSAGE MANAGE LISTENER {} {} {}",
                        oper.getName(), action,
                        message != null ? message.getContent() : "NULL");
    }

}
