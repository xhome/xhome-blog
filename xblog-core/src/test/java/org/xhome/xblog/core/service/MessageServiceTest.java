package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Message;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestMessageManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class MessageServiceTest extends AbstractTest {

    private MessageService messageService;
    private long           id = 2L;

    public MessageServiceTest() {
        messageService = context.getBean(MessageServiceImpl.class);
        oper.setId(101L);

        ((MessageServiceImpl) messageService)
                        .registerMessageManageListener(new TestMessageManageListener());
    }

    @Test
    public void testAddMessage() {
        Message message = new Message();
        message.setOwner(1L);
        message.setModifier(1L);

        message.setUserName("Jhat");
        message.setUserEmail("cpf624@126.com");
        message.setUserWebsite("https://jhat.pw");
        message.setContent("MESSAGE2");

        messageService.addMessage(oper, message);
    }

    @Test
    public void testGetMessage() {
        Message message = messageService.getMessage(oper, id);
        printMessage(message);
    }

    @Test
    public void testCountMessages() {
        QueryBase query = new QueryBase();
        logger.info("{}", messageService.countMessages(oper, query));
    }

    @Test
    public void testGetMessages() {
        QueryBase query = new QueryBase();
        List<Message> messages = messageService.getMessages(oper, query);
        printMessage(messages);
    }

    @Test
    public void testIsMessageUpdateable() {
        Message message = messageService.getMessage(oper, id);
        logger.info("{}", messageService.isMessageUpdateable(oper, message));
    }

    @Test
    public void testIsMessageDeleteable() {
        Message message = messageService.getMessage(oper, id);
        logger.info("{}", messageService.isMessageDeleteable(oper, message));
    }

    @Test
    public void testIsMessageLocked() {
        Message message = messageService.getMessage(oper, id);
        logger.info("{}", messageService.isMessageLocked(oper, message));
    }

    @Test
    public void testLockMessage() {
        Message message = messageService.getMessage(oper, id);
        messageService.lockMessage(oper, message);
    }

    @Test
    public void testUnlockMessage() {
        Message message = messageService.getMessage(oper, id);
        messageService.unlockMessage(oper, message);
    }

    @Test
    public void testUpdateMessage() {
        Message message = messageService.getMessage(oper, id);
        message.setReply("REPLY2");
        int r = messageService.updateMessage(oper, message);
        logger.info("result:" + r);
    }

    @Test
    public void testDeleteMessage() {
        Message message = messageService.getMessage(oper, id);
        messageService.deleteMessage(oper, message);
    }

}
