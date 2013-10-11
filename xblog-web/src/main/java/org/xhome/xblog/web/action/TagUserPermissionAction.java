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
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagUserPermission;
import org.xhome.xblog.core.service.TagUserPermissionService;
import org.xhome.xblog.web.util.ValidatorUtils;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description 
 */
@Controller
public class TagUserPermissionAction {

	@Autowired(required = false)
	private TagUserPermissionService permissionService;
	private Logger logger = LoggerFactory.getLogger(TagUserPermissionAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_TAG_USER_PERMISSION_ADD			= "xblog/permission/tag/user/add.do";
	public final static String	RM_TAG_USER_PERMISSION_UPDATE		= "xblog/permission/tag/user/update.do";
	public final static String	RM_TAG_USER_PERMISSION_LOCK			= "xblog/permission/tag/user/lock.do";
	public final static String	RM_TAG_USER_PERMISSION_UNLOCK		= "xblog/permission/tag/user/unlock.do";
	public final static String	RM_TAG_USER_PERMISSION_REMOVE		= "xblog/permission/tag/user/remove.do";
	public final static String	RM_TAG_USER_PERMISSION_DELETE		= "xblog/permission/tag/user/delete.do";
	
	public final static String	RM_TAG_USER_PERMISSION_EXISTS		= "xblog/permission/tag/user/exists.do";
	public final static String	RM_TAG_USER_PERMISSION_UPDATEABLE	= "xblog/permission/tag/user/updateable.do";
	public final static String	RM_TAG_USER_PERMISSION_LOCKED		= "xblog/permission/tag/user/locked.do";
	public final static String	RM_TAG_USER_PERMISSION_REMOVEABLE	= "xblog/permission/tag/user/removeable.do";
	public final static String	RM_TAG_USER_PERMISSION_DELETEABLE	= "xblog/permission/tag/user/deleteable.do";
	public final static String	RM_TAG_USER_PERMISSION_GET			= "xblog/permission/tag/user/get.do";
	public final static String	RM_TAG_USER_PERMISSION_QUERY		= "xblog/permission/tag/user/query.do";
	public final static String	RM_TAG_USER_PERMISSION_COUNT		= "xblog/permission/tag/user/count.do";
	
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
	@RequestMapping(value = RM_TAG_USER_PERMISSION_ADD, method = RequestMethod.POST)
	public Object addTagUserPermission(@Validated @RequestAttribute("permission") TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setOwner(request, permission);
			AuthUtils.setModifier(request, permission);
			status = (short) permissionService.addTagUserPermission(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (status == Status.SUCCESS) {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]添加用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]添加用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_UPDATE, method = RequestMethod.POST)
	public Object updateTagUserPermission(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, permission);
			status = (short) permissionService.updateTagUserPermission(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (status == Status.SUCCESS) {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]更新用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]更新用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_LOCK, method = RequestMethod.POST)
	public Object lockTagUserPermission(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, permission);
			status = (short) permissionService.lockTagUserPermission(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (status == Status.SUCCESS) {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]锁定用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, tag);
			} else {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]锁定用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_UNLOCK, method = RequestMethod.POST)
	public Object unlockTagUserPermission(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, permission);
			status = (short) permissionService.unlockTagUserPermission(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (status == Status.SUCCESS) {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]解锁用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]解锁用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_REMOVE, method = RequestMethod.POST)
	public Object removeTagUserPermission(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, permission);
			status = (short) permissionService.removeTagUserPermission(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (status == Status.SUCCESS) {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]移除用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]移除用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_DELETE, method = RequestMethod.POST)
	public Object deleteTagUserPermission(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			status = (short) permissionService.deleteTagUserPermission(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (status == Status.SUCCESS) {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]删除用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为标签" + tag.getName() + "[" + tag.getId()
						+ "]删除用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_EXISTS, method = RequestMethod.GET)
	public Object isTagUserPermissionExists(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isTagUserPermissionExists(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (is) {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]存在";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]不存在";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_UPDATEABLE, method = RequestMethod.GET)
	public Object isTagUserPermissionUpdateable(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isTagUserPermissionUpdateable(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (is) {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]不可以更新";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_LOCKED, method = RequestMethod.GET)
	public Object isTagUserPermissionLocked(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isTagUserPermissionLocked(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (is) {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]未被锁定";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_REMOVEABLE, method = RequestMethod.GET)
	public Object isTagUserPermissionRemoveable(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isTagUserPermissionRemoveable(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (is) {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]不可以移除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_DELETEABLE, method = RequestMethod.GET)
	public Object isTagUserPermissionDeleteable(@Validated TagUserPermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isTagUserPermissionDeleteable(user, permission);
			Tag tag = permission.getTag();
			User puser = permission.getUser();
			if (is) {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到标签" + tag.getName() + "[" + tag.getId()
						+ "]用户" + puser.getName() + "[" + puser.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]不可以删除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_GET, method = RequestMethod.GET)
	public Object getTagUserPermission(@RequestParam("id") Long id, HttpServletRequest request) {
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
					+ permission.getPermission() + "[" + permission.getId() + "]";
		} else {
			status = Status.ERROR;
			msg = "标签查询失败";
		}
		Result r = new Result(status, msg, permission);
		
		if (logger.isInfoEnabled()) {
			
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_QUERY, method = RequestMethod.GET)
	public Object getTagUserPermissions(QueryBase query, HttpServletRequest request) {
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
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_USER_PERMISSION_COUNT, method = RequestMethod.GET)
	public Object countTagUserPermissions(QueryBase query, HttpServletRequest request) {
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
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}

	public void setTagUserPermissionService(TagUserPermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public TagUserPermissionService getTagUserPermissionService() {
		return permissionService;
	}
	
}
