package org.xhome.xblog.web.action;

import java.util.List;

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
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleUserPermission;
import org.xhome.xblog.core.service.ArticleUserPermissionService;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description
 */
@Controller
public class ArticleUserPermissionAction extends AbstractAction {

	@Autowired
	private ArticleUserPermissionService permissionService;

	public final static String RM_ARTICLE_USER_PERMISSION_ADD = "xblog/permission/article/user/add";
	public final static String RM_ARTICLE_USER_PERMISSION_UPDATE = "xblog/permission/article/user/update";
	public final static String RM_ARTICLE_USER_PERMISSION_LOCK = "xblog/permission/article/user/lock";
	public final static String RM_ARTICLE_USER_PERMISSION_UNLOCK = "xblog/permission/article/user/unlock";
	public final static String RM_ARTICLE_USER_PERMISSION_DELETE = "xblog/permission/article/user/delete";

	public final static String RM_ARTICLE_USER_PERMISSION_EXISTS = "xblog/permission/article/user/exists";
	public final static String RM_ARTICLE_USER_PERMISSION_UPDATEABLE = "xblog/permission/article/user/updateable";
	public final static String RM_ARTICLE_USER_PERMISSION_LOCKED = "xblog/permission/article/user/locked";
	public final static String RM_ARTICLE_USER_PERMISSION_DELETEABLE = "xblog/permission/article/user/deleteable";
	public final static String RM_ARTICLE_USER_PERMISSION_GET = "xblog/permission/article/user/get";
	public final static String RM_ARTICLE_USER_PERMISSION_QUERY = "xblog/permission/article/user/query";
	public final static String RM_ARTICLE_USER_PERMISSION_COUNT = "xblog/permission/article/user/count";

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_ADD, method = RequestMethod.POST)
	public Object addArticleUserPermission(
			@Validated @RequestAttribute("permission") ArticleUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, permission);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.addArticleUserPermission(user,
				permission);
		Article article = permission.getArticle();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]添加用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "成功";
		} else {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]添加用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_UPDATE, method = RequestMethod.POST)
	public Object updateArticleUserPermission(
			@Validated @RequestAttribute("permission") ArticleUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.updateArticleUserPermission(user,
				permission);
		Article article = permission.getArticle();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]更新用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]更新用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_LOCK, method = RequestMethod.POST)
	public Object lockArticleUserPermission(
			@Validated @RequestAttribute("permission") ArticleUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.lockArticleUserPermission(user,
				permission);
		Article article = permission.getArticle();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]锁定用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]锁定用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_UNLOCK, method = RequestMethod.POST)
	public Object unlockArticleUserPermission(
			@Validated @RequestAttribute("permission") ArticleUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.unlockArticleUserPermission(user,
				permission);
		Article article = permission.getArticle();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]解锁用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]解锁用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_DELETE, method = RequestMethod.POST)
	public Object deleteArticleUserPermission(
			@Validated @RequestAttribute("permissions") List<ArticleUserPermission> permissions,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (ArticleUserPermission permission : permissions) {
			AuthUtils.setModifier(request, permission);
		}
		try {
			status = (short) permissionService.deleteArticleUserPermissions(
					user, permissions);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "用户为文章删除用户权限成功";
		} else {
			msg = "用户为文章删除用户权限失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permissions);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_EXISTS, method = RequestMethod.GET)
	public Object isArticleUserPermissionExists(
			@Validated @RequestAttribute("permission") ArticleUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean exists = permissionService.isArticleUserPermissionExists(user,
				permission);
		Article article = permission.getArticle();
		User puser = permission.getUser();
		if (exists) {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]存在";
		} else {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不存在";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, exists);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_UPDATEABLE, method = RequestMethod.GET)
	public Object isArticleUserPermissionUpdateable(
			@Validated @RequestAttribute("permission") ArticleUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = permissionService
				.isArticleUserPermissionUpdateable(user, permission);
		Article article = permission.getArticle();
		User puser = permission.getUser();
		if (updateable) {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以更新";
		} else {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_LOCKED, method = RequestMethod.GET)
	public Object isArticleUserPermissionLocked(
			@Validated @RequestAttribute("permission") ArticleUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = permissionService.isArticleUserPermissionLocked(user,
				permission);
		Article article = permission.getArticle();
		User puser = permission.getUser();
		if (locked) {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]已被锁定";
		} else {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_DELETEABLE, method = RequestMethod.GET)
	public Object isArticleUserPermissionDeleteable(
			@Validated @RequestAttribute("permission") ArticleUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = permissionService
				.isArticleUserPermissionDeleteable(user, permission);
		Article article = permission.getArticle();
		User puser = permission.getUser();
		if (deleteable) {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以删除";
		} else {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_GET, method = RequestMethod.GET)
	public Object getArticleUserPermission(@RequestParam("id") Long id,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		ArticleUserPermission permission = null;

		String msg = null;
		short status = Status.SUCCESS;

		permission = permissionService.getArticleUserPermission(user, id);
		if (permission != null) {
			Article article = permission.getArticle();
			User puser = permission.getUser();
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]";
		} else {
			status = Status.ERROR;
			msg = "文章查询失败";
		}

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_QUERY, method = RequestMethod.GET)
	public Object getArticleUserPermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (query == null) {
			query = new QueryBase();
		}
		if (logger.isInfoEnabled()) {
			logger.info("用户{}按条件{}查询文章用户权限信息", uname, query.getParameters());
		}
		permissionService.getArticleUserPermissions(user, query);

		String msg = "条件查询文章用户权限信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new DataResult(status, msg, query);
	}

	@RequestMapping(value = RM_ARTICLE_USER_PERMISSION_COUNT, method = RequestMethod.GET)
	public Object countArticleUserPermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (query == null) {
			query = new QueryBase();
		}
		if (logger.isInfoEnabled()) {
			logger.info("用户{}按条件{}统计文章用户权限信息", uname, query.getParameters());
		}
		long count = permissionService.countArticleUserPermissions(user, query);

		String msg = "条件统计文章用户权限信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setArticleUserPermissionService(
			ArticleUserPermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public ArticleUserPermissionService getArticleUserPermissionService() {
		return permissionService;
	}

}
