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
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleUserPermission;
import org.xhome.xblog.ManageLogType;
import org.xhome.xblog.core.dao.ArticleUserPermissionDAO;
import org.xhome.xblog.core.listener.ArticleUserPermissionManageListener;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:53:33 PM
 * @describe 
 */
@Service
public class ArticleUserPermissionServiceImpl implements ArticleUserPermissionService {
	
	@Autowired(required = false)
	private ArticleUserPermissionDAO	articleUserPermissionDAO;
	@Autowired(required = false)
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<ArticleUserPermissionManageListener> articleUserPermissionManageListeners;
	
	private Logger	logger;
	
	public ArticleUserPermissionServiceImpl() {
		logger = LoggerFactory.getLogger(ArticleUserPermissionService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addArticleUserPermission(User oper, ArticleUserPermission articleUserPermission) {
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.ADD, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.ADD, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.ADD, Status.BLOCKED, articleUserPermission);
			return Status.BLOCKED;
		}
		
		if (articleUserPermissionDAO.isArticleUserPermissionExists(articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add article {}[{}] permission {} for user {}[{}], but it's already exists", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.ADD, null, Status.EXISTS, oper);
			this.afterArticleUserPermissionManage(oper, Action.ADD, Status.EXISTS, articleUserPermission);
			return Status.EXISTS;
		}
		
		articleUserPermission.setStatus(Status.OK);
		articleUserPermission.setVersion((short)0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		articleUserPermission.setCreated(t);
		articleUserPermission.setModified(t);
		
		short r = articleUserPermissionDAO.addArticleUserPermission(articleUserPermission) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("fail to add article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.ADD, articleUserPermission.getId(), r, oper);
		this.afterArticleUserPermissionManage(oper, Action.ADD, r, articleUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateArticleUserPermission(User oper, ArticleUserPermission articleUserPermission) {
		long id = articleUserPermission.getId();
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.UPDATE, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.UPDATE, Status.BLOCKED, articleUserPermission);
			return Status.BLOCKED;
		}
		
		ArticleUserPermission old = articleUserPermissionDAO.queryArticleUserPermission(id);
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update article {}[{}] permission {} for user {}[{}], but it's not exists", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterArticleUserPermissionManage(oper, Action.UPDATE, Status.NOT_EXISTS, articleUserPermission);
			return Status.NOT_EXISTS;
		}
		
		if (!old.getVersion().equals(articleUserPermission.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update article {}[{}] permission {} for user {}[{}], but version not match", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			this.afterArticleUserPermissionManage(oper, Action.UPDATE, Status.VERSION_NOT_MATCH, articleUserPermission);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UPDATE, id, status, oper);
			this.afterArticleUserPermissionManage(oper, Action.UPDATE, Status.EXISTS, articleUserPermission);
			return status;
		}
		
		articleUserPermission.setOwner(old.getOwner());
		articleUserPermission.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		articleUserPermission.setModified(t);
		
		short r  = articleUserPermissionDAO.updateArticleUserPermission(articleUserPermission) == 1 ? Status.SUCCESS : Status.ERROR;
		if (r == Status.SUCCESS) {
			articleUserPermission.incrementVersion();
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("fail to update article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.UPDATE, id, r, oper);
		this.afterArticleUserPermissionManage(oper, Action.UPDATE, r, articleUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockArticleUserPermission(User oper, ArticleUserPermission articleUserPermission) {
		long id = articleUserPermission.getId();
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.LOCK, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.LOCK, Status.BLOCKED, articleUserPermission);
			return Status.BLOCKED;
		}
		
		short r = articleUserPermissionDAO.lockArticleUserPermission(articleUserPermission) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("fail to lock article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.LOCK, id, r, oper);
		this.afterArticleUserPermissionManage(oper, Action.LOCK, r, articleUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockArticleUserPermission(User oper, ArticleUserPermission articleUserPermission) {
		long id = articleUserPermission.getId();
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.UNLOCK, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to unlock article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.UNLOCK, Status.BLOCKED, articleUserPermission);
			return Status.BLOCKED;
		}
		
		short r = articleUserPermissionDAO.unlockArticleUserPermission(articleUserPermission) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("fail to unlock article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.UNLOCK, id, r, oper);
		this.afterArticleUserPermissionManage(oper, Action.UNLOCK, r, articleUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeArticleUserPermission(User oper, ArticleUserPermission articleUserPermission) {
		long id = articleUserPermission.getId();
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.REMOVE, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.REMOVE, Status.BLOCKED, articleUserPermission);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (articleUserPermissionDAO.isArticleUserPermissionRemoveable(articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			}
			articleUserPermissionDAO.removeArticleUserPermission(articleUserPermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("article {}[{}] permission {} for user {}[{}] isn't removeable", articleTitle, articleId, permission, userName, userId);
			}
			r = Status.NO_REMOVE;
		}
		
		this.logManage(mstr, Action.REMOVE, id, r, oper);
		this.afterArticleUserPermissionManage(oper, Action.REMOVE, r, articleUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteArticleUserPermission(User oper, ArticleUserPermission articleUserPermission) {
		long id = articleUserPermission.getId();
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.DELETE, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.DELETE, Status.BLOCKED, articleUserPermission);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (articleUserPermissionDAO.isArticleUserPermissionDeleteable(articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			}
			articleUserPermissionDAO.deleteArticleUserPermission(articleUserPermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("article {}[{}] permission {} for user {}[{}] isn't deleteable", articleTitle, articleId, permission, userName, userId);
			}
			r = Status.NO_DELETE;
		}

		this.logManage(mstr, Action.DELETE, id, r, oper);
		this.afterArticleUserPermissionManage(oper, Action.DELETE, r, articleUserPermission);
		return r;
	}
	
	@Override
	public boolean isArticleUserPermissionExists(User oper, ArticleUserPermission articleUserPermission) {
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.IS_EXISTS, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge exists of article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.IS_EXISTS, Status.BLOCKED, articleUserPermission);
			return false;
		}
		
		boolean e = articleUserPermissionDAO.isArticleUserPermissionExists(articleUserPermission);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("not exists of article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			}
		}
		
		this.logManage(mstr, Action.IS_EXISTS, articleUserPermission.getId(), Status.SUCCESS, oper);
		this.afterArticleUserPermissionManage(oper, Action.IS_EXISTS, Status.SUCCESS, articleUserPermission);
		return e;
	}
	
	@Override
	public boolean isArticleUserPermissionUpdateable(User oper, ArticleUserPermission articleUserPermission) {
		long id = articleUserPermission.getId();
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.IS_UPDATEABLE, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge updateable of article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_UPDATEABLE, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, articleUserPermission);
			return false;
		}
		
		boolean e = articleUserPermissionDAO.isArticleUserPermissionUpdateable(articleUserPermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}] permission {} for user {}[{}] is updateable", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("article {}[{}] permission {} for user {}[{}] isn't updateable", articleTitle, articleId, permission, userName, userId);
			}
		}
		
		this.logManage(mstr, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterArticleUserPermissionManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, articleUserPermission);
		return e;
	}
	
	@Override
	public boolean isArticleUserPermissionLocked(User oper, ArticleUserPermission articleUserPermission) {
		long id = articleUserPermission.getId();
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.IS_LOCKED, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge locked of article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.IS_LOCKED, Status.BLOCKED, articleUserPermission);
			return false;
		}
		
		boolean e = articleUserPermissionDAO.isArticleUserPermissionLocked(articleUserPermission);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}] permission {} for user {}[{}] is locked", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("article {}[{}] permission {} for user {}[{}] isn't locked", articleTitle, articleId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterArticleUserPermissionManage(oper, Action.IS_LOCKED, Status.SUCCESS, articleUserPermission);
		return e;
	}
	
	@Override
	public boolean isArticleUserPermissionRemoveable(User oper, ArticleUserPermission articleUserPermission) {
		long id = articleUserPermission.getId();
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.IS_REMOVEABLE, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge removeable of article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_REMOVEABLE, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, articleUserPermission);
			return false;
		}
		
		boolean e = articleUserPermissionDAO.isArticleUserPermissionRemoveable(articleUserPermission);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}] permission {} for user {}[{}] is removeable", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("article {}[{}] permission {} for user {}[{}] isn't removeable", articleTitle, articleId, permission, userName, userId);
			}
		}
		
		this.logManage(mstr, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterArticleUserPermissionManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, articleUserPermission);
		return e;
	}
	
	@Override
	public boolean isArticleUserPermissionDeleteable(User oper, ArticleUserPermission articleUserPermission) {
		long id = articleUserPermission.getId();
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
		int permission = articleUserPermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeArticleUserPermissionManage(oper, Action.IS_DELETEABLE, articleUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge deleteable of article {}[{}] permission {} for user {}[{}], but it's blocked", articleTitle, articleId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_DELETEABLE, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, articleUserPermission);
			return false;
		}
		
		boolean e = articleUserPermissionDAO.isArticleUserPermissionDeleteable(articleUserPermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}] permission {} for user {}[{}] is deleteable", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("article {}[{}] permission {} for user {}[{}] isn't deleteable", articleTitle, articleId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterArticleUserPermissionManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, articleUserPermission);
		return e;
	}
	
	@Override
	public ArticleUserPermission getArticleUserPermission(User oper, long id) {
		if (!this.beforeArticleUserPermissionManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get article user permission of id {}, but it's blocked", id);
			}
			
			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}
		
		ArticleUserPermission articleUserPermission = articleUserPermissionDAO.queryArticleUserPermission(id);
		
		String mstr = null;
		if (logger.isDebugEnabled()) {
			if (articleUserPermission != null) {
				Article article = articleUserPermission.getArticle();
				User user = articleUserPermission.getUser();
				Long articleId = article != null ? article.getId() : null, userId = user != null ? user.getId() : null;
				String articleTitle = article != null ? article.getTitle() : "", userName = user != null ? user.getName() : "";
				int permission = articleUserPermission.getPermission();
				mstr = articleTitle + "(" + articleId + ")" + ", " + userName + "(" + userId + ")";
				logger.debug("get article {}[{}] permission {} for user {}[{}]", articleTitle, articleId, permission, userName, userId);
			} else {
				logger.debug("article user permission of id {} is not exists", id);
			}
		}
		
		this.logManage(mstr, Action.GET, id, Status.SUCCESS, oper);
		this.afterArticleUserPermissionManage(oper, Action.GET, Status.SUCCESS, articleUserPermission, id);
		return articleUserPermission;
	}
	
	@Override
	public List<ArticleUserPermission> getArticleUserPermissions(User oper) {
		return getArticleUserPermissions(oper, null);
	}
	
	@Override
	public List<ArticleUserPermission> getArticleUserPermissions(User oper, QueryBase query) {
		if (!this.beforeArticleUserPermissionManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query article user permissions, but it's blocked");
			}
			
			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
		List<ArticleUserPermission> results = articleUserPermissionDAO.queryArticleUserPermissions(query);
		if (query != null) {
			query.setResults(results);
			long total = articleUserPermissionDAO.countArticleUserPermissions(query);
			query.setTotalRow(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query article user permissions with parameters {}", query.getParameters());
			} else {
				logger.debug("query article user permissions");
			}
		}
		
		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterArticleUserPermissionManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return results;
	}
	
	@Override
	public long countArticleUserPermissions(User oper) {
		return countArticleUserPermissions(oper, null);
	}
	
	@Override
	public long countArticleUserPermissions(User oper, QueryBase query) {
		if (!this.beforeArticleUserPermissionManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count article user permissions, but it's blocked");
			}
			
			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterArticleUserPermissionManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = articleUserPermissionDAO.countArticleUserPermissions(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count article user permissions with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count article user permissions of {}", c);
			}
		}
		
		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterArticleUserPermissionManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private void logManage(String content, Short action, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, ManageLogType.TAG_USER_PERMISSION, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	private boolean beforeArticleUserPermissionManage(User oper, short action, ArticleUserPermission articleUserPermission, Object ...args) {
		if (articleUserPermissionManageListeners != null) {
			for (ArticleUserPermissionManageListener listener : articleUserPermissionManageListeners) {
				if (!listener.beforeArticleUserPermissionManage(oper, action, articleUserPermission, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterArticleUserPermissionManage(User oper, short action, short result, ArticleUserPermission articleUserPermission, Object ...args) {
		if (articleUserPermissionManageListeners != null) {
			for (ArticleUserPermissionManageListener listener : articleUserPermissionManageListeners) {
				listener.afterArticleUserPermissionManage(oper, action, result, articleUserPermission, args);
			}
		}
	}
	
	public void setArticleUserPermissionDAO(ArticleUserPermissionDAO articleUserPermissionDAO) {
		this.articleUserPermissionDAO = articleUserPermissionDAO;
	}
	
	public ArticleUserPermissionDAO getArticleUserPermissionDAO() {
		return this.articleUserPermissionDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}
	
	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setArticleUserPermissionManageListeners(List<ArticleUserPermissionManageListener> articleUserPermissionManageListeners) {
		this.articleUserPermissionManageListeners = articleUserPermissionManageListeners;
	}

	public List<ArticleUserPermissionManageListener> getArticleUserPermissionManageListeners() {
		return articleUserPermissionManageListeners;
	}
	
	public void registerArticleUserPermissionManageListener(ArticleUserPermissionManageListener articleUserPermissionManageListener) {
		if (articleUserPermissionManageListeners == null) {
			articleUserPermissionManageListeners = new ArrayList<ArticleUserPermissionManageListener>();
		}
		articleUserPermissionManageListeners.add(articleUserPermissionManageListener);
	}
	
}
