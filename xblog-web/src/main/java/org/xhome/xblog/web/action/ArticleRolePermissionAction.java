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
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleRolePermission;
import org.xhome.xblog.core.service.ArticleRolePermissionService;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description
 */
@Controller
public class ArticleRolePermissionAction extends AbstractAction {

	@Autowired
	private ArticleRolePermissionService permissionService;

	public final static String RM_ARTICLE_ROLE_PERMISSION_ADD = "xblog/permission/article/role/add";
	public final static String RM_ARTICLE_ROLE_PERMISSION_UPDATE = "xblog/permission/article/role/update";
	public final static String RM_ARTICLE_ROLE_PERMISSION_LOCK = "xblog/permission/article/role/lock";
	public final static String RM_ARTICLE_ROLE_PERMISSION_UNLOCK = "xblog/permission/article/role/unlock";
	public final static String RM_ARTICLE_ROLE_PERMISSION_REMOVE = "xblog/permission/article/role/remove";
	public final static String RM_ARTICLE_ROLE_PERMISSION_DELETE = "xblog/permission/article/role/delete";

	public final static String RM_ARTICLE_ROLE_PERMISSION_EXISTS = "xblog/permission/article/role/exists";
	public final static String RM_ARTICLE_ROLE_PERMISSION_UPDATEABLE = "xblog/permission/article/role/updateable";
	public final static String RM_ARTICLE_ROLE_PERMISSION_LOCKED = "xblog/permission/article/role/locked";
	public final static String RM_ARTICLE_ROLE_PERMISSION_REMOVEABLE = "xblog/permission/article/role/removeable";
	public final static String RM_ARTICLE_ROLE_PERMISSION_DELETEABLE = "xblog/permission/article/role/deleteable";
	public final static String RM_ARTICLE_ROLE_PERMISSION_GET = "xblog/permission/article/role/get";
	public final static String RM_ARTICLE_ROLE_PERMISSION_QUERY = "xblog/permission/article/role/query";
	public final static String RM_ARTICLE_ROLE_PERMISSION_COUNT = "xblog/permission/article/role/count";

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_ADD, method = RequestMethod.POST)
	public Object addArticleRolePermission(
			@Validated @RequestAttribute("permission") ArticleRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, permission);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.addArticleRolePermission(user,
				permission);
		Article article = permission.getArticle();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]添加角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "成功";
		} else {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]添加角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_UPDATE, method = RequestMethod.POST)
	public Object updateArticleRolePermission(
			@Validated @RequestAttribute("permission") ArticleRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.updateArticleRolePermission(user,
				permission);
		Article article = permission.getArticle();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]更新角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]更新角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_LOCK, method = RequestMethod.POST)
	public Object lockArticleRolePermission(
			@Validated @RequestAttribute("permission") ArticleRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.lockArticleRolePermission(user,
				permission);
		Article article = permission.getArticle();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]锁定角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]锁定角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_UNLOCK, method = RequestMethod.POST)
	public Object unlockArticleRolePermission(
			@Validated @RequestAttribute("permission") ArticleRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.unlockArticleRolePermission(user,
				permission);
		Article article = permission.getArticle();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]解锁角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为文章" + article.getTitle() + "[" + article.getId()
					+ "]解锁角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_REMOVE, method = RequestMethod.POST)
	public Object removeArticleRolePermission(
			@Validated @RequestAttribute("permissions") List<ArticleRolePermission> permissions,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (ArticleRolePermission permission : permissions) {
			AuthUtils.setModifier(request, permission);
		}
		try {
			status = (short) permissionService.removeArticleRolePermissions(
					user, permissions);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "用户为文章移除角色权限成功";
		} else {
			msg = "用户为文章移除角色权限失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permissions);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_DELETE, method = RequestMethod.POST)
	public Object deleteArticleRolePermission(
			@Validated @RequestAttribute("permissions") List<ArticleRolePermission> permissions,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (ArticleRolePermission permission : permissions) {
			AuthUtils.setModifier(request, permission);
		}
		try {
			status = (short) permissionService.deleteArticleRolePermissions(
					user, permissions);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "用户为文章删除角色权限成功";
		} else {
			msg = "用户为文章删除角色权限失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permissions);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_EXISTS, method = RequestMethod.GET)
	public Object isArticleRolePermissionExists(
			@Validated @RequestAttribute("permission") ArticleRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean exists = permissionService.isArticleRolePermissionExists(user,
				permission);
		Article article = permission.getArticle();
		Role role = permission.getRole();
		if (exists) {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]存在";
		} else {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不存在";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, exists);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_UPDATEABLE, method = RequestMethod.GET)
	public Object isArticleRolePermissionUpdateable(
			@Validated @RequestAttribute("permission") ArticleRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = permissionService
				.isArticleRolePermissionUpdateable(user, permission);
		Article article = permission.getArticle();
		Role role = permission.getRole();
		if (updateable) {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以更新";
		} else {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_LOCKED, method = RequestMethod.GET)
	public Object isArticleRolePermissionLocked(
			@Validated @RequestAttribute("permission") ArticleRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = permissionService.isArticleRolePermissionLocked(user,
				permission);
		Article article = permission.getArticle();
		Role role = permission.getRole();
		if (locked) {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]已被锁定";
		} else {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_REMOVEABLE, method = RequestMethod.GET)
	public Object isArticleRolePermissionRemoveable(
			@Validated @RequestAttribute("permission") ArticleRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean removeable = permissionService
				.isArticleRolePermissionRemoveable(user, permission);
		Article article = permission.getArticle();
		Role role = permission.getRole();
		if (removeable) {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以移除";
		} else {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以移除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, removeable);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_DELETEABLE, method = RequestMethod.GET)
	public Object isArticleRolePermissionDeleteable(
			@Validated @RequestAttribute("permission") ArticleRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = permissionService
				.isArticleRolePermissionDeleteable(user, permission);
		Article article = permission.getArticle();
		Role role = permission.getRole();
		if (deleteable) {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以删除";
		} else {
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_GET, method = RequestMethod.GET)
	public Object getArticleRolePermission(@RequestParam("id") Long id,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		ArticleRolePermission permission = null;

		String msg = null;
		short status = Status.SUCCESS;

		permission = permissionService.getArticleRolePermission(user, id);
		if (permission != null) {
			Article article = permission.getArticle();
			Role role = permission.getRole();
			msg = "用户查询到文章" + article.getTitle() + "[" + article.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
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

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_QUERY, method = RequestMethod.GET)
	public Object getArticleRolePermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}查询文章角色权限信息", uname, query.getParameters());
			} else {
				query = new QueryBase();
				logger.info("用户{}查询文章角色权限信息", uname);
			}
		}
		permissionService.getArticleRolePermissions(user, query);

		String msg = "条件查询文章角色权限信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new DataResult(status, msg, query);
	}

	@RequestMapping(value = RM_ARTICLE_ROLE_PERMISSION_COUNT, method = RequestMethod.GET)
	public Object countArticleRolePermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}统计文章角色权限信息", uname, query.getParameters());
			} else {
				logger.info("用户{}统计文章角色权限信息", uname);
			}
		}
		long count = permissionService.countArticleRolePermissions(user, query);

		String msg = "条件统计文章角色权限信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setArticleRolePermissionService(
			ArticleRolePermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public ArticleRolePermissionService getArticleRolePermissionService() {
		return permissionService;
	}

}
