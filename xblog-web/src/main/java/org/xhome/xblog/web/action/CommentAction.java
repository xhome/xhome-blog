package org.xhome.xblog.web.action;

import javax.servlet.http.HttpServletRequest;

import org.jhat.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.http.response.Result;
import org.xhome.validator.CommonValidator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.web.util.ValidatorUtils;
import org.xhome.xblog.Comment;
import org.xhome.xblog.core.service.CommentService;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Oct 11, 20138:45:07 PM
 * @describe 
 */
@Controller
public class CommentAction {

	@Autowired(required = false)
	private CommentService commentService;
	private Logger logger = LoggerFactory.getLogger(CommentAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_COMMENT_ADD			= "xblog/comment/add.do";
	public final static String	RM_COMMENT_UPDATE		= "xblog/comment/update.do";
	public final static String	RM_COMMENT_LOCK			= "xblog/comment/lock.do";
	public final static String	RM_COMMENT_UNLOCK		= "xblog/comment/unlock.do";
	public final static String	RM_COMMENT_REMOVE		= "xblog/comment/remove.do";
	public final static String	RM_COMMENT_DELETE		= "xblog/comment/delete.do";
	
	public final static String	RM_COMMENT_UPDATEABLE	= "xblog/comment/updateable.do";
	public final static String	RM_COMMENT_LOCKED		= "xblog/comment/locked.do";
	public final static String	RM_COMMENT_REMOVEABLE	= "xblog/comment/removeable.do";
	public final static String	RM_COMMENT_DELETEABLE	= "xblog/comment/deleteable.do";
	public final static String	RM_COMMENT_GET			= "xblog/comment/get.do";
	public final static String	RM_COMMENT_QUERY		= "xblog/comment/query.do";
	public final static String	RM_COMMENT_COUNT		= "xblog/comment/count.do";
	
	@InitBinder
	public void initBinder(HttpServletRequest request, WebDataBinder binder) {
		String uri = request.getRequestURI();
		commonValidator.setValidators(validatorMapping.getValidatorByUri(uri));
		binder.setValidator(commonValidator);
		if (logger.isDebugEnabled()) {
			logger.debug("init binder for " + uri);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_ADD, method = RequestMethod.POST)
	public Object addComment(@Validated @RequestAttribute("comment") Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setOwner(request, comment);
			AuthUtils.setModifier(request, comment);
			status = (short) commentService.addComment(user, comment);
			if (status == Status.SUCCESS) {
				msg = "为文章" + comment.getArticle().getId() + "添加评论" + comment.getContent() + "成功";
				r = new Result(status, msg, comment);
			} else {
				msg = "为文章" + comment.getArticle().getId() + "添加评论" + comment.getContent() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_UPDATE, method = RequestMethod.POST)
	public Object updateComment(@Validated Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, comment);
			status = (short) commentService.updateComment(user, comment);
			if (status == Status.SUCCESS) {
				msg = "为文章" + comment.getArticle().getId() + "更新评论[" + comment.getId() + "]" + comment.getContent() + "成功";
				r = new Result(status, msg, comment);
			} else {
				msg = "为文章" + comment.getArticle().getId() + "更新评论[" + comment.getId() + "]" + comment.getContent() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_LOCK, method = RequestMethod.POST)
	public Object lockComment(@Validated Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, comment);
			status = (short) commentService.lockComment(user, comment);
			if (status == Status.SUCCESS) {
				msg = "为文章" + comment.getArticle().getId() + "锁定评论[" + comment.getId() + "]" + comment.getContent() + "成功";
				r = new Result(status, msg, comment);
			} else {
				msg = "为文章" + comment.getArticle().getId() + "锁定评论[" + comment.getId() + "]" + comment.getContent() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_UNLOCK, method = RequestMethod.POST)
	public Object unlockComment(@Validated Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, comment);
			status = (short) commentService.unlockComment(user, comment);
			if (status == Status.SUCCESS) {
				msg = "为文章" + comment.getArticle().getId() + "解锁评论[" + comment.getId() + "]" + comment.getContent() + "成功";
				r = new Result(status, msg, comment);
			} else {
				msg = "为文章" + comment.getArticle().getId() + "解锁评论[" + comment.getId() + "]" + comment.getContent() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_REMOVE, method = RequestMethod.POST)
	public Object removeComment(@Validated Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, comment);
			status = (short) commentService.removeComment(user, comment);
			if (status == Status.SUCCESS) {
				msg = "为文章" + comment.getArticle().getId() + "移除评论[" + comment.getId() + "]" + comment.getContent() + "成功";
				r = new Result(status, msg, comment);
			} else {
				msg = "为文章" + comment.getArticle().getId() + "移除评论[" + comment.getId() + "]" + comment.getContent() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_DELETE, method = RequestMethod.POST)
	public Object deleteComment(@Validated Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			status = (short) commentService.deleteComment(user, comment);
			if (status == Status.SUCCESS) {
				msg = "为文章" + comment.getArticle().getId() + "删除评论[" + comment.getId() + "]" + comment.getContent() + "成功";
				r = new Result(status, msg, comment);
			} else {
				msg = "为文章" + comment.getArticle().getId() + "删除评论[" + comment.getId() + "]" + comment.getContent() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_UPDATEABLE, method = RequestMethod.GET)
	public Object isCommentUpdateable(@Validated Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = commentService.isCommentUpdateable(user, comment);
			if (is) {
				msg = "查询到评论[" + comment.getId() + "]" + comment.getContent() + "可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到评论[" + comment.getId() + "]" + comment.getContent() + "不可以更新";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_LOCKED, method = RequestMethod.GET)
	public Object isCommentLocked(@Validated Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = commentService.isCommentLocked(user, comment);
			if (is) {
				msg = "查询到评论[" + comment.getId() + "]" + comment.getContent() + "已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到评论[" + comment.getId() + "]" + comment.getContent() + "未被锁定";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_REMOVEABLE, method = RequestMethod.GET)
	public Object isCommentRemoveable(@Validated Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = commentService.isCommentRemoveable(user, comment);
			if (is) {
				msg = "查询到评论[" + comment.getId() + "]" + comment.getContent() + "可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到评论[" + comment.getId() + "]" + comment.getContent() + "不可以移除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_DELETEABLE, method = RequestMethod.GET)
	public Object isCommentDeleteable(@Validated Comment comment, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = commentService.isCommentDeleteable(user, comment);
			if (is) {
				msg = "查询到评论[" + comment.getId() + "]" + comment.getContent() + "可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到评论[" + comment.getId() + "]" + comment.getContent() + "不可以删除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_COMMENT_GET, method = RequestMethod.GET)
	public Object getComment(@RequestParam(value = "id") Long id, HttpServletRequest request) {
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
		Result r = new Result(status, msg, comment);
		
		if (logger.isInfoEnabled()) {
			
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
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
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
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
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	public CommentService getCommentService() {
		return commentService;
	}
	
}
