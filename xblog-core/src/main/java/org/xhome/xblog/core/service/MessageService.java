package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Message;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:20:06 PM
 * @describe
 */
@Service
public interface MessageService {

    public int addMessage(User oper, Message message);

    public int updateMessage(User oper, Message message);

    public int lockMessage(User oper, Message message);

    public int unlockMessage(User oper, Message message);

    public int deleteMessage(User oper, Message message);

    public int deleteMessages(User oper, List<Message> messages);

    public boolean isMessageUpdateable(User oper, Message message);

    public boolean isMessageLocked(User oper, Message message);

    public boolean isMessageDeleteable(User oper, Message message);

    public Message getMessage(User oper, long id);

    public List<Message> getMessages(User oper, QueryBase query);

    public long countMessages(User oper, QueryBase query);

}
