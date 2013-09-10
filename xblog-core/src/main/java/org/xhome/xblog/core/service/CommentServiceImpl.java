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
import org.xhome.xauth.User;
import org.xhome.xblog.Article;
import org.xhome.xblog.ManageLog;
import org.xhome.xblog.Comment;
import org.xhome.xblog.core.dao.CommentDAO;
import org.xhome.xblog.core.listener.CommentManageListener;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:53:33 PM
 * @describe 
 */
@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired(required = false)
	private CommentDAO	commentDAO;
	@Autowired(required = false)
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<CommentManageListener> commentManageListeners;
	
	private Logger	logger;
	
	public CommentServiceImpl() {
		logger = LoggerFactory.getLogger(CommentService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addComment(User oper, Comment comment) {
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "]";
		
		if (!this.beforeCommentManage(oper, Action.ADD, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add comment {} for article {}, but it's blocked", content, aid);
			}
			
			this.logManage(cstr, Action.ADD, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.ADD, Status.BLOCKED, comment);
			return Status.BLOCKED;
		}
		
		comment.setStatus(Status.OK);
		comment.setVersion((short)0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		comment.setCreated(t);
		comment.setModified(t);
		
		short r = commentDAO.addComment(comment) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add comment {} for article {}[{}]", content, aid);
			} else {
				logger.debug("fail to add comment {} for article {}[{}]", content, aid);
			}
		}

		this.logManage(cstr, Action.ADD, comment.getId(), r, oper);
		this.afterCommentManage(oper, Action.ADD, r, comment);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateComment(User oper, Comment comment) {
		Long id = comment.getId();
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "], Comment[" + id + "]";
		
		if (!this.beforeCommentManage(oper, Action.UPDATE, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update comment {}[{}], but it's blocked", content, id);
			}
			
			this.logManage(cstr, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.UPDATE, Status.BLOCKED, comment);
			return Status.BLOCKED;
		}
		
		Comment old = commentDAO.queryComment(id);
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update comment {}[{}], but it's not exists", content, id);
			}
			
			this.logManage(cstr, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterCommentManage(oper, Action.UPDATE, Status.NOT_EXISTS, comment);
			return Status.NOT_EXISTS;
		}
		
		String oldContent = old.getContent();
		
		if (!old.getVersion().equals(comment.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update comment {}[{}], but version not match", oldContent, id);
			}
			
			this.logManage(cstr, Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			this.afterCommentManage(oper, Action.UPDATE, Status.VERSION_NOT_MATCH, comment);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update comment {}[{}]", oldContent, id);
			}
			
			this.logManage(cstr, Action.UPDATE, id, status, oper);
			this.afterCommentManage(oper, Action.UPDATE, Status.EXISTS, comment);
			return status;
		}
		
		comment.setOwner(old.getOwner());
		comment.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		comment.setModified(t);
		
		short r  = commentDAO.updateComment(comment) == 1 ? Status.SUCCESS : Status.ERROR;
		if (r == Status.SUCCESS) {
			comment.incrementVersion();
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update comment {}[{}]", oldContent, id);
			} else {
				logger.debug("fail to update comment {}[{}]", oldContent, id);
			}
		}

		this.logManage(cstr, Action.UPDATE, id, r, oper);
		this.afterCommentManage(oper, Action.UPDATE, r, comment);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockComment(User oper, Comment comment) {
		Long id = comment.getId();
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "], Comment[" + id + "]";
		
		if (!this.beforeCommentManage(oper, Action.LOCK, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock comment {}[{}], but it's blocked", content, id);
			}
			
			this.logManage(cstr, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.LOCK, Status.BLOCKED, comment);
			return Status.BLOCKED;
		}
		
		short r = commentDAO.lockComment(comment) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock comment {}[{}]", content, id);
			} else {
				logger.debug("fail to lock comment {}[{}]", content, id);
			}
		}

		this.logManage(cstr, Action.LOCK, id, r, oper);
		this.afterCommentManage(oper, Action.LOCK, r, comment);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockComment(User oper, Comment comment) {
		Long id = comment.getId();
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "], Comment[" + id + "]";
		
		if (!this.beforeCommentManage(oper, Action.UNLOCK, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to unlock comment {}[{}], but it's blocked", content, id);
			}
			
			this.logManage(cstr, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.UNLOCK, Status.BLOCKED, comment);
			return Status.BLOCKED;
		}
		
		short r = commentDAO.unlockComment(comment) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock comment {}[{}]", content, id);
			} else {
				logger.debug("fail to unlock comment {}[{}]", content, id);
			}
		}

		this.logManage(cstr, Action.UNLOCK, id, r, oper);
		this.afterCommentManage(oper, Action.UNLOCK, r, comment);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeComment(User oper, Comment comment) {
		Long id = comment.getId();
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "], Comment[" + id + "]";
		
		if (!this.beforeCommentManage(oper, Action.REMOVE, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove comment {}[{}], but it's blocked", content, id);
			}
			
			this.logManage(cstr, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.REMOVE, Status.BLOCKED, comment);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (commentDAO.isCommentRemoveable(comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove comment {}[{}]", content, id);
			}
			commentDAO.removeComment(comment);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("comment {}[{}] isn't removeable", content, id);
			}
			r = Status.NO_REMOVE;
		}
		
		this.logManage(cstr, Action.REMOVE, id, r, oper);
		this.afterCommentManage(oper, Action.REMOVE, r, comment);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteComment(User oper, Comment comment) {
		Long id = comment.getId();
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "], Comment[" + id + "]";
		
		if (!this.beforeCommentManage(oper, Action.DELETE, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete comment {}[{}], but it's blocked", content, id);
			}
			
			this.logManage(cstr, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.DELETE, Status.BLOCKED, comment);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (commentDAO.isCommentDeleteable(comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete comment {}[{}]", content, comment.getId());
			}
			commentDAO.deleteComment(comment);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("comment {}[{}] isn't deleteable", content, comment.getId());
			}
			r = Status.NO_DELETE;
		}

		this.logManage(cstr, Action.DELETE, id, r, oper);
		this.afterCommentManage(oper, Action.DELETE, r, comment);
		return r;
	}
	
	@Override
	public boolean isCommentUpdateable(User oper, Comment comment) {
		Long id = comment.getId();
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "], Comment[" + id + "]";
		
		if (!this.beforeCommentManage(oper, Action.IS_UPDATEABLE, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge updateable of comment {}[{}], but it's blocked", content, id);
			}
			
			this.logManage(cstr, Action.IS_UPDATEABLE, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, comment);
			return false;
		}
		
		boolean e = commentDAO.isCommentUpdateable(comment);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("comment {}[{}] is updateable", content, id);
			} else {
				logger.debug("comment {}[{}] isn't updateable", content, id);
			}
		}
		
		this.logManage(cstr, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterCommentManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, comment);
		return e;
	}
	
	@Override
	public boolean isCommentLocked(User oper, Comment comment) {
		Long id = comment.getId();
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "], Comment[" + id + "]";
		
		if (!this.beforeCommentManage(oper, Action.IS_LOCKED, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge locked of comment {}[{}], but it's blocked", content, id);
			}
			
			this.logManage(cstr, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.IS_LOCKED, Status.BLOCKED, comment);
			return false;
		}
		
		boolean e = commentDAO.isCommentLocked(comment);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("comment {}[{}] is locked", content, id);
			} else {
				logger.debug("comment {}[{}] isn't locked", content, id);
			}
		}

		this.logManage(cstr, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterCommentManage(oper, Action.IS_LOCKED, Status.SUCCESS, comment);
		return e;
	}
	
	@Override
	public boolean isCommentRemoveable(User oper, Comment comment) {
		Long id = comment.getId();
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "], Comment[" + id + "]";
		
		if (!this.beforeCommentManage(oper, Action.IS_REMOVEABLE, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge removeable of comment {}[{}], but it's blocked", content, id);
			}
			
			this.logManage(cstr, Action.IS_REMOVEABLE, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, comment);
			return false;
		}
		
		boolean e = commentDAO.isCommentRemoveable(comment);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("comment {}[{}] is removeable", content, id);
			} else {
				logger.debug("comment {}[{}] isn't removeable", content, id);
			}
		}
		
		this.logManage(cstr, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterCommentManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, comment);
		return e;
	}
	
	@Override
	public boolean isCommentDeleteable(User oper, Comment comment) {
		Long id = comment.getId();
		Article article = comment.getArticle();
		long aid = article.getId();
		String content = comment.getContent();
		String cstr = "Article[" + aid + "], Comment[" + id + "]";
		
		if (!this.beforeCommentManage(oper, Action.IS_DELETEABLE, comment)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge deleteable of comment {}[{}], but it's blocked", content, id);
			}
			
			this.logManage(cstr, Action.IS_DELETEABLE, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, comment);
			return false;
		}
		
		boolean e = commentDAO.isCommentDeleteable(comment);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("comment {}[{}] is deleteable", content, id);
			} else {
				logger.debug("comment {}[{}] isn't deleteable", content, id);
			}
		}

		this.logManage(cstr, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterCommentManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, comment);
		return e;
	}
	
	@Override
	public Comment getComment(User oper, long id) {
		if (!this.beforeCommentManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get comment of id {}, but it's blocked", id);
			}
			
			this.logManage("Comment[" + id + "]", Action.GET, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}
		
		Comment comment = commentDAO.queryComment(id);
		
		String content = null;
		if (logger.isDebugEnabled()) {
			if (comment != null) {
				content = comment.getContent();
				logger.debug("get comment {}[{}]", content, id);
			} else {
				logger.debug("comment of id {} is not exists", id);
			}
		}
		
		this.logManage("Comment[" + id + "]", Action.GET, id, Status.SUCCESS, oper);
		this.afterCommentManage(oper, Action.GET, Status.SUCCESS, comment, id);
		return comment;
	}
	
	@Override
	public List<Comment> getComments(User oper) {
		return getComments(oper, null);
	}
	
	@Override
	public List<Comment> getComments(User oper, QueryBase query) {
		if (!this.beforeCommentManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query comments, but it's blocked");
			}
			
			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
		List<Comment> results = commentDAO.queryComments(query);
		if (query != null) {
			query.setResults(results);
			long total = commentDAO.countComments(query);
			query.setTotalRow(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query comments with parameters {}", query.getParameters());
			} else {
				logger.debug("query comments");
			}
		}
		
		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterCommentManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return results;
	}
	
	@Override
	public long countComments(User oper) {
		return countComments(oper, null);
	}
	
	@Override
	public long countComments(User oper, QueryBase query) {
		if (!this.beforeCommentManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count comments, but it's blocked");
			}
			
			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterCommentManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = commentDAO.countComments(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count comments with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count comments of {}", c);
			}
		}
		
		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterCommentManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private void logManage(String content, Short action, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, ManageLog.TYPE_COMMENT, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	private boolean beforeCommentManage(User oper, short action, Comment comment, Object ...args) {
		if (commentManageListeners != null) {
			for (CommentManageListener listener : commentManageListeners) {
				if (!listener.beforeCommentManage(oper, action, comment, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterCommentManage(User oper, short action, short result, Comment comment, Object ...args) {
		if (commentManageListeners != null) {
			for (CommentManageListener listener : commentManageListeners) {
				listener.afterCommentManage(oper, action, result, comment, args);
			}
		}
	}
	
	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}
	
	public CommentDAO getCommentDAO() {
		return this.commentDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}
	
	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setCommentManageListeners(List<CommentManageListener> commentManageListeners) {
		this.commentManageListeners = commentManageListeners;
	}

	public List<CommentManageListener> getCommentManageListeners() {
		return commentManageListeners;
	}
	
	public void registerCommentManageListener(CommentManageListener commentManageListener) {
		if (commentManageListeners == null) {
			commentManageListeners = new ArrayList<CommentManageListener>();
		}
		commentManageListeners.add(commentManageListener);
	}
	
}
