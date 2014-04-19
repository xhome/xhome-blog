package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Message;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:52:54 PM
 * @describe
 */
public class MessageManageAdapter implements MessageManageListener {

    public boolean beforeMessageManage(User oper, short action,
                    Message message, Object... args) {
        return true;
    }

    public void afterMessageManage(User oper, short action, short result,
                    Message message, Object... args) {}

}
