package org.xhome.xblog.web.action;

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
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.Comment;
import org.xhome.xblog.core.service.CommentService;

/**
 * @project xblog-web
 * @author jhat
 * @email cpf624@126.com
 * @date Oct 11, 20138:45:07 PM
 * @describe
 */
@Controller
public class CommentAction extends AbstractAction {

	@Autowired
	private CommentService commentService;

	public final static String RM_COMMENT_ADD = "xblog/comment/add";
	public final static String RM_COMMENT_UPDATE = "xblog/comment/update";
	public final static String RM_COMMENT_LOCK = "xblog/comment/lock";
	public final static String RM_COMMENT_UNLOCK = "xblog/comment/unlock";
	public final static String RM_COMMENT_REMOVE = "xblog/comment/remove";
	public final static String RM_COMMENT_DELETE = "xblog/comment/delete";

	public final static String RM_COMMENT_UPDATEABLE = "xblog/comment/updateable";
	public final static String RM_COMMENT_LOCKED = "xblog/comment/locked";
	public final static String RM_COMMENT_REMOVEABLE = "xblog/comment/removeable";
	public final static String RM_COMMENT_DELETEABLE = "xblog/comment/deleteable";
	public final static String RM_COMMENT_GET = "xblog/comment/get";
	public final static String RM_COMMENT_QUERY = "xblog/comment/query";
	public final static String RM_COMMENT_COUNT = "xblog/comment/count";

	@RequestMapping(value = RM_COMMENT_ADD, method = RequestMethod.POST)
	public Object addComment(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, comment);
		AuthUtils.setModifier(request, comment);
		status = (short) commentService.addComment(user, comment);
		if (status == Status.SUCCESS) {
			msg = "为文章" + comment.getArticle().getId() + "添加评论"
					+ comment.getContent() + "成功";
		} else {
			msg = "为文章" + comment.getArticle().getId() + "添加评论"
					+ comment.getContent() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, comment);
	}

	@RequestMapping(value = RM_COMMENT_UPDATE, method = RequestMethod.POST)
	public Object updateComment(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, comment);
		status = (short) commentService.updateComment(user, comment);
		if (status == Status.SUCCESS) {
			msg = "为文章" + comment.getArticle().getId() + "更新评论["
					+ comment.getId() + "]" + comment.getContent() + "成功";
		} else {
			msg = "为文章" + comment.getArticle().getId() + "更新评论["
					+ comment.getId() + "]" + comment.getContent() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, comment);
	}

	@RequestMapping(value = RM_COMMENT_LOCK, method = RequestMethod.POST)
	public Object lockComment(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, comment);
		status = (short) commentService.lockComment(user, comment);
		if (status == Status.SUCCESS) {
			msg = "为文章" + comment.getArticle().getId() + "锁定评论["
					+ comment.getId() + "]" + comment.getContent() + "成功";
		} else {
			msg = "为文章" + comment.getArticle().getId() + "锁定评论["
					+ comment.getId() + "]" + comment.getContent() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, comment);
	}

	@RequestMapping(value = RM_COMMENT_UNLOCK, method = RequestMethod.POST)
	public Object unlockComment(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, comment);
		status = (short) commentService.unlockComment(user, comment);
		if (status == Status.SUCCESS) {
			msg = "为文章" + comment.getArticle().getId() + "解锁评论["
					+ comment.getId() + "]" + comment.getContent() + "成功";
		} else {
			msg = "为文章" + comment.getArticle().getId() + "解锁评论["
					+ comment.getId() + "]" + comment.getContent() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, comment);
	}

	@RequestMapping(value = RM_COMMENT_REMOVE, method = RequestMethod.POST)
	public Object removeComment(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, comment);
		status = (short) commentService.removeComment(user, comment);
		if (status == Status.SUCCESS) {
			msg = "为文章" + comment.getArticle().getId() + "移除评论["
					+ comment.getId() + "]" + comment.getContent() + "成功";
		} else {
			msg = "为文章" + comment.getArticle().getId() + "移除评论["
					+ comment.getId() + "]" + comment.getContent() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, comment);
	}

	@RequestMapping(value = RM_COMMENT_DELETE, method = RequestMethod.POST)
	public Object deleteComment(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		status = (short) commentService.deleteComment(user, comment);
		if (status == Status.SUCCESS) {
			msg = "为文章" + comment.getArticle().getId() + "删除评论["
					+ comment.getId() + "]" + comment.getContent() + "成功";
		} else {
			msg = "为文章" + comment.getArticle().getId() + "删除评论["
					+ comment.getId() + "]" + comment.getContent() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, comment);
	}

	@RequestMapping(value = RM_COMMENT_UPDATEABLE, method = RequestMethod.GET)
	public Object isCommentUpdateable(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = commentService.isCommentUpdateable(user, comment);
		if (updateable) {
			msg = "查询到评论[" + comment.getId() + "]" + comment.getContent()
					+ "可以更新";
		} else {
			msg = "查询到评论[" + comment.getId() + "]" + comment.getContent()
					+ "不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	@RequestMapping(value = RM_COMMENT_LOCKED, method = RequestMethod.GET)
	public Object isCommentLocked(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = commentService.isCommentLocked(user, comment);
		if (locked) {
			msg = "查询到评论[" + comment.getId() + "]" + comment.getContent()
					+ "已被锁定";
		} else {
			msg = "查询到评论[" + comment.getId() + "]" + comment.getContent()
					+ "未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	@RequestMapping(value = RM_COMMENT_REMOVEABLE, method = RequestMethod.GET)
	public Object isCommentRemoveable(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean removeable = commentService.isCommentRemoveable(user, comment);
		if (removeable) {
			msg = "查询到评论[" + comment.getId() + "]" + comment.getContent()
					+ "可以移除";
		} else {
			msg = "查询到评论[" + comment.getId() + "]" + comment.getContent()
					+ "不可以移除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, removeable);
	}

	@RequestMapping(value = RM_COMMENT_DELETEABLE, method = RequestMethod.GET)
	public Object isCommentDeleteable(
			@Validated @RequestAttribute("comment") Comment comment,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = commentService.isCommentDeleteable(user, comment);
		if (deleteable) {
			msg = "查询到评论[" + comment.getId() + "]" + comment.getContent()
					+ "可以删除";
		} else {
			msg = "查询到评论[" + comment.getId() + "]" + comment.getContent()
					+ "不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	@RequestMapping(value = RM_COMMENT_GET, method = RequestMethod.GET)
	public Object getComment(@RequestParam(value = "id") Long id,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		String msg = null;
		short status = Status.SUCCESS;
		Comment comment = null;

		comment = commentService.getComment(user, id);
		if (comment != null) {
			msg = "评论[" + id + "]" + comment.getContent() + "查询成功";
		} else {
			status = Status.ERROR;
			msg = "评论查询失败";
		}

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, comment);
	}

	@RequestMapping(value = RM_COMMENT_QUERY, method = RequestMethod.GET)
	public Object getComments(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}查询评论信息", uname, query.getParameters());
			} else {
				query = new QueryBase();
				logger.info("用户{}查询评论信息", uname);
			}
		}
		commentService.getComments(user, query);

		String msg = "条件查询评论信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, query);
	}

	@RequestMapping(value = RM_COMMENT_COUNT, method = RequestMethod.GET)
	public Object countComments(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}统计评论信息", uname, query.getParameters());
			} else {
				logger.info("用户{}统计评论信息", uname);
			}
		}
		long count = commentService.countComments(user, query);

		String msg = "条件统计评论信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	public CommentService getCommentService() {
		return commentService;
	}

}
