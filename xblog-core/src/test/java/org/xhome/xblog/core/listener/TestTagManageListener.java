package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.Tag;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:58:52 PM
 * @describe
 */
public class TestTagManageListener implements TagManageListener {

    private Logger logger = LoggerFactory.getLogger(TestTagManageListener.class);

    @Override
    public boolean beforeTagManage(User oper, short action, Tag tag,
                    Object... args) {
        logger.debug("TEST BEFORE TAG MANAGE LISTENER {} {} {}",
                        oper.getName(), action, tag != null ? tag.getName()
                                        : "NULL");
        return true;
    }

    @Override
    public void afterTagManage(User oper, short action, short result, Tag tag,
                    Object... args) {
        logger.debug("TEST AFTER TAG MANAGE LISTENER {} {} {}", oper.getName(),
                        action, tag != null ? tag.getName() : "NULL");
    }

}
