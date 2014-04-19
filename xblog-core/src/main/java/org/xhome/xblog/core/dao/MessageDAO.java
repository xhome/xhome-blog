package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Message;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 2, 201310:24:24 PM
 * @describe
 */
public interface MessageDAO {

    public int addMessage(Message message);

    public int updateMessage(Message message);

    public int lockMessage(Message message);

    public int unlockMessage(Message message);

    public int deleteMessage(Message message);

    public boolean isMessageUpdateable(Message message);

    public boolean isMessageLocked(Message message);

    public boolean isMessageDeleteable(Message message);

    public Message queryMessage(Long id);

    public List<Message> queryMessages(QueryBase query);

    public long countMessages(QueryBase query);

}
