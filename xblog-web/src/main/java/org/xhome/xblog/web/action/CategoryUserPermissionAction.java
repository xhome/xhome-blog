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
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryUserPermission;
import org.xhome.xblog.core.service.CategoryUserPermissionService;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description
 */
@Controller
public class CategoryUserPermissionAction extends AbstractAction {

	@Autowired
	private CategoryUserPermissionService permissionService;

	public final static String RM_CATEGORY_USER_PERMISSION_ADD = "xblog/permission/category/user/add";
	public final static String RM_CATEGORY_USER_PERMISSION_UPDATE = "xblog/permission/category/user/update";
	public final static String RM_CATEGORY_USER_PERMISSION_LOCK = "xblog/permission/category/user/lock";
	public final static String RM_CATEGORY_USER_PERMISSION_UNLOCK = "xblog/permission/category/user/unlock";
	public final static String RM_CATEGORY_USER_PERMISSION_REMOVE = "xblog/permission/category/user/remove";
	public final static String RM_CATEGORY_USER_PERMISSION_DELETE = "xblog/permission/category/user/delete";

	public final static String RM_CATEGORY_USER_PERMISSION_EXISTS = "xblog/permission/category/user/exists";
	public final static String RM_CATEGORY_USER_PERMISSION_UPDATEABLE = "xblog/permission/category/user/updateable";
	public final static String RM_CATEGORY_USER_PERMISSION_LOCKED = "xblog/permission/category/user/locked";
	public final static String RM_CATEGORY_USER_PERMISSION_REMOVEABLE = "xblog/permission/category/user/removeable";
	public final static String RM_CATEGORY_USER_PERMISSION_DELETEABLE = "xblog/permission/category/user/deleteable";
	public final static String RM_CATEGORY_USER_PERMISSION_GET = "xblog/permission/category/user/get";
	public final static String RM_CATEGORY_USER_PERMISSION_QUERY = "xblog/permission/category/user/query";
	public final static String RM_CATEGORY_USER_PERMISSION_COUNT = "xblog/permission/category/user/count";

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_ADD, method = RequestMethod.POST)
	public Object addCategoryUserPermission(
			@Validated @RequestAttribute("permission") CategoryUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, permission);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.addCategoryUserPermission(user,
				permission);
		Category category = permission.getCategory();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]添加用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "成功";
		} else {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]添加用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_UPDATE, method = RequestMethod.POST)
	public Object updateCategoryUserPermission(
			@Validated @RequestAttribute("permission") CategoryUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.updateCategoryUserPermission(user,
				permission);
		Category category = permission.getCategory();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]更新用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]更新用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_LOCK, method = RequestMethod.POST)
	public Object lockCategoryUserPermission(
			@Validated @RequestAttribute("permission") CategoryUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.lockCategoryUserPermission(user,
				permission);
		Category category = permission.getCategory();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]锁定用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]锁定用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_UNLOCK, method = RequestMethod.POST)
	public Object unlockCategoryUserPermission(
			@Validated @RequestAttribute("permission") CategoryUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.unlockCategoryUserPermission(user,
				permission);
		Category category = permission.getCategory();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]解锁用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]解锁用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_REMOVE, method = RequestMethod.POST)
	public Object removeCategoryUserPermission(
			@Validated @RequestAttribute("permissions") List<CategoryUserPermission> permissions,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (CategoryUserPermission permission : permissions) {
			AuthUtils.setModifier(request, permission);
		}
		try {
			status = (short) permissionService.removeCategoryUserPermissions(
					user, permissions);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "用户为分类移除用户权限成功";
		} else {
			msg = "用户为分类移除用户权限失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permissions);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_DELETE, method = RequestMethod.POST)
	public Object deleteCategoryUserPermission(
			@Validated @RequestAttribute("permissions") List<CategoryUserPermission> permissions,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (CategoryUserPermission permission : permissions) {
			AuthUtils.setModifier(request, permission);
		}
		try {
			status = (short) permissionService.deleteCategoryUserPermissions(
					user, permissions);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "用户为分类删除用户权限成功";
		} else {
			msg = "用户为分类删除用户权限失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permissions);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_EXISTS, method = RequestMethod.GET)
	public Object isCategoryUserPermissionExists(
			@Validated @RequestAttribute("permission") CategoryUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean exists = permissionService.isCategoryUserPermissionExists(user,
				permission);
		Category category = permission.getCategory();
		User puser = permission.getUser();
		if (exists) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]存在";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不存在";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, exists);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_UPDATEABLE, method = RequestMethod.GET)
	public Object isCategoryUserPermissionUpdateable(
			@Validated @RequestAttribute("permission") CategoryUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = permissionService
				.isCategoryUserPermissionUpdateable(user, permission);
		Category category = permission.getCategory();
		User puser = permission.getUser();
		if (updateable) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以更新";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_LOCKED, method = RequestMethod.GET)
	public Object isCategoryUserPermissionLocked(
			@Validated @RequestAttribute("permission") CategoryUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = permissionService.isCategoryUserPermissionLocked(user,
				permission);
		Category category = permission.getCategory();
		User puser = permission.getUser();
		if (locked) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]已被锁定";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_REMOVEABLE, method = RequestMethod.GET)
	public Object isCategoryUserPermissionRemoveable(
			@Validated @RequestAttribute("permission") CategoryUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean removeable = permissionService
				.isCategoryUserPermissionRemoveable(user, permission);
		Category category = permission.getCategory();
		User puser = permission.getUser();
		if (removeable) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以移除";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以移除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, removeable);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_DELETEABLE, method = RequestMethod.GET)
	public Object isCategoryUserPermissionDeleteable(
			@Validated @RequestAttribute("permission") CategoryUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = permissionService
				.isCategoryUserPermissionDeleteable(user, permission);
		Category category = permission.getCategory();
		User puser = permission.getUser();
		if (deleteable) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以删除";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_GET, method = RequestMethod.GET)
	public Object getCategoryUserPermission(@RequestParam("id") Long id,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		CategoryUserPermission permission = null;

		String msg = null;
		short status = Status.SUCCESS;

		permission = permissionService.getCategoryUserPermission(user, id);
		if (permission != null) {
			Category category = permission.getCategory();
			User puser = permission.getUser();
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]用户" + puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]";
		} else {
			status = Status.ERROR;
			msg = "分类查询失败";
		}

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_QUERY, method = RequestMethod.GET)
	public Object getCategoryUserPermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}查询分类用户权限信息", uname, query.getParameters());
			} else {
				query = new QueryBase();
				logger.info("用户{}查询分类用户权限信息", uname);
			}
		}
		permissionService.getCategoryUserPermissions(user, query);

		String msg = "条件查询分类用户权限信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new DataResult(status, msg, query);
	}

	@RequestMapping(value = RM_CATEGORY_USER_PERMISSION_COUNT, method = RequestMethod.GET)
	public Object countCategoryUserPermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}统计分类用户权限信息", uname, query.getParameters());
			} else {
				logger.info("用户{}统计分类用户权限信息", uname);
			}
		}
		long count = permissionService
				.countCategoryUserPermissions(user, query);

		String msg = "条件统计分类用户权限信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setCategoryUserPermissionService(
			CategoryUserPermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public CategoryUserPermissionService getCategoryUserPermissionService() {
		return permissionService;
	}

}
