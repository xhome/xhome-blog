package org.xhome.xblog.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.xhome.web.action.AbstractAction;
import org.xhome.web.response.CommonResult;
import org.xhome.web.response.DataResult;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.Message;
import org.xhome.xblog.core.service.CategoryService;
import org.xhome.xblog.core.service.MessageService;
import org.xhome.xblog.core.service.TagService;

/**
 * @project xblog-web
 * @author jhat
 * @email cpf624@126.com
 * @date Oct 11, 20138:45:07 PM
 * @describe
 */
@Controller
public class MessageAction extends AbstractAction {

    @Autowired
    private MessageService     messageService;
    @Autowired
    private CategoryService    categoryService;
    @Autowired
    private TagService         tagService;

    public final static String RM_MESSAGE_ADD        = "xblog/message/add";
    public final static String RM_MESSAGE_UPDATE     = "xblog/message/update";
    public final static String RM_MESSAGE_LOCK       = "xblog/message/lock";
    public final static String RM_MESSAGE_UNLOCK     = "xblog/message/unlock";
    public final static String RM_MESSAGE_DELETE     = "xblog/message/delete";

    public final static String RM_MESSAGE_UPDATEABLE = "xblog/message/updateable";
    public final static String RM_MESSAGE_LOCKED     = "xblog/message/locked";
    public final static String RM_MESSAGE_DELETEABLE = "xblog/message/deleteable";
    public final static String RM_MESSAGE_GET        = "xblog/message/get";
    public final static String RM_MESSAGE_QUERY      = "xblog/message/query";
    public final static String RM_MESSAGE_COUNT      = "xblog/message/count";

    public final static String RM_MESSAGE_LIST       = "xblog/message/list";

