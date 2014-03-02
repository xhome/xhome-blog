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
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryRolePermission;
import org.xhome.xblog.core.service.CategoryRolePermissionService;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description
 */
@Controller
public class CategoryRolePermissionAction extends AbstractAction {

	@Autowired
	private CategoryRolePermissionService permissionService;

	public final static String RM_CATEGORY_ROLE_PERMISSION_ADD = "xblog/permission/category/role/add";
	public final static String RM_CATEGORY_ROLE_PERMISSION_UPDATE = "xblog/permission/category/role/update";
	public final static String RM_CATEGORY_ROLE_PERMISSION_LOCK = "xblog/permission/category/role/lock";
	public final static String RM_CATEGORY_ROLE_PERMISSION_UNLOCK = "xblog/permission/category/role/unlock";
	public final static String RM_CATEGORY_ROLE_PERMISSION_REMOVE = "xblog/permission/category/role/remove";
	public final static String RM_CATEGORY_ROLE_PERMISSION_DELETE = "xblog/permission/category/role/delete";

	public final static String RM_CATEGORY_ROLE_PERMISSION_EXISTS = "xblog/permission/category/role/exists";
	public final static String RM_CATEGORY_ROLE_PERMISSION_UPDATEABLE = "xblog/permission/category/role/updateable";
	public final static String RM_CATEGORY_ROLE_PERMISSION_LOCKED = "xblog/permission/category/role/locked";
	public final static String RM_CATEGORY_ROLE_PERMISSION_REMOVEABLE = "xblog/permission/category/role/removeable";
	public final static String RM_CATEGORY_ROLE_PERMISSION_DELETEABLE = "xblog/permission/category/role/deleteable";
	public final static String RM_CATEGORY_ROLE_PERMISSION_GET = "xblog/permission/category/role/get";
	public final static String RM_CATEGORY_ROLE_PERMISSION_QUERY = "xblog/permission/category/role/query";
	public final static String RM_CATEGORY_ROLE_PERMISSION_COUNT = "xblog/permission/category/role/count";

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_ADD, method = RequestMethod.POST)
	public Object addCategoryRolePermission(
			@Validated @RequestAttribute("permission") CategoryRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, permission);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.addCategoryRolePermission(user,
				permission);
		Category category = permission.getCategory();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]添加角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "成功";
		} else {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]添加角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_UPDATE, method = RequestMethod.POST)
	public Object updateCategoryRolePermission(
			@Validated @RequestAttribute("permission") CategoryRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.updateCategoryRolePermission(user,
				permission);
		Category category = permission.getCategory();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]更新角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]更新角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_LOCK, method = RequestMethod.POST)
	public Object lockCategoryRolePermission(
			@Validated @RequestAttribute("permission") CategoryRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.lockCategoryRolePermission(user,
				permission);
		Category category = permission.getCategory();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]锁定角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]锁定角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_UNLOCK, method = RequestMethod.POST)
	public Object unlockCategoryRolePermission(
			@Validated @RequestAttribute("permission") CategoryRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.unlockCategoryRolePermission(user,
				permission);
		Category category = permission.getCategory();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]解锁角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为分类" + category.getName() + "[" + category.getId()
					+ "]解锁角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_REMOVE, method = RequestMethod.POST)
	public Object removeCategoryRolePermission(
			@Validated @RequestAttribute("permissions") List<CategoryRolePermission> permissions,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (CategoryRolePermission permission : permissions) {
			AuthUtils.setModifier(request, permission);
		}
		try {
			status = (short) permissionService.removeCategoryRolePermissions(
					user, permissions);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "用户为分类移除角色权限成功";
		} else {
			msg = "用户为分类移除角色权限失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permissions);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_DELETE, method = RequestMethod.POST)
	public Object deleteCategoryRolePermission(
			@Validated @RequestAttribute("permissions") List<CategoryRolePermission> permissions,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (CategoryRolePermission permission : permissions) {
			AuthUtils.setModifier(request, permission);
		}
		try {
			status = (short) permissionService.deleteCategoryRolePermissions(
					user, permissions);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "用户为分类删除角色权限成功";
		} else {
			msg = "用户为分类删除角色权限失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permissions);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_EXISTS, method = RequestMethod.GET)
	public Object isCategoryRolePermissionExists(
			@Validated @RequestAttribute("permission") CategoryRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean exists = permissionService.isCategoryRolePermissionExists(user,
				permission);
		Category category = permission.getCategory();
		Role role = permission.getRole();
		if (exists) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]存在";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不存在";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, exists);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_UPDATEABLE, method = RequestMethod.GET)
	public Object isCategoryRolePermissionUpdateable(
			@Validated @RequestAttribute("permission") CategoryRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = permissionService
				.isCategoryRolePermissionUpdateable(user, permission);
		Category category = permission.getCategory();
		Role role = permission.getRole();
		if (updateable) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以更新";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_LOCKED, method = RequestMethod.GET)
	public Object isCategoryRolePermissionLocked(
			@Validated @RequestAttribute("permission") CategoryRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = permissionService.isCategoryRolePermissionLocked(user,
				permission);
		Category category = permission.getCategory();
		Role role = permission.getRole();
		if (locked) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]已被锁定";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_REMOVEABLE, method = RequestMethod.GET)
	public Object isCategoryRolePermissionRemoveable(
			@Validated @RequestAttribute("permission") CategoryRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean removeable = permissionService
				.isCategoryRolePermissionRemoveable(user, permission);
		Category category = permission.getCategory();
		Role role = permission.getRole();
		if (removeable) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以移除";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以移除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, removeable);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_DELETEABLE, method = RequestMethod.GET)
	public Object isCategoryRolePermissionDeleteable(
			@Validated @RequestAttribute("permission") CategoryRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = permissionService
				.isCategoryRolePermissionDeleteable(user, permission);
		Category category = permission.getCategory();
		Role role = permission.getRole();
		if (deleteable) {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以删除";
		} else {
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_GET, method = RequestMethod.GET)
	public Object getCategoryRolePermission(@RequestParam("id") Long id,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		CategoryRolePermission permission = null;

		String msg = null;
		short status = Status.SUCCESS;

		permission = permissionService.getCategoryRolePermission(user, id);
		if (permission != null) {
			Category category = permission.getCategory();
			Role role = permission.getRole();
			msg = "用户查询到分类" + category.getName() + "[" + category.getId()
					+ "]角色" + role.getName() + "[" + role.getId() + "]权限"
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

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_QUERY, method = RequestMethod.GET)
	public Object getCategoryRolePermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (query == null) {
			query = new QueryBase();
		}
		if (logger.isInfoEnabled()) {
			logger.info("用户{}按条件{}查询分类角色权限信息", uname, query.getParameters());
		}
		permissionService.getCategoryRolePermissions(user, query);

		String msg = "条件查询分类角色权限信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new DataResult(status, msg, query);
	}

	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_COUNT, method = RequestMethod.GET)
	public Object countCategoryRolePermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (query == null) {
			query = new QueryBase();
		}
		if (logger.isInfoEnabled()) {
			logger.info("用户{}按条件{}统计分类角色权限信息", uname, query.getParameters());
		}
		long count = permissionService
				.countCategoryRolePermissions(user, query);

		String msg = "条件统计分类角色权限信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setCategoryRolePermissionService(
			CategoryRolePermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public CategoryRolePermissionService getCategoryRolePermissionService() {
		return permissionService;
	}

}
