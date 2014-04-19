package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.Comment;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:58:52 PM
 * @describe
 */
public class TestCommentManageListener implements CommentManageListener {

    private Logger logger = LoggerFactory.getLogger(TestCommentManageListener.class);

    @Override
    public boolean beforeCommentManage(User oper, short action,
                    Comment comment, Object... args) {
        logger.debug("TEST BEFORE COMMENT MANAGE LISTENER {} {} {}",
                        oper.getName(), action,
                        comment != null ? comment.getContent() : "NULL");
        return true;
    }

    @Override
    public void afterCommentManage(User oper, short action, short result,
                    Comment comment, Object... args) {
        logger.debug("TEST AFTER COMMENT MANAGE LISTENER {} {} {}",
                        oper.getName(), action,
                        comment != null ? comment.getContent() : "NULL");
    }

}
