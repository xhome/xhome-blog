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
import org.xhome.util.StringUtils;
import org.xhome.web.action.AbstractAction;
import org.xhome.web.response.CommonResult;
import org.xhome.web.response.DataResult;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
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
public class CategoryAction extends AbstractAction {

	@Autowired
	private CategoryService categoryService;

	public final static String RM_CATEGORY_ADD = "xblog/category/add";
	public final static String RM_CATEGORY_UPDATE = "xblog/category/update";
	public final static String RM_CATEGORY_LOCK = "xblog/category/lock";
	public final static String RM_CATEGORY_UNLOCK = "xblog/category/unlock";
	public final static String RM_CATEGORY_DELETE = "xblog/category/delete";

	public final static String RM_CATEGORY_EXISTS = "xblog/category/exists";
	public final static String RM_CATEGORY_UPDATEABLE = "xblog/category/updateable";
	public final static String RM_CATEGORY_LOCKED = "xblog/category/locked";
	public final static String RM_CATEGORY_DELETEABLE = "xblog/category/deleteable";
	public final static String RM_CATEGORY_GET = "xblog/category/get";
	public final static String RM_CATEGORY_QUERY = "xblog/category/query";
	public final static String RM_CATEGORY_COUNT = "xblog/category/count";

	@RequestMapping(value = RM_CATEGORY_ADD, method = RequestMethod.POST)
	public Object addCategory(
			@Validated @RequestAttribute("category") Category category,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, category);
		AuthUtils.setModifier(request, category);
		status = (short) categoryService.addCategory(user, category);
		if (status == Status.SUCCESS) {
			msg = "添加分类" + category.getName() + "成功";
		} else {
			msg = "添加分类" + category.getName() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, category);
	}

	@RequestMapping(value = RM_CATEGORY_UPDATE, method = RequestMethod.POST)
	public Object updateCategory(
			@Validated @RequestAttribute("category") Category category,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, category);
		status = (short) categoryService.updateCategory(user, category);
		if (status == Status.SUCCESS) {
			msg = "更新分类[" + category.getId() + "]" + category.getName() + "成功";
		} else {
			msg = "更新分类[" + category.getId() + "]" + category.getName() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, category);
	}

	// @RequestMapping(value = RM_CATEGORY_LOCK, method = RequestMethod.POST)
	public Object lockCategory(
			@Validated @RequestAttribute("category") Category category,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, category);
		status = (short) categoryService.lockCategory(user, category);
		if (status == Status.SUCCESS) {
			msg = "锁定分类[" + category.getId() + "]" + category.getName() + "成功";
		} else {
			msg = "锁定分类[" + category.getId() + "]" + category.getName() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, category);
	}

	// @RequestMapping(value = RM_CATEGORY_UNLOCK, method = RequestMethod.POST)
	public Object unlockCategory(
			@Validated @RequestAttribute("category") Category category,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, category);
		status = (short) categoryService.unlockCategory(user, category);
		if (status == Status.SUCCESS) {
			msg = "解锁分类[" + category.getId() + "]" + category.getName() + "成功";
		} else {
			msg = "解锁分类[" + category.getId() + "]" + category.getName() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, category);
	}

	@RequestMapping(value = RM_CATEGORY_DELETE, method = RequestMethod.POST)
	public Object deleteCategory(
			@Validated @RequestAttribute("categories") List<Category> categories,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (Category category : categories) {
			AuthUtils.setModifier(request, category);
		}
		try {
			status = (short) categoryService.deleteCategories(user, categories);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "删除分类成功";
		} else {
			msg = "删除分类失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, categories);
	}

	// @RequestMapping(value = RM_CATEGORY_EXISTS, method = RequestMethod.GET)
	public Object isCategoryExists(
			@Validated @RequestAttribute("category") Category category,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean exists = categoryService.isCategoryExists(user, category);
		if (exists) {
			msg = "查询分类" + category.getName() + "存在";
		} else {
			msg = "查询分类" + category.getName() + "不存在";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, exists);
	}

	// @RequestMapping(value = RM_CATEGORY_UPDATEABLE, method =
	// RequestMethod.GET)
	public Object isCategoryUpdateable(
			@Validated @RequestAttribute("category") Category category,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = categoryService.isCategoryUpdateable(user,
				category);
		if (updateable) {
			msg = "查询到分类[" + category.getId() + "]" + category.getName()
					+ "可以更新";
		} else {
			msg = "查询到分类[" + category.getId() + "]" + category.getName()
					+ "不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	// @RequestMapping(value = RM_CATEGORY_LOCKED, method = RequestMethod.GET)
	public Object isCategoryLocked(
			@Validated @RequestAttribute("category") Category category,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = categoryService.isCategoryLocked(user, category);
		if (locked) {
			msg = "查询到分类[" + category.getId() + "]" + category.getName()
					+ "已被锁定";
		} else {
			msg = "查询到分类[" + category.getId() + "]" + category.getName()
					+ "未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	// @RequestMapping(value = RM_CATEGORY_DELETEABLE, method =
	// RequestMethod.GET)
	public Object isCategoryDeleteable(
			@Validated @RequestAttribute("category") Category category,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = categoryService.isCategoryDeleteable(user,
				category);
		if (deleteable) {
			msg = "查询到分类[" + category.getId() + "]" + category.getName()
					+ "可以删除";
		} else {
			msg = "查询到分类[" + category.getId() + "]" + category.getName()
					+ "不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	// @RequestMapping(value = RM_CATEGORY_GET, method = RequestMethod.GET)
	public Object getCategory(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "name", required = false) String name,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		Category category = null;
		if (id != null) {
			logger.info("用户{}按ID[{}]查询分类", uname, id);
			category = categoryService.getCategory(user, id);
		} else if (StringUtils.isNotEmpty(name)) {
			logger.info("用户{}按名称[{}]查询分类", uname, name);
			category = categoryService.getCategory(user, name);
		}

		String msg = null;
		short status = Status.SUCCESS;

		if (category != null) {
			msg = "分类[" + category.getId() + "]" + category.getName() + "查询成功";
		} else {
			status = Status.ERROR;
			msg = "分类查询失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, category);
	}

	@RequestMapping(value = RM_CATEGORY_QUERY, method = RequestMethod.GET)
	public Object getCategorys(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (query == null) {
			query = new QueryBase();
		}
		if (logger.isInfoEnabled()) {
			logger.info("用户{}按条件{}查询分类信息", uname, query.getParameters());
		}
		categoryService.getCategorys(user, query);

		String msg = "条件查询分类信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new DataResult(status, msg, query);
	}

	// @RequestMapping(value = RM_CATEGORY_COUNT, method = RequestMethod.GET)
	public Object countCategorys(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (query == null) {
			query = new QueryBase();
		}
		if (logger.isInfoEnabled()) {
			logger.info("用户{}按条件{}统计分类信息", uname, query.getParameters());
		}
		long count = categoryService.countCategorys(user, query);

		String msg = "条件统计分类信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

}
