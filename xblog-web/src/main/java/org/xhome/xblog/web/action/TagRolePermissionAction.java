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
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagRolePermission;
import org.xhome.xblog.core.service.TagRolePermissionService;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description
 */
@Controller
public class TagRolePermissionAction extends AbstractAction {

	@Autowired(required = false)
	private TagRolePermissionService permissionService;

	public final static String RM_TAG_ROLE_PERMISSION_ADD = "xblog/permission/tag/role/add";
	public final static String RM_TAG_ROLE_PERMISSION_UPDATE = "xblog/permission/tag/role/update";
	public final static String RM_TAG_ROLE_PERMISSION_LOCK = "xblog/permission/tag/role/lock";
	public final static String RM_TAG_ROLE_PERMISSION_UNLOCK = "xblog/permission/tag/role/unlock";
	public final static String RM_TAG_ROLE_PERMISSION_REMOVE = "xblog/permission/tag/role/remove";
	public final static String RM_TAG_ROLE_PERMISSION_DELETE = "xblog/permission/tag/role/delete";

	public final static String RM_TAG_ROLE_PERMISSION_EXISTS = "xblog/permission/tag/role/exists";
	public final static String RM_TAG_ROLE_PERMISSION_UPDATEABLE = "xblog/permission/tag/role/updateable";
	public final static String RM_TAG_ROLE_PERMISSION_LOCKED = "xblog/permission/tag/role/locked";
	public final static String RM_TAG_ROLE_PERMISSION_REMOVEABLE = "xblog/permission/tag/role/removeable";
	public final static String RM_TAG_ROLE_PERMISSION_DELETEABLE = "xblog/permission/tag/role/deleteable";
	public final static String RM_TAG_ROLE_PERMISSION_GET = "xblog/permission/tag/role/get";
	public final static String RM_TAG_ROLE_PERMISSION_QUERY = "xblog/permission/tag/role/query";
	public final static String RM_TAG_ROLE_PERMISSION_COUNT = "xblog/permission/tag/role/count";

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_ADD, method = RequestMethod.POST)
	public Object addTagRolePermission(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, permission);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.addTagRolePermission(user,
				permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]添加角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]添加角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_UPDATE, method = RequestMethod.POST)
	public Object updateTagRolePermission(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.updateTagRolePermission(user,
				permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]更新角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]更新角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_LOCK, method = RequestMethod.POST)
	public Object lockTagRolePermission(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.lockTagRolePermission(user,
				permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]锁定角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]锁定角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_UNLOCK, method = RequestMethod.POST)
	public Object unlockTagRolePermission(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.unlockTagRolePermission(user,
				permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]解锁角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]解锁角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_REMOVE, method = RequestMethod.POST)
	public Object removeTagRolePermission(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.removeTagRolePermission(user,
				permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]移除角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]移除角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_DELETE, method = RequestMethod.POST)
	public Object deleteTagRolePermission(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		status = (short) permissionService.deleteTagRolePermission(user,
				permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]删除角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]删除角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_EXISTS, method = RequestMethod.GET)
	public Object isTagRolePermissionExists(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean exists = permissionService.isTagRolePermissionExists(user,
				permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (exists) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]存在";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不存在";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, exists);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_UPDATEABLE, method = RequestMethod.GET)
	public Object isTagRolePermissionUpdateable(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = permissionService.isTagRolePermissionUpdateable(
				user, permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (updateable) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以更新";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_LOCKED, method = RequestMethod.GET)
	public Object isTagRolePermissionLocked(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = permissionService.isTagRolePermissionLocked(user,
				permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (locked) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]已被锁定";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_REMOVEABLE, method = RequestMethod.GET)
	public Object isTagRolePermissionRemoveable(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean removeable = permissionService.isTagRolePermissionRemoveable(
				user, permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (removeable) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以移除";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以移除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, removeable);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_DELETEABLE, method = RequestMethod.GET)
	public Object isTagRolePermissionDeleteable(
			@Validated @RequestAttribute("permission") TagRolePermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = permissionService.isTagRolePermissionDeleteable(
				user, permission);
		Tag tag = permission.getTag();
		Role role = permission.getRole();
		if (deleteable) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以删除";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_GET, method = RequestMethod.GET)
	public Object getTagRolePermission(@RequestParam("id") Long id,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		TagRolePermission permission = null;

		String msg = null;
		short status = Status.SUCCESS;

		permission = permissionService.getTagRolePermission(user, id);
		if (permission != null) {
			Tag tag = permission.getTag();
			Role role = permission.getRole();
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]";
		} else {
			status = Status.ERROR;
			msg = "标签查询失败";
		}

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_QUERY, method = RequestMethod.GET)
	public Object getTagRolePermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}查询标签角色权限信息", uname, query.getParameters());
			} else {
				query = new QueryBase();
				logger.info("用户{}查询标签角色权限信息", uname);
			}
		}
		permissionService.getTagRolePermissions(user, query);

		String msg = "条件查询标签角色权限信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, query);
	}

	@RequestMapping(value = RM_TAG_ROLE_PERMISSION_COUNT, method = RequestMethod.GET)
	public Object countTagRolePermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}统计标签角色权限信息", uname, query.getParameters());
			} else {
				logger.info("用户{}统计标签角色权限信息", uname);
			}
		}
		long count = permissionService.countTagRolePermissions(user, query);

		String msg = "条件统计标签角色权限信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setTagRolePermissionService(
			TagRolePermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public TagRolePermissionService getTagRolePermissionService() {
		return permissionService;
	}

}
