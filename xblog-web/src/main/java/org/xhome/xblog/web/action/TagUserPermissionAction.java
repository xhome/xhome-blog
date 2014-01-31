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
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagUserPermission;
import org.xhome.xblog.core.service.TagUserPermissionService;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description
 */
@Controller
public class TagUserPermissionAction extends AbstractAction {

	@Autowired(required = false)
	private TagUserPermissionService permissionService;

	public final static String RM_TAG_USER_PERMISSION_ADD = "xblog/permission/tag/user/add";
	public final static String RM_TAG_USER_PERMISSION_UPDATE = "xblog/permission/tag/user/update";
	public final static String RM_TAG_USER_PERMISSION_LOCK = "xblog/permission/tag/user/lock";
	public final static String RM_TAG_USER_PERMISSION_UNLOCK = "xblog/permission/tag/user/unlock";
	public final static String RM_TAG_USER_PERMISSION_REMOVE = "xblog/permission/tag/user/remove";
	public final static String RM_TAG_USER_PERMISSION_DELETE = "xblog/permission/tag/user/delete";

	public final static String RM_TAG_USER_PERMISSION_EXISTS = "xblog/permission/tag/user/exists";
	public final static String RM_TAG_USER_PERMISSION_UPDATEABLE = "xblog/permission/tag/user/updateable";
	public final static String RM_TAG_USER_PERMISSION_LOCKED = "xblog/permission/tag/user/locked";
	public final static String RM_TAG_USER_PERMISSION_REMOVEABLE = "xblog/permission/tag/user/removeable";
	public final static String RM_TAG_USER_PERMISSION_DELETEABLE = "xblog/permission/tag/user/deleteable";
	public final static String RM_TAG_USER_PERMISSION_GET = "xblog/permission/tag/user/get";
	public final static String RM_TAG_USER_PERMISSION_QUERY = "xblog/permission/tag/user/query";
	public final static String RM_TAG_USER_PERMISSION_COUNT = "xblog/permission/tag/user/count";

	@RequestMapping(value = RM_TAG_USER_PERMISSION_ADD, method = RequestMethod.POST)
	public Object addTagUserPermission(
			@Validated @RequestAttribute("permission") TagUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, permission);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.addTagUserPermission(user,
				permission);
		Tag tag = permission.getTag();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]添加用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]添加用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_UPDATE, method = RequestMethod.POST)
	public Object updateTagUserPermission(
			@Validated @RequestAttribute("permission") TagUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.updateTagUserPermission(user,
				permission);
		Tag tag = permission.getTag();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]更新用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]更新用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_LOCK, method = RequestMethod.POST)
	public Object lockTagUserPermission(
			@Validated @RequestAttribute("permission") TagUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.lockTagUserPermission(user,
				permission);
		Tag tag = permission.getTag();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]锁定用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]锁定用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_UNLOCK, method = RequestMethod.POST)
	public Object unlockTagUserPermission(
			@Validated @RequestAttribute("permission") TagUserPermission permission,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, permission);
		status = (short) permissionService.unlockTagUserPermission(user,
				permission);
		Tag tag = permission.getTag();
		User puser = permission.getUser();
		if (status == Status.SUCCESS) {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]解锁用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "成功";
		} else {
			msg = "用户为标签" + tag.getName() + "[" + tag.getId() + "]解锁用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]" + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permission);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_REMOVE, method = RequestMethod.POST)
	public Object removeTagUserPermission(
			@Validated @RequestAttribute("permissions") List<TagUserPermission> permissions,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (TagUserPermission permission : permissions) {
			AuthUtils.setModifier(request, permission);
		}
		try {
			status = (short) permissionService.removeTagUserPermissions(user,
					permissions);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "用户为标签移除用户权限成功";
		} else {
			msg = "用户为标签移除用户权限失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permissions);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_DELETE, method = RequestMethod.POST)
	public Object deleteTagUserPermission(
			@Validated @RequestAttribute("permissions") List<TagUserPermission> permissions,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (TagUserPermission permission : permissions) {
			AuthUtils.setModifier(request, permission);
		}
		try {
			status = (short) permissionService.deleteTagUserPermissions(user,
					permissions);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "用户为标签删除用户权限成功";
		} else {
			msg = "用户为标签删除用户权限失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, permissions);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_EXISTS, method = RequestMethod.GET)
	public Object isTagUserPermissionExists(
			@Validated @RequestAttribute("permission") TagUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean exists = permissionService.isTagUserPermissionExists(user,
				permission);
		Tag tag = permission.getTag();
		User puser = permission.getUser();
		if (exists) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]存在";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不存在";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, exists);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_UPDATEABLE, method = RequestMethod.GET)
	public Object isTagUserPermissionUpdateable(
			@Validated @RequestAttribute("permission") TagUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = permissionService.isTagUserPermissionUpdateable(
				user, permission);
		Tag tag = permission.getTag();
		User puser = permission.getUser();
		if (updateable) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以更新";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_LOCKED, method = RequestMethod.GET)
	public Object isTagUserPermissionLocked(
			@Validated @RequestAttribute("permission") TagUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = permissionService.isTagUserPermissionLocked(user,
				permission);
		Tag tag = permission.getTag();
		User puser = permission.getUser();
		if (locked) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]已被锁定";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_REMOVEABLE, method = RequestMethod.GET)
	public Object isTagUserPermissionRemoveable(
			@Validated @RequestAttribute("permission") TagUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean removeable = permissionService.isTagUserPermissionRemoveable(
				user, permission);
		Tag tag = permission.getTag();
		User puser = permission.getUser();
		if (removeable) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以移除";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以移除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, removeable);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_DELETEABLE, method = RequestMethod.GET)
	public Object isTagUserPermissionDeleteable(
			@Validated @RequestAttribute("permission") TagUserPermission permission,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = permissionService.isTagUserPermissionDeleteable(
				user, permission);
		Tag tag = permission.getTag();
		User puser = permission.getUser();
		if (deleteable) {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]可以删除";
		} else {
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId()
					+ "]不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_GET, method = RequestMethod.GET)
	public Object getTagUserPermission(@RequestParam("id") Long id,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		TagUserPermission permission = null;

		String msg = null;
		short status = Status.SUCCESS;

		permission = permissionService.getTagUserPermission(user, id);
		if (permission != null) {
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			msg = "用户查询到标签" + tag.getName() + "[" + tag.getId() + "]用户"
					+ puser.getName() + "[" + puser.getId() + "]权限"
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

	@RequestMapping(value = RM_TAG_USER_PERMISSION_QUERY, method = RequestMethod.GET)
	public Object getTagUserPermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}查询标签用户权限信息", uname, query.getParameters());
			} else {
				query = new QueryBase();
				logger.info("用户{}查询标签用户权限信息", uname);
			}
		}
		permissionService.getTagUserPermissions(user, query);

		String msg = "条件查询标签用户权限信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new DataResult(status, msg, query);
	}

	@RequestMapping(value = RM_TAG_USER_PERMISSION_COUNT, method = RequestMethod.GET)
	public Object countTagUserPermissions(QueryBase query,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}统计标签用户权限信息", uname, query.getParameters());
			} else {
				logger.info("用户{}统计标签用户权限信息", uname);
			}
		}
		long count = permissionService.countTagUserPermissions(user, query);

		String msg = "条件统计标签用户权限信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setTagUserPermissionService(
			TagUserPermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public TagUserPermissionService getTagUserPermissionService() {
		return permissionService;
	}

}
