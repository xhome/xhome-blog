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
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryRolePermission;
import org.xhome.xblog.core.service.CategoryRolePermissionService;
import org.xhome.xblog.web.util.ValidatorUtils;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description 
 */
@Controller
public class CategoryRolePermissionAction {

	@Autowired(required = false)
	private CategoryRolePermissionService permissionService;
	private Logger logger = LoggerFactory.getLogger(CategoryRolePermissionAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_CATEGORY_ROLE_PERMISSION_ADD			= "xblog/permission/category/role/add.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_UPDATE		= "xblog/permission/category/role/update.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_LOCK		= "xblog/permission/category/role/lock.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_UNLOCK		= "xblog/permission/category/role/unlock.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_REMOVE		= "xblog/permission/category/role/remove.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_DELETE		= "xblog/permission/category/role/delete.do";
	
	public final static String	RM_CATEGORY_ROLE_PERMISSION_EXISTS		= "xblog/permission/category/role/exists.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_UPDATEABLE	= "xblog/permission/category/role/updateable.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_LOCKED		= "xblog/permission/category/role/locked.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_REMOVEABLE	= "xblog/permission/category/role/removeable.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_DELETEABLE	= "xblog/permission/category/role/deleteable.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_GET			= "xblog/permission/category/role/get.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_QUERY		= "xblog/permission/category/role/query.do";
	public final static String	RM_CATEGORY_ROLE_PERMISSION_COUNT		= "xblog/permission/category/role/count.do";
	
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_ADD, method = RequestMethod.POST)
	public Object addCategoryRolePermission(@Validated @RequestAttribute("permission") CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			status = (short) permissionService.addCategoryRolePermission(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (status == Status.SUCCESS) {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]添加角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]添加角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_UPDATE, method = RequestMethod.POST)
	public Object updateCategoryRolePermission(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			status = (short) permissionService.updateCategoryRolePermission(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (status == Status.SUCCESS) {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]更新角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]更新角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_LOCK, method = RequestMethod.POST)
	public Object lockCategoryRolePermission(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			status = (short) permissionService.lockCategoryRolePermission(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (status == Status.SUCCESS) {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]锁定角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, category);
			} else {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]锁定角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_UNLOCK, method = RequestMethod.POST)
	public Object unlockCategoryRolePermission(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			status = (short) permissionService.unlockCategoryRolePermission(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (status == Status.SUCCESS) {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]解锁角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]解锁角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_REMOVE, method = RequestMethod.POST)
	public Object removeCategoryRolePermission(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			status = (short) permissionService.removeCategoryRolePermission(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (status == Status.SUCCESS) {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]移除角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]移除角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_DELETE, method = RequestMethod.POST)
	public Object deleteCategoryRolePermission(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			status = (short) permissionService.deleteCategoryRolePermission(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (status == Status.SUCCESS) {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]删除角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId() + "]" + "成功";
				r = new Result(status, msg, permission);
			} else {
				msg = "用户为分类" + category.getName() + "[" + category.getId()
						+ "]删除角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_EXISTS, method = RequestMethod.GET)
	public Object isCategoryRolePermissionExists(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isCategoryRolePermissionExists(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (is) {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]存在";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_UPDATEABLE, method = RequestMethod.GET)
	public Object isCategoryRolePermissionUpdateable(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isCategoryRolePermissionUpdateable(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (is) {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_LOCKED, method = RequestMethod.GET)
	public Object isCategoryRolePermissionLocked(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isCategoryRolePermissionLocked(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (is) {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_REMOVEABLE, method = RequestMethod.GET)
	public Object isCategoryRolePermissionRemoveable(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isCategoryRolePermissionRemoveable(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (is) {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_DELETEABLE, method = RequestMethod.GET)
	public Object isCategoryRolePermissionDeleteable(@Validated CategoryRolePermission permission, BindingResult result, HttpServletRequest request) {
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
			boolean is = permissionService.isCategoryRolePermissionDeleteable(user, permission);
			Category category = permission.getCategory();
			Role role = permission.getRole();
			if (is) {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
						+ "]权限" + permission.getPermission() + "[" + permission.getId()
						+ "]可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "用户查询到分类" + category.getName() + "[" + category.getId()
						+ "]角色" + role.getName() + "[" + role.getId()
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
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_GET, method = RequestMethod.GET)
	public Object getCategoryRolePermission(@RequestParam("id") Long id, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		CategoryRolePermission permission = null;
		
		String msg = null;
		short status = Status.SUCCESS;
		
		permission = permissionService.getCategoryRolePermission(user, id);
		if (permission != null) {
			Category category = permission.getCategory();
			Role role = permission.getRole();
			msg = "用户查询到分类" + category.getName() + "[" + category.getId() + "]角色"
					+ role.getName() + "[" + role.getId() + "]权限"
					+ permission.getPermission() + "[" + permission.getId() + "]";
		} else {
			status = Status.ERROR;
			msg = "分类查询失败";
		}
		Result r = new Result(status, msg, permission);
		
		if (logger.isInfoEnabled()) {
			
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_QUERY, method = RequestMethod.GET)
	public Object getCategoryRolePermissions(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}查询分类角色权限信息", uname, query.getParameters());
			} else {
				query = new QueryBase();
				logger.info("用户{}查询分类角色权限信息", uname);
			}
		}
		permissionService.getCategoryRolePermissions(user, query);
		
		String msg = "条件查询分类角色权限信息";
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_ROLE_PERMISSION_COUNT, method = RequestMethod.GET)
	public Object countCategoryRolePermissions(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}统计分类角色权限信息", uname, query.getParameters());
			} else {
				logger.info("用户{}统计分类角色权限信息", uname);
			}
		}
		long count = permissionService.countCategoryRolePermissions(user, query);
		
		String msg = "条件统计分类角色权限信息，共" + count;
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}

	public void setCategoryRolePermissionService(CategoryRolePermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public CategoryRolePermissionService getCategoryRolePermissionService() {
		return permissionService;
	}
	
}
