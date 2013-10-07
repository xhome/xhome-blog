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
import org.xhome.util.StringUtils;
import org.xhome.validator.CommonValidator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.web.util.ValidatorUtils;
import org.xhome.xblog.Category;
import org.xhome.xblog.core.service.CategoryService;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description 
 */
@Controller
public class CategoryAction {

	@Autowired(required = false)
	private CategoryService categoryService;
	private Logger logger = LoggerFactory.getLogger(CategoryAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_CATEGORY_ADD			= "xblog/category/add.do";
	public final static String	RM_CATEGORY_UPDATE		= "xblog/category/update.do";
	public final static String	RM_CATEGORY_LOCK		= "xblog/category/lock.do";
	public final static String	RM_CATEGORY_UNLOCK		= "xblog/category/unlock.do";
	public final static String	RM_CATEGORY_REMOVE		= "xblog/category/remove.do";
	public final static String	RM_CATEGORY_DELETE		= "xblog/category/delete.do";
	
	public final static String	RM_CATEGORY_EXISTS		= "xblog/category/exists.do";
	public final static String	RM_CATEGORY_UPDATEABLE	= "xblog/category/updateable.do";
	public final static String	RM_CATEGORY_LOCKED		= "xblog/category/locked.do";
	public final static String	RM_CATEGORY_REMOVEABLE	= "xblog/category/removeable.do";
	public final static String	RM_CATEGORY_DELETEABLE	= "xblog/category/deleteable.do";
	public final static String	RM_CATEGORY_GET			= "xblog/category/get.do";
	public final static String	RM_CATEGORY_QUERY		= "xblog/category/query.do";
	public final static String	RM_CATEGORY_COUNT		= "xblog/category/count.do";
	
	@InitBinder
	public void initBinder(HttpServletRequest request, WebDataBinder binder) {
		String uri = request.getRequestURI();
		commonValidator.setValidators(validatorMapping.getValidatorByUri(uri));
		binder.setValidator(commonValidator);
		if (logger.isDebugEnabled()) {
			logger.debug("init binder for {}", uri);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_ADD, method = RequestMethod.POST)
	public Object addCategory(@Validated @RequestAttribute("category") Category category, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setOwner(request, category);
			AuthUtils.setModifier(request, category);
			status = (short) categoryService.addCategory(user, category);
			if (status == Status.SUCCESS) {
				msg = "添加栏目" + category.getName() + "成功";
				r = new Result(status, msg, category);
			} else {
				msg = "添加栏目" + category.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_UPDATE, method = RequestMethod.POST)
	public Object updateCategory(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, category);
			status = (short) categoryService.updateCategory(user, category);
			if (status == Status.SUCCESS) {
				msg = "更新栏目[" + category.getId() + "]" + category.getName() + "成功";
				r = new Result(status, msg, category);
			} else {
				msg = "更新栏目[" + category.getId() + "]" + category.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_LOCK, method = RequestMethod.POST)
	public Object lockCategory(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, category);
			status = (short) categoryService.lockCategory(user, category);
			if (status == Status.SUCCESS) {
				msg = "锁定栏目[" + category.getId() + "]" + category.getName() + "成功";
				r = new Result(status, msg, category);
			} else {
				msg = "锁定栏目[" + category.getId() + "]" + category.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_UNLOCK, method = RequestMethod.POST)
	public Object unlockCategory(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, category);
			status = (short) categoryService.unlockCategory(user, category);
			if (status == Status.SUCCESS) {
				msg = "解锁栏目[" + category.getId() + "]" + category.getName() + "成功";
				r = new Result(status, msg, category);
			} else {
				msg = "解锁栏目[" + category.getId() + "]" + category.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_REMOVE, method = RequestMethod.POST)
	public Object removeCategory(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, category);
			status = (short) categoryService.removeCategory(user, category);
			if (status == Status.SUCCESS) {
				msg = "移除栏目[" + category.getId() + "]" + category.getName() + "成功";
				r = new Result(status, msg, category);
			} else {
				msg = "移除栏目[" + category.getId() + "]" + category.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_DELETE, method = RequestMethod.POST)
	public Object deleteCategory(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			status = (short) categoryService.deleteCategory(user, category);
			if (status == Status.SUCCESS) {
				msg = "删除栏目[" + category.getId() + "]" + category.getName() + "成功";
				r = new Result(status, msg, category);
			} else {
				msg = "删除栏目[" + category.getId() + "]" + category.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_EXISTS, method = RequestMethod.GET)
	public Object isCategoryExists(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			boolean is = categoryService.isCategoryExists(user, category);
			if (is) {
				msg = "查询栏目" + category.getName() + "存在";
				r = new Result(status, msg, true);
			} else {
				msg = "查询栏目" + category.getName() + "不存在";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_UPDATEABLE, method = RequestMethod.GET)
	public Object isCategoryUpdateable(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			boolean is = categoryService.isCategoryUpdateable(user, category);
			if (is) {
				msg = "查询到栏目[" + category.getId() + "]" + category.getName() + "可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到栏目[" + category.getId() + "]" + category.getName() + "不可以更新";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_LOCKED, method = RequestMethod.GET)
	public Object isCategoryLocked(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			boolean is = categoryService.isCategoryLocked(user, category);
			if (is) {
				msg = "查询到栏目[" + category.getId() + "]" + category.getName() + "已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到栏目[" + category.getId() + "]" + category.getName() + "未被锁定";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_REMOVEABLE, method = RequestMethod.GET)
	public Object isCategoryRemoveable(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			boolean is = categoryService.isCategoryRemoveable(user, category);
			if (is) {
				msg = "查询到栏目[" + category.getId() + "]" + category.getName() + "可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到栏目[" + category.getId() + "]" + category.getName() + "不可以移除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_DELETEABLE, method = RequestMethod.GET)
	public Object isCategoryDeleteable(@Validated Category category, BindingResult result, HttpServletRequest request) {
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
			boolean is = categoryService.isCategoryDeleteable(user, category);
			if (is) {
				msg = "查询到栏目[" + category.getId() + "]" + category.getName() + "可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到栏目[" + category.getId() + "]" + category.getName() + "不可以删除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_GET, method = RequestMethod.GET)
	public Object getCategory(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name", required = false) String name, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		Category category = null;
		if (id != null) {
			logger.info("用户{}按ID[{}]查询栏目", uname, id);
			category = categoryService.getCategory(user, id);
		} else if (StringUtils.isNotEmpty(name)) {
			logger.info("用户{}按名称[{}]查询栏目", uname, name);
			category = categoryService.getCategory(user, name);
		}
		
		String msg = null;
		short status = Status.SUCCESS;
		
		if (category != null) {
			msg = "栏目[" + category.getId() + "]" + category.getName() + "查询成功";
		} else {
			status = Status.ERROR;
			msg = "栏目查询失败";
		}
		Result r = new Result(status, msg, category);
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_QUERY, method = RequestMethod.GET)
	public Object getCategorys(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}查询栏目信息", uname, query.getParameters());
			} else {
				query = new QueryBase();
				logger.info("用户{}查询栏目信息", uname);
			}
		}
		categoryService.getCategorys(user, query);
		
		String msg = "条件查询栏目信息";
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_CATEGORY_COUNT, method = RequestMethod.GET)
	public Object countCategorys(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户{}按条件{}统计栏目信息", uname, query.getParameters());
			} else {
				logger.info("用户{}统计栏目信息", uname);
			}
		}
		long count = categoryService.countCategorys(user, query);
		
		String msg = "条件统计栏目信息，共" + count;
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}
	
}
