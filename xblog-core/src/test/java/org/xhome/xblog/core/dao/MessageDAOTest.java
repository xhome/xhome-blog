package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Article;
import org.xhome.xblog.Message;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 2, 20139:54:39 PM
 * @describe
 */
public class MessageDAOTest extends AbstractTest {

    private MessageDAO messageDAO;
    private long       id = 1L;

    public MessageDAOTest() {
        messageDAO = context.getBean(MessageDAO.class);
    }

    @Test
    public void testAddMessage() {
        Article article = new Article();
        article.setId(1L);
        Message message = new Message();
        message.setOwner(1L);
        message.setModifier(1L);

        message.setUserName("Jhat");
        message.setUserEmail("cpf624@126.com");
        message.setUserWebsite("https://jhat.pw");
        message.setContent("MESSAGE");

        messageDAO.addMessage(message);
    }

    @Test
    public void testGetMessage() {
        Message message = messageDAO.queryMessage(id);
        printMessage(message);
    }

    @Test
    public void testQueryMessage() {
        QueryBase query = new QueryBase();

        List<Message> messages = messageDAO.queryMessages(query);
        printMessage(messages);
    }

    @Test
    public void testUpdateMessage() {

        Message message = messageDAO.queryMessage(id);
        printMessage(message);

        message.setReply("REPLY");
        messageDAO.updateMessage(message);

        message = messageDAO.queryMessage(id);
        printMessage(message);
    }

    @Test
    public void testDeleteMessage() {
        Message message = messageDAO.queryMessage(id);
        messageDAO.deleteMessage(message);
    }

}