    @RequestMapping(value = RM_MESSAGE_LIST, method = RequestMethod.GET)
    public Object listMessages(HttpServletRequest request,
                    @RequestParam(value = "page", required = false) Long page,
                    @RequestParam(value = "limit", required = false) Long limit) {
        User user = AuthUtils.getCurrentUser(request);
        Map<String, Object> data = new HashMap<String, Object>();
        String uname = user.getName();

        QueryBase categories = new QueryBase();
        categories.setLimit(Long.MAX_VALUE);
        categoryService.getCategorys(user, categories);
        data.put("categories", categories);

        QueryBase tags = new QueryBase();
        tags.setLimit(Long.MAX_VALUE);
        tagService.getTags(user, tags);
        data.put("tags", tags);

        QueryBase messages = new QueryBase();
        if (page != null && page > 0) {
            messages.setPage(page);
        }
        if (limit != null && limit > 0) {
            messages.setLimit(limit);
        }
        messageService.getMessages(user, messages);
        data.put("messages", messages);

        String msg = "留言信息查询成功";
        short status = Status.SUCCESS;

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, uname, msg);
        }

        return new CommonResult(status, msg, data);
    }

    @RequestMapping(value = RM_MESSAGE_ADD, method = RequestMethod.POST)
    public Object addMessage(
                    @Validated @RequestAttribute("message") Message message,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        AuthUtils.setOwner(request, message);
        AuthUtils.setModifier(request, message);
        status = (short) messageService.addMessage(user, message);
        if (status == Status.SUCCESS) {
            msg = "添加留言[" + message.getId() + "]成功";
        } else {
            msg = "添加留言[" + message.getId() + "]失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, user.getName(), msg);
        }

        return new CommonResult(status, msg, message);
    }

    @RequestMapping(value = RM_MESSAGE_UPDATE, method = RequestMethod.POST)
    public Object updateMessage(
                    @Validated @RequestAttribute("message") Message message,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        AuthUtils.setModifier(request, message);
        status = (short) messageService.updateMessage(user, message);
        if (status == Status.SUCCESS) {
            msg = "回复留言[" + message.getId() + "]成功";
        } else {
            msg = "回复留言[" + message.getId() + "]失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, user.getName(), msg);
        }

        return new CommonResult(status, msg, message);
    }

    // @RequestMapping(value = RM_MESSAGE_LOCK, method = RequestMethod.POST)
    public Object lockMessage(
                    @Validated @RequestAttribute("message") Message message,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        AuthUtils.setModifier(request, message);
        status = (short) messageService.lockMessage(user, message);
        if (status == Status.SUCCESS) {
            msg = "锁定留言[" + message.getId() + "]成功";
        } else {
            msg = "锁定留言[" + message.getId() + "]失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, user.getName(), msg);
        }

        return new CommonResult(status, msg, message);
    }

    // @RequestMapping(value = RM_MESSAGE_UNLOCK, method = RequestMethod.POST)
    public Object unlockMessage(
                    @Validated @RequestAttribute("message") Message message,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        AuthUtils.setModifier(request, message);
        status = (short) messageService.unlockMessage(user, message);
        if (status == Status.SUCCESS) {
            msg = "解锁留言[" + message.getId() + "]成功";
        } else {
            msg = "解锁留言[" + message.getId() + "]失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, user.getName(), msg);
        }

        return new CommonResult(status, msg, message);
    }

    @RequestMapping(value = RM_MESSAGE_DELETE, method = RequestMethod.POST)
    public Object deleteMessage(
                    @Validated @RequestAttribute("messages") List<Message> messages,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        for (Message message : messages) {
            AuthUtils.setModifier(request, message);
        }
        try {
            status = (short) messageService.deleteMessages(user, messages);
        } catch (RuntimeException e) {
            status = Status.ERROR;
        }

        if (status == Status.SUCCESS) {
            msg = "删除留言成功";
        } else {
            msg = "删除留言失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, user.getName(), msg);
        }

        return new CommonResult(status, msg, messages);
    }

    // @RequestMapping(value = RM_MESSAGE_UPDATEABLE, method =
    // RequestMethod.GET)
    public Object isMessageUpdateable(
                    @Validated @RequestAttribute("message") Message message,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        boolean updateable = messageService.isMessageUpdateable(user, message);
        if (updateable) {
            msg = "查询到留言[" + message.getId() + "]可以更新";
        } else {
            msg = "查询到留言[" + message.getId() + "]不可以更新";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, user.getName(), msg);
        }

        return new CommonResult(status, msg, updateable);
    }

    // @RequestMapping(value = RM_MESSAGE_LOCKED, method = RequestMethod.GET)
    public Object isMessageLocked(
                    @Validated @RequestAttribute("message") Message message,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        boolean locked = messageService.isMessageLocked(user, message);
        if (locked) {
            msg = "查询到留言[" + message.getId() + "]已被锁定";
        } else {
            msg = "查询到留言[" + message.getId() + "]未被锁定";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, user.getName(), msg);
        }

        return new CommonResult(status, msg, locked);
    }

    // @RequestMapping(value = RM_MESSAGE_DELETEABLE, method =
    // RequestMethod.GET)
    public Object isMessageDeleteable(
                    @Validated @RequestAttribute("message") Message message,
                    HttpServletRequest request) {
        short status = Status.SUCCESS;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        boolean deleteable = messageService.isMessageDeleteable(user, message);
        if (deleteable) {
            msg = "查询到留言[" + message.getId() + "]可以删除";
        } else {
            msg = "查询到留言[" + message.getId() + "]不可以删除";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, user.getName(), msg);
        }

        return new CommonResult(status, msg, deleteable);
    }

    @RequestMapping(value = RM_MESSAGE_GET, method = RequestMethod.GET)
    public Object getMessage(@RequestParam(value = "id") Long id,
                    HttpServletRequest request) {
        User user = AuthUtils.getCurrentUser(request);
        String uname = user.getName();
        String msg = null;
        short status = Status.SUCCESS;
        Message message = null;

        message = messageService.getMessage(user, id);
        if (message != null) {
            msg = "留言[" + id + "]查询成功";
        } else {
            status = Status.ERROR;
            msg = "留言查询失败";
        }

        if (logger.isInfoEnabled()) {

            logger.info("[{}] {} {}", status, uname, msg);
        }

        return new CommonResult(status, msg, message);
    }

    @RequestMapping(value = RM_MESSAGE_QUERY, method = RequestMethod.GET)
    public Object getMessages(QueryBase query, HttpServletRequest request) {
        User user = AuthUtils.getCurrentUser(request);
        String uname = user.getName();

        if (query == null) {
            query = new QueryBase();
        }
        if (logger.isInfoEnabled()) {
            logger.info("用户{}按条件{}查询留言信息", uname, query.getParameters());
        }
        messageService.getMessages(user, query);

        String msg = "条件查询留言信息";
        short status = Status.SUCCESS;

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, uname, msg);
        }

        return new DataResult(status, msg, query);
    }

    // @RequestMapping(value = RM_MESSAGE_COUNT, method = RequestMethod.GET)
    public Object countMessages(QueryBase query, HttpServletRequest request) {
        User user = AuthUtils.getCurrentUser(request);
        String uname = user.getName();

        if (query == null) {
            query = new QueryBase();
        }
        if (logger.isInfoEnabled()) {
            logger.info("用户{}按条件{}统计留言信息", uname, query.getParameters());
        }
        long count = messageService.countMessages(user, query);

        String msg = "条件统计留言信息，共" + count;
        short status = Status.SUCCESS;

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, uname, msg);
        }

        return new CommonResult(status, msg, count);
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    /**
     * @return the categoryService
     */
    public CategoryService getCategoryService() {
        return categoryService;
    }

    /**
     * @param categoryService
     *            the categoryService to set
     */
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * @return the tagService
     */
    public TagService getTagService() {
        return tagService;
    }

    /**
     * @param tagService
     *            the tagService to set
     */
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

}
