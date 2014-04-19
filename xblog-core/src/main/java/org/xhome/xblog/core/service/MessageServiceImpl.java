package org.xhome.xblog.core.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.ManageLogService;
import org.xhome.xblog.ManageLogType;
import org.xhome.xblog.Message;
import org.xhome.xblog.core.dao.MessageDAO;
import org.xhome.xblog.core.listener.MessageManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:33 PM
 * @describe
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDAO                  messageDAO;
    @Autowired
    private BlogConfigService           blogConfigService;
    @Autowired
    private ManageLogService            manageLogService;
    @Autowired(required = false)
    private List<MessageManageListener> messageManageListeners;

    private Logger                      logger;

    public MessageServiceImpl() {
        logger = LoggerFactory.getLogger(MessageService.class);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public int addMessage(User oper, Message message) {
        String content = message.getContent();

        if (!this.beforeMessageManage(oper, Action.ADD, message)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to add message {}, but it's blocked", content);
            }

            this.logManage(null, Action.ADD, null, Status.BLOCKED, oper);
            this.afterMessageManage(oper, Action.ADD, Status.BLOCKED, message);
            return Status.BLOCKED;
        }

        short r = Status.BLOCKED;
        if (blogConfigService.allowLeaveMessage()) {
            message.setStatus(Status.OK);
            message.setVersion((short) 0);
            Timestamp t = new Timestamp(System.currentTimeMillis());
            message.setCreated(t);
            message.setModified(t);

            r = messageDAO.addMessage(message) == 1 ? Status.SUCCESS
                            : Status.ERROR;
        }

        if (logger.isDebugEnabled()) {
            if (r == Status.SUCCESS) {
                logger.debug("success to add message {}", content);
            } else {
                logger.debug("fail to add message {}", content);
            }
        }

        this.logManage(null, Action.ADD, message.getId(), r, oper);
        this.afterMessageManage(oper, Action.ADD, r, message);
        return r;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public int updateMessage(User oper, Message message) {
        Long id = message.getId();
        String content = message.getContent();

        if (!this.beforeMessageManage(oper, Action.UPDATE, message)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to update message {}[{}], but it's blocked",
                                content, id);
            }

            this.logManage(null, Action.UPDATE, null, Status.BLOCKED, oper);
            this.afterMessageManage(oper, Action.UPDATE, Status.BLOCKED,
                            message);
            return Status.BLOCKED;
        }

        Message old = messageDAO.queryMessage(id);

        if (old == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to update message {}[{}], but it's not exists",
                                content, id);
            }

            this.logManage(null, Action.UPDATE, id, Status.NOT_EXISTS, oper);
            this.afterMessageManage(oper, Action.UPDATE, Status.NOT_EXISTS,
                            message);
            return Status.NOT_EXISTS;
        }
        message.setOwner(old.getOwner());
        message.setCreated(old.getCreated());

        String oldMessage = old.getContent();

        if (!old.getVersion().equals(message.getVersion())) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to update message {}[{}], but version not match",
                                oldMessage, id);
            }

            this.logManage(null, Action.UPDATE, id, Status.VERSION_NOT_MATCH,
                            oper);
            this.afterMessageManage(oper, Action.UPDATE,
                            Status.VERSION_NOT_MATCH, message);
            return Status.VERSION_NOT_MATCH;
        }

        short status = old.getStatus();
        if (status == Status.NO_UPDATE || status == Status.LOCK) {
            if (logger.isDebugEnabled()) {
                logger.debug("it's not allowed to update message {}[{}]",
                                oldMessage, id);
            }

            this.logManage(null, Action.UPDATE, id, status, oper);
            this.afterMessageManage(oper, Action.UPDATE, Status.EXISTS, message);
            return status;
        }

        Timestamp t = new Timestamp(System.currentTimeMillis());
        message.setModified(t);

        short r = messageDAO.updateMessage(message) == 1 ? Status.SUCCESS
                        : Status.ERROR;
        if (r == Status.SUCCESS) {
            message.incrementVersion();
        }

        if (logger.isDebugEnabled()) {
            if (r == Status.SUCCESS) {
                logger.debug("success to update message {}[{}]", oldMessage, id);
            } else {
                logger.debug("fail to update message {}[{}]", oldMessage, id);
            }
        }

        this.logManage(null, Action.UPDATE, id, r, oper);
        this.afterMessageManage(oper, Action.UPDATE, r, message);
        return r;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public int lockMessage(User oper, Message message) {
        Long id = message.getId();
        String content = message.getContent();

        if (!this.beforeMessageManage(oper, Action.LOCK, message)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to lock message {}[{}], but it's blocked",
                                content, id);
            }

            this.logManage(null, Action.LOCK, null, Status.BLOCKED, oper);
            this.afterMessageManage(oper, Action.LOCK, Status.BLOCKED, message);
            return Status.BLOCKED;
        }

        short r = messageDAO.lockMessage(message) == 1 ? Status.SUCCESS
                        : Status.ERROR;

        if (logger.isDebugEnabled()) {
            if (r == Status.SUCCESS) {
                logger.debug("success to lock message {}[{}]", content, id);
            } else {
                logger.debug("fail to lock message {}[{}]", content, id);
            }
        }

        this.logManage(null, Action.LOCK, id, r, oper);
        this.afterMessageManage(oper, Action.LOCK, r, message);
        return r;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public int unlockMessage(User oper, Message message) {
        Long id = message.getId();
        String content = message.getContent();

        if (!this.beforeMessageManage(oper, Action.UNLOCK, message)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to unlock message {}[{}], but it's blocked",
                                content, id);
            }

            this.logManage(null, Action.UNLOCK, null, Status.BLOCKED, oper);
            this.afterMessageManage(oper, Action.UNLOCK, Status.BLOCKED,
                            message);
            return Status.BLOCKED;
        }

        short r = messageDAO.unlockMessage(message) == 1 ? Status.SUCCESS
                        : Status.ERROR;

        if (logger.isDebugEnabled()) {
            if (r == Status.SUCCESS) {
                logger.debug("success to unlock message {}[{}]", content, id);
            } else {
                logger.debug("fail to unlock message {}[{}]", content, id);
            }
        }

        this.logManage(null, Action.UNLOCK, id, r, oper);
        this.afterMessageManage(oper, Action.UNLOCK, r, message);
        return r;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public int deleteMessage(User oper, Message message) {
        Long id = message.getId();
        String content = message.getContent();

        if (!this.beforeMessageManage(oper, Action.DELETE, message)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to delete message {}[{}], but it's blocked",
                                content, id);
            }

            this.logManage(null, Action.DELETE, null, Status.BLOCKED, oper);
            this.afterMessageManage(oper, Action.DELETE, Status.BLOCKED,
                            message);
            return Status.BLOCKED;
        }

        short r = Status.SUCCESS;
        if (messageDAO.isMessageDeleteable(message)) {
            if (logger.isDebugEnabled()) {
                logger.debug("delete message {}[{}]", content, id);
            }
            messageDAO.deleteMessage(message);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("message {}[{}] isn't deleteable", content, id);
            }
            r = Status.NO_DELETE;
        }

        this.logManage(null, Action.DELETE, id, r, oper);
        this.afterMessageManage(oper, Action.DELETE, r, message);
        return r;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public int deleteMessages(User oper, List<Message> messages) {
        int r = Status.SUCCESS;
        for (Message message : messages) {
            r = this.deleteMessage(oper, message);
            if (r != Status.SUCCESS) {
                throw new RuntimeException("fail to delete Message ["
                                + message.getId() + "]");
            }
        }
        return r;
    }

    @Override
    public boolean isMessageUpdateable(User oper, Message message) {
        Long id = message.getId();
        String content = message.getContent();

        if (!this.beforeMessageManage(oper, Action.IS_UPDATEABLE, message)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to juge updateable of message {}[{}], but it's blocked",
                                content, id);
            }

            this.logManage(null, Action.IS_UPDATEABLE, null, Status.BLOCKED,
                            oper);
            this.afterMessageManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED,
                            message);
            return false;
        }

        boolean e = messageDAO.isMessageUpdateable(message);

        if (logger.isDebugEnabled()) {
            if (e) {
                logger.debug("message {}[{}] is updateable", content, id);
            } else {
                logger.debug("message {}[{}] isn't updateable", content, id);
            }
        }

        this.logManage(null, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
        this.afterMessageManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS,
                        message);
        return e;
    }

    @Override
    public boolean isMessageLocked(User oper, Message message) {
        Long id = message.getId();
        String content = message.getContent();

        if (!this.beforeMessageManage(oper, Action.IS_LOCKED, message)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to juge locked of message {}[{}], but it's blocked",
                                content, id);
            }

            this.logManage(null, Action.IS_LOCKED, null, Status.BLOCKED, oper);
            this.afterMessageManage(oper, Action.IS_LOCKED, Status.BLOCKED,
                            message);
            return false;
        }

        boolean e = messageDAO.isMessageLocked(message);

        if (logger.isDebugEnabled()) {
            if (e) {
                logger.debug("message {}[{}] is locked", content, id);
            } else {
                logger.debug("message {}[{}] isn't locked", content, id);
            }
        }

        this.logManage(null, Action.IS_LOCKED, id, Status.SUCCESS, oper);
        this.afterMessageManage(oper, Action.IS_LOCKED, Status.SUCCESS, message);
        return e;
    }

    @Override
    public boolean isMessageDeleteable(User oper, Message message) {
        Long id = message.getId();
        String content = message.getContent();

        if (!this.beforeMessageManage(oper, Action.IS_DELETEABLE, message)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to juge deleteable of message {}[{}], but it's blocked",
                                content, id);
            }

            this.logManage(null, Action.IS_DELETEABLE, null, Status.BLOCKED,
                            oper);
            this.afterMessageManage(oper, Action.IS_DELETEABLE, Status.BLOCKED,
                            message);
            return false;
        }

        boolean e = messageDAO.isMessageDeleteable(message);

        if (logger.isDebugEnabled()) {
            if (e) {
                logger.debug("message {}[{}] is deleteable", content, id);
            } else {
                logger.debug("message {}[{}] isn't deleteable", content, id);
            }
        }

        this.logManage(null, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
        this.afterMessageManage(oper, Action.IS_DELETEABLE, Status.SUCCESS,
                        message);
        return e;
    }

    @Override
    public Message getMessage(User oper, long id) {
        if (!this.beforeMessageManage(oper, Action.GET, null, id)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to get message of id {}, but it's blocked",
                                id);
            }

            this.logManage("Message[" + id + "]", Action.GET, null,
                            Status.BLOCKED, oper);
            this.afterMessageManage(oper, Action.GET, Status.BLOCKED, null, id);
            return null;
        }

        Message message = messageDAO.queryMessage(id);

        String content = null;
        if (logger.isDebugEnabled()) {
            if (message != null) {
                content = message.getContent();
                logger.debug("get message {}[{}]", content, id);
            } else {
                logger.debug("message of id {} is not exists", id);
            }
        }

        this.logManage("Message[" + id + "]", Action.GET, id, Status.SUCCESS,
                        oper);
        this.afterMessageManage(oper, Action.GET, Status.SUCCESS, message, id);
        return message;
    }

    @Override
    public List<Message> getMessages(User oper, QueryBase query) {
        if (!this.beforeMessageManage(oper, Action.QUERY, null, query)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to query messages, but it's blocked");
            }

            this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
            this.afterMessageManage(oper, Action.QUERY, Status.BLOCKED, null,
                            query);
            return null;
        }

        List<Message> results = messageDAO.queryMessages(query);
        if (query != null) {
            query.setResults(results);
            long total = messageDAO.countMessages(query);
            query.setTotal(total);
        }

        if (logger.isDebugEnabled()) {
            if (query != null) {
                logger.debug("query messages with parameters {}",
                                query.getParameters());
            } else {
                logger.debug("query messages");
            }
        }

        this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
        this.afterMessageManage(oper, Action.QUERY, Status.SUCCESS, null, query);
        return results;
    }

    @Override
    public long countMessages(User oper, QueryBase query) {
        if (!this.beforeMessageManage(oper, Action.COUNT, null, query)) {
            if (logger.isDebugEnabled()) {
                logger.debug("try to count messages, but it's blocked");
            }

            this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
            this.afterMessageManage(oper, Action.COUNT, Status.BLOCKED, null,
                            query);
            return -1;
        }

        long c = messageDAO.countMessages(query);
        if (logger.isDebugEnabled()) {
            if (query != null) {
                logger.debug("count messages with parameters {} of {}",
                                query.getParameters(), c);
            } else {
                logger.debug("count messages of {}", c);
            }
        }

        this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
        this.afterMessageManage(oper, Action.COUNT, Status.SUCCESS, null, query);
        return c;
    }

    private void logManage(String content, Short action, Long obj,
                    Short status, User oper) {
        ManageLog manageLog = new ManageLog(ManageLog.MANAGE_LOG_XBLOG,
                        content, action, ManageLogType.COMMENT, obj,
                        oper == null ? null : oper.getId());
        manageLog.setStatus(status);
        manageLogService.logManage(manageLog);
    }

    private boolean beforeMessageManage(User oper, short action,
                    Message message, Object... args) {
        if (messageManageListeners != null) {
            for (MessageManageListener listener : messageManageListeners) {
                if (!listener.beforeMessageManage(oper, action, message, args)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void afterMessageManage(User oper, short action, short result,
                    Message message, Object... args) {
        if (messageManageListeners != null) {
            for (MessageManageListener listener : messageManageListeners) {
                listener.afterMessageManage(oper, action, result, message, args);
            }
        }
    }

    public void setMessageDAO(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public MessageDAO getMessageDAO() {
        return this.messageDAO;
    }

    /**
     * @return the blogConfigService
     */
    public BlogConfigService getBlogConfigService() {
        return blogConfigService;
    }

    /**
     * @param blogConfigService
     *            the blogConfigService to set
     */
    public void setBlogConfigService(BlogConfigService blogConfigService) {
        this.blogConfigService = blogConfigService;
    }

    public void setManageLogService(ManageLogService manageLogService) {
        this.manageLogService = manageLogService;
    }

    public ManageLogService getManageLogService() {
        return this.manageLogService;
    }

    public void setMessageManageListeners(
                    List<MessageManageListener> messageManageListeners) {
        this.messageManageListeners = messageManageListeners;
    }

    public List<MessageManageListener> getMessageManageListeners() {
        return messageManageListeners;
    }

    public void registerMessageManageListener(
                    MessageManageListener messageManageListener) {
        if (messageManageListeners == null) {
            messageManageListeners = new ArrayList<MessageManageListener>();
        }
        messageManageListeners.add(messageManageListener);
    }

}
