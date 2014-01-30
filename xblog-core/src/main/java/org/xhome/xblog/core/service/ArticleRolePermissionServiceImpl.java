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
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.ManageLogService;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleRolePermission;
import org.xhome.xblog.ManageLogType;
import org.xhome.xblog.core.dao.ArticleRolePermissionDAO;
import org.xhome.xblog.core.listener.ArticleRolePermissionManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:33 PM
 * @describe
 */
@Service
public class ArticleRolePermissionServiceImpl implements
		ArticleRolePermissionService {

	@Autowired
	private ArticleRolePermissionDAO articleRolePermissionDAO;
	@Autowired
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<ArticleRolePermissionManageListener> articleRolePermissionManageListeners;

	private Logger logger;

	public ArticleRolePermissionServiceImpl() {
		logger = LoggerFactory.getLogger(ArticleRolePermissionService.class);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission) {
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.ADD,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to add article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.ADD, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.ADD,
					Status.BLOCKED, articleRolePermission);
			return Status.BLOCKED;
		}

		if (articleRolePermissionDAO
				.isArticleRolePermissionExists(articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to add article {}[{}] permission {} for role {}[{}], but it's already exists",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.ADD, null, Status.EXISTS, oper);
			this.afterArticleRolePermissionManage(oper, Action.ADD,
					Status.EXISTS, articleRolePermission);
			return Status.EXISTS;
		}

		articleRolePermission.setStatus(Status.OK);
		articleRolePermission.setVersion((short) 0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		articleRolePermission.setCreated(t);
		articleRolePermission.setModified(t);

		short r = articleRolePermissionDAO
				.addArticleRolePermission(articleRolePermission) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to add article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug(
						"fail to add article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.ADD, articleRolePermission.getId(), r, oper);
		this.afterArticleRolePermissionManage(oper, Action.ADD, r,
				articleRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission) {
		long id = articleRolePermission.getId();
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.UPDATE,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.UPDATE,
					Status.BLOCKED, articleRolePermission);
			return Status.BLOCKED;
		}

		ArticleRolePermission old = articleRolePermissionDAO
				.queryArticleRolePermission(id);

		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update article {}[{}] permission {} for role {}[{}], but it's not exists",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterArticleRolePermissionManage(oper, Action.UPDATE,
					Status.NOT_EXISTS, articleRolePermission);
			return Status.NOT_EXISTS;
		}

		if (!old.getVersion().equals(articleRolePermission.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update article {}[{}] permission {} for role {}[{}], but version not match",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UPDATE, id, Status.VERSION_NOT_MATCH,
					oper);
			this.afterArticleRolePermissionManage(oper, Action.UPDATE,
					Status.VERSION_NOT_MATCH, articleRolePermission);
			return Status.VERSION_NOT_MATCH;
		}

		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"it's not allowed to update article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UPDATE, id, status, oper);
			this.afterArticleRolePermissionManage(oper, Action.UPDATE,
					Status.EXISTS, articleRolePermission);
			return status;
		}

		articleRolePermission.setOwner(old.getOwner());
		articleRolePermission.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		articleRolePermission.setModified(t);

		short r = articleRolePermissionDAO
				.updateArticleRolePermission(articleRolePermission) == 1 ? Status.SUCCESS
				: Status.ERROR;
		if (r == Status.SUCCESS) {
			articleRolePermission.incrementVersion();
		}

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to update article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug(
						"fail to update article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.UPDATE, id, r, oper);
		this.afterArticleRolePermissionManage(oper, Action.UPDATE, r,
				articleRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission) {
		long id = articleRolePermission.getId();
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.LOCK,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to lock article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.LOCK,
					Status.BLOCKED, articleRolePermission);
			return Status.BLOCKED;
		}

		short r = articleRolePermissionDAO
				.lockArticleRolePermission(articleRolePermission) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to lock article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug(
						"fail to lock article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.LOCK, id, r, oper);
		this.afterArticleRolePermissionManage(oper, Action.LOCK, r,
				articleRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission) {
		long id = articleRolePermission.getId();
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.UNLOCK,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to unlock article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.UNLOCK,
					Status.BLOCKED, articleRolePermission);
			return Status.BLOCKED;
		}

		short r = articleRolePermissionDAO
				.unlockArticleRolePermission(articleRolePermission) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to unlock article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug(
						"fail to unlock article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.UNLOCK, id, r, oper);
		this.afterArticleRolePermissionManage(oper, Action.UNLOCK, r,
				articleRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission) {
		long id = articleRolePermission.getId();
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.REMOVE,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to remove article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.REMOVE,
					Status.BLOCKED, articleRolePermission);
			return Status.BLOCKED;
		}

		short r = Status.SUCCESS;
		if (articleRolePermissionDAO
				.isArticleRolePermissionRemoveable(articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"remove article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			}
			articleRolePermissionDAO
					.removeArticleRolePermission(articleRolePermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] isn't removeable",
						articleTitle, articleId, permission, roleName, roleId);
			}
			r = Status.NO_REMOVE;
		}

		this.logManage(mstr, Action.REMOVE, id, r, oper);
		this.afterArticleRolePermissionManage(oper, Action.REMOVE, r,
				articleRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission) {
		long id = articleRolePermission.getId();
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.DELETE,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to delete article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.DELETE,
					Status.BLOCKED, articleRolePermission);
			return Status.BLOCKED;
		}

		short r = Status.SUCCESS;
		if (articleRolePermissionDAO
				.isArticleRolePermissionDeleteable(articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"delete article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			}
			articleRolePermissionDAO
					.deleteArticleRolePermission(articleRolePermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] isn't deleteable",
						articleTitle, articleId, permission, roleName, roleId);
			}
			r = Status.NO_DELETE;
		}

		this.logManage(mstr, Action.DELETE, id, r, oper);
		this.afterArticleRolePermissionManage(oper, Action.DELETE, r,
				articleRolePermission);
		return r;
	}

	@Override
	public boolean isArticleRolePermissionExists(User oper,
			ArticleRolePermission articleRolePermission) {
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.IS_EXISTS,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge exists of article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.IS_EXISTS,
					Status.BLOCKED, articleRolePermission);
			return false;
		}

		boolean e = articleRolePermissionDAO
				.isArticleRolePermissionExists(articleRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"exists of article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug(
						"not exists of article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_EXISTS, articleRolePermission.getId(),
				Status.SUCCESS, oper);
		this.afterArticleRolePermissionManage(oper, Action.IS_EXISTS,
				Status.SUCCESS, articleRolePermission);
		return e;
	}

	@Override
	public boolean isArticleRolePermissionUpdateable(User oper,
			ArticleRolePermission articleRolePermission) {
		long id = articleRolePermission.getId();
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.IS_UPDATEABLE,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge updateable of article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.IS_UPDATEABLE, null, Status.BLOCKED,
					oper);
			this.afterArticleRolePermissionManage(oper, Action.IS_UPDATEABLE,
					Status.BLOCKED, articleRolePermission);
			return false;
		}

		boolean e = articleRolePermissionDAO
				.isArticleRolePermissionUpdateable(articleRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] is updateable",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] isn't updateable",
						articleTitle, articleId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterArticleRolePermissionManage(oper, Action.IS_UPDATEABLE,
				Status.SUCCESS, articleRolePermission);
		return e;
	}

	@Override
	public boolean isArticleRolePermissionLocked(User oper,
			ArticleRolePermission articleRolePermission) {
		long id = articleRolePermission.getId();
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.IS_LOCKED,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge locked of article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.IS_LOCKED,
					Status.BLOCKED, articleRolePermission);
			return false;
		}

		boolean e = articleRolePermissionDAO
				.isArticleRolePermissionLocked(articleRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] is locked",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] isn't locked",
						articleTitle, articleId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterArticleRolePermissionManage(oper, Action.IS_LOCKED,
				Status.SUCCESS, articleRolePermission);
		return e;
	}

	@Override
	public boolean isArticleRolePermissionRemoveable(User oper,
			ArticleRolePermission articleRolePermission) {
		long id = articleRolePermission.getId();
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.IS_REMOVEABLE,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge removeable of article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.IS_REMOVEABLE, null, Status.BLOCKED,
					oper);
			this.afterArticleRolePermissionManage(oper, Action.IS_REMOVEABLE,
					Status.BLOCKED, articleRolePermission);
			return false;
		}

		boolean e = articleRolePermissionDAO
				.isArticleRolePermissionRemoveable(articleRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] is removeable",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] isn't removeable",
						articleTitle, articleId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterArticleRolePermissionManage(oper, Action.IS_REMOVEABLE,
				Status.SUCCESS, articleRolePermission);
		return e;
	}

	@Override
	public boolean isArticleRolePermissionDeleteable(User oper,
			ArticleRolePermission articleRolePermission) {
		long id = articleRolePermission.getId();
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
				.getId() : null;
		String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = articleRolePermission.getPermission();
		String mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeArticleRolePermissionManage(oper, Action.IS_DELETEABLE,
				articleRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge deleteable of article {}[{}] permission {} for role {}[{}], but it's blocked",
						articleTitle, articleId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.IS_DELETEABLE, null, Status.BLOCKED,
					oper);
			this.afterArticleRolePermissionManage(oper, Action.IS_DELETEABLE,
					Status.BLOCKED, articleRolePermission);
			return false;
		}

		boolean e = articleRolePermissionDAO
				.isArticleRolePermissionDeleteable(articleRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] is deleteable",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug(
						"article {}[{}] permission {} for role {}[{}] isn't deleteable",
						articleTitle, articleId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterArticleRolePermissionManage(oper, Action.IS_DELETEABLE,
				Status.SUCCESS, articleRolePermission);
		return e;
	}

	@Override
	public ArticleRolePermission getArticleRolePermission(User oper, long id) {
		if (!this.beforeArticleRolePermissionManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to get article role permission of id {}, but it's blocked",
						id);
			}

			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.GET,
					Status.BLOCKED, null, id);
			return null;
		}

		ArticleRolePermission articleRolePermission = articleRolePermissionDAO
				.queryArticleRolePermission(id);

		String mstr = null;
		if (logger.isDebugEnabled()) {
			if (articleRolePermission != null) {
				Article article = articleRolePermission.getArticle();
				Role role = articleRolePermission.getRole();
				Long articleId = article != null ? article.getId() : null, roleId = role != null ? role
						.getId() : null;
				String articleTitle = article != null ? article.getTitle() : "", roleName = role != null ? role
						.getName() : null;
				;
				int permission = articleRolePermission.getPermission();
				mstr = articleTitle + "(" + articleId + ")" + ", " + roleName
						+ "(" + roleId + ")";
				logger.debug(
						"get article {}[{}] permission {} for role {}[{}]",
						articleTitle, articleId, permission, roleName, roleId);
			} else {
				logger.debug("article role permission of id {} is not exists",
						id);
			}
		}

		this.logManage(mstr, Action.GET, id, Status.SUCCESS, oper);
		this.afterArticleRolePermissionManage(oper, Action.GET, Status.SUCCESS,
				articleRolePermission, id);
		return articleRolePermission;
	}

	@Override
	public List<ArticleRolePermission> getArticleRolePermissions(User oper,
			QueryBase query) {
		if (!this.beforeArticleRolePermissionManage(oper, Action.QUERY, null,
				query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query article role permissions, but it's blocked");
			}

			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.QUERY,
					Status.BLOCKED, null, query);
			return null;
		}

		List<ArticleRolePermission> results = articleRolePermissionDAO
				.queryArticleRolePermissions(query);
		if (query != null) {
			query.setResults(results);
			long total = articleRolePermissionDAO
					.countArticleRolePermissions(query);
			query.setTotal(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug(
						"query article role permissions with parameters {}",
						query.getParameters());
			} else {
				logger.debug("query article role permissions");
			}
		}

		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterArticleRolePermissionManage(oper, Action.QUERY,
				Status.SUCCESS, null, query);
		return results;
	}

	@Override
	public long countArticleRolePermissions(User oper, QueryBase query) {
		if (!this.beforeArticleRolePermissionManage(oper, Action.COUNT, null,
				query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count article role permissions, but it's blocked");
			}

			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterArticleRolePermissionManage(oper, Action.COUNT,
					Status.BLOCKED, null, query);
			return -1;
		}

		long c = articleRolePermissionDAO.countArticleRolePermissions(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug(
						"count article role permissions with parameters {} of {}",
						query.getParameters(), c);
			} else {
				logger.debug("count article role permissions of {}", c);
			}
		}

		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterArticleRolePermissionManage(oper, Action.COUNT,
				Status.SUCCESS, null, query);
		return c;
	}

	private void logManage(String content, Short action, Long obj,
			Short status, User oper) {
		ManageLog manageLog = new ManageLog(ManageLog.MANAGE_LOG_XBLOG,
				content, action, ManageLogType.TAG_ROLE_PERMISSION, obj,
				oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}

	private boolean beforeArticleRolePermissionManage(User oper, short action,
			ArticleRolePermission articleRolePermission, Object... args) {
		if (articleRolePermissionManageListeners != null) {
			for (ArticleRolePermissionManageListener listener : articleRolePermissionManageListeners) {
				if (!listener.beforeArticleRolePermissionManage(oper, action,
						articleRolePermission, args)) {
					return false;
				}
			}
		}
		return true;
	}

	private void afterArticleRolePermissionManage(User oper, short action,
			short result, ArticleRolePermission articleRolePermission,
			Object... args) {
		if (articleRolePermissionManageListeners != null) {
			for (ArticleRolePermissionManageListener listener : articleRolePermissionManageListeners) {
				listener.afterArticleRolePermissionManage(oper, action, result,
						articleRolePermission, args);
			}
		}
	}

	public void setArticleRolePermissionDAO(
			ArticleRolePermissionDAO articleRolePermissionDAO) {
		this.articleRolePermissionDAO = articleRolePermissionDAO;
	}

	public ArticleRolePermissionDAO getArticleRolePermissionDAO() {
		return this.articleRolePermissionDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}

	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setArticleRolePermissionManageListeners(
			List<ArticleRolePermissionManageListener> articleRolePermissionManageListeners) {
		this.articleRolePermissionManageListeners = articleRolePermissionManageListeners;
	}

	public List<ArticleRolePermissionManageListener> getArticleRolePermissionManageListeners() {
		return articleRolePermissionManageListeners;
	}

	public void registerArticleRolePermissionManageListener(
			ArticleRolePermissionManageListener articleRolePermissionManageListener) {
		if (articleRolePermissionManageListeners == null) {
			articleRolePermissionManageListeners = new ArrayList<ArticleRolePermissionManageListener>();
		}
		articleRolePermissionManageListeners
				.add(articleRolePermissionManageListener);
	}

}
