package org.xhome.xblog.core.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.ManageLogService;
import org.xhome.xblog.Category;
import org.xhome.xblog.ManageLogType;
import org.xhome.xblog.core.dao.CategoryDAO;
import org.xhome.xblog.core.listener.CategoryManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:33 PM
 * @describe
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<CategoryManageListener> categoryManageListeners;

	private Logger logger;

	public CategoryServiceImpl() {
		logger = LoggerFactory.getLogger(CategoryService.class);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addCategory(User oper, Category category) {
		String name = category.getName();

		if (!this.beforeCategoryManage(oper, Action.ADD, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add category {}, but it's blocked", name);
			}

			this.logManage(name, Action.ADD, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.ADD, Status.BLOCKED, category);
			return Status.BLOCKED;
		}

		if (categoryDAO.isCategoryExists(category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add category {}, but it's already exists",
						name);
			}

			this.logManage(name, Action.ADD, null, Status.EXISTS, oper);
			this.afterCategoryManage(oper, Action.ADD, Status.EXISTS, category);
			return Status.EXISTS;
		}

		category.setStatus(Status.OK);
		category.setVersion((short) 0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		category.setCreated(t);
		category.setModified(t);

		short r = categoryDAO.addCategory(category) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add category {}", name);
			} else {
				logger.debug("fail to add category {}", name);
			}
		}

		this.logManage(name, Action.ADD, category.getId(), r, oper);
		this.afterCategoryManage(oper, Action.ADD, r, category);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateCategory(User oper, Category category) {
		String name = category.getName();
		Long id = category.getId();

		if (!this.beforeCategoryManage(oper, Action.UPDATE, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update category {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.UPDATE, Status.BLOCKED,
					category);
			return Status.BLOCKED;
		}

		Category old = categoryDAO.queryCategoryById(id);

		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update category {}[{}], but it's not exists",
						name, id);
			}

			this.logManage(name, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterCategoryManage(oper, Action.UPDATE, Status.NOT_EXISTS,
					category);
			return Status.NOT_EXISTS;
		}

		String oldName = old.getName();

		if (!old.getVersion().equals(category.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update category {}[{}], but version not match",
						oldName, id);
			}

			this.logManage(oldName, Action.UPDATE, id,
					Status.VERSION_NOT_MATCH, oper);
			this.afterCategoryManage(oper, Action.UPDATE,
					Status.VERSION_NOT_MATCH, category);
			return Status.VERSION_NOT_MATCH;
		}

		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update category {}[{}]",
						oldName, id);
			}

			this.logManage(oldName, Action.UPDATE, id, status, oper);
			this.afterCategoryManage(oper, Action.UPDATE, Status.EXISTS,
					category);
			return status;
		}

		if (!oldName.equals(name) && categoryDAO.isCategoryExists(category)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update category {}[{}] to {}, but it's exists",
						oldName, id, name);
			}
			this.logManage(oldName, Action.UPDATE, id, Status.EXISTS, oper);
			this.afterCategoryManage(oper, Action.UPDATE, Status.EXISTS,
					category);
			return Status.EXISTS;
		}

		category.setOwner(old.getOwner());
		category.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		category.setModified(t);

		short r = categoryDAO.updateCategory(category) == 1 ? Status.SUCCESS
				: Status.ERROR;
		if (r == Status.SUCCESS) {
			category.incrementVersion();
		}

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update category {}[{}]", oldName, id);
			} else {
				logger.debug("fail to update category {}[{}]", oldName, id);
			}
		}

		this.logManage(oldName, Action.UPDATE, id, r, oper);
		this.afterCategoryManage(oper, Action.UPDATE, r, category);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockCategory(User oper, Category category) {
		String name = category.getName();
		Long id = category.getId();

		if (!this.beforeCategoryManage(oper, Action.LOCK, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock category {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.LOCK, Status.BLOCKED,
					category);
			return Status.BLOCKED;
		}

		short r = categoryDAO.lockCategory(category) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock category {}[{}]", name, id);
			} else {
				logger.debug("fail to lock category {}[{}]", name, id);
			}
		}

		this.logManage(name, Action.LOCK, id, r, oper);
		this.afterCategoryManage(oper, Action.LOCK, r, category);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockCategory(User oper, Category category) {
		String name = category.getName();
		Long id = category.getId();

		if (!this.beforeCategoryManage(oper, Action.UNLOCK, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to unlock category {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.UNLOCK, Status.BLOCKED,
					category);
			return Status.BLOCKED;
		}

		short r = categoryDAO.unlockCategory(category) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock category {}[{}]", name, id);
			} else {
				logger.debug("fail to unlock category {}[{}]", name, id);
			}
		}

		this.logManage(name, Action.UNLOCK, id, r, oper);
		this.afterCategoryManage(oper, Action.UNLOCK, r, category);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeCategory(User oper, Category category) {
		String name = category.getName();
		Long id = category.getId();

		if (!this.beforeCategoryManage(oper, Action.REMOVE, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove category {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.REMOVE, Status.BLOCKED,
					category);
			return Status.BLOCKED;
		}

		short r = Status.SUCCESS;
		if (categoryDAO.isCategoryRemoveable(category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove category {}[{}]", name, id);
			}
			categoryDAO.removeCategory(category);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("category {}[{}] isn't removeable", name, id);
			}
			r = Status.NO_REMOVE;
		}

		this.logManage(name, Action.REMOVE, id, r, oper);
		this.afterCategoryManage(oper, Action.REMOVE, r, category);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeCategories(User oper, List<Category> categories) {
		int r = Status.SUCCESS;
		for (Category category : categories) {
			r = this.removeCategory(oper, category);
			if (r != Status.SUCCESS) {
				throw new RuntimeException("fail to remove Category ["
						+ category.getId() + "]");
			}
		}
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteCategory(User oper, Category category) {
		String name = category.getName();
		Long id = category.getId();

		if (!this.beforeCategoryManage(oper, Action.DELETE, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete category {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.DELETE, Status.BLOCKED,
					category);
			return Status.BLOCKED;
		}

		short r = Status.SUCCESS;
		if (categoryDAO.isCategoryDeleteable(category)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete category {}[{}]", name, id);
			}
			categoryDAO.deleteCategory(category);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("category {}[{}] isn't deleteable", name, id);
			}
			r = Status.NO_DELETE;
		}

		this.logManage(name, Action.DELETE, id, r, oper);
		this.afterCategoryManage(oper, Action.DELETE, r, category);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteCategories(User oper, List<Category> categories) {
		int r = Status.SUCCESS;
		for (Category category : categories) {
			r = this.deleteCategory(oper, category);
			if (r != Status.SUCCESS) {
				throw new RuntimeException("fail to delete Category ["
						+ category.getId() + "]");
			}
		}
		return r;
	}

	@Override
	public boolean isCategoryExists(User oper, Category category) {
		String name = category.getName();

		if (!this.beforeCategoryManage(oper, Action.IS_EXISTS, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge exists of category {}, but it's blocked",
						name);
			}

			this.logManage(name, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.IS_EXISTS, Status.BLOCKED,
					category);
			return false;
		}

		boolean e = categoryDAO.isCategoryExists(category);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of category {}", name);
			} else {
				logger.debug("not exists of category {}", name);
			}
		}

		this.logManage(name, Action.IS_EXISTS, category.getId(),
				Status.SUCCESS, oper);
		this.afterCategoryManage(oper, Action.IS_EXISTS, Status.SUCCESS,
				category);
		return e;
	}

	@Override
	public boolean isCategoryUpdateable(User oper, Category category) {
		String name = category.getName();
		Long id = category.getId();

		if (!this.beforeCategoryManage(oper, Action.IS_UPDATEABLE, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge updateable of category {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.IS_UPDATEABLE, null, Status.BLOCKED,
					oper);
			this.afterCategoryManage(oper, Action.IS_UPDATEABLE,
					Status.BLOCKED, category);
			return false;
		}

		boolean e = categoryDAO.isCategoryUpdateable(category);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("category {}[{}] is updateable", name, id);
			} else {
				logger.debug("category {}[{}] isn't updateable", name, id);
			}
		}

		this.logManage(name, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterCategoryManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS,
				category);
		return e;
	}

	@Override
	public boolean isCategoryLocked(User oper, Category category) {
		String name = category.getName();
		Long id = category.getId();

		if (!this.beforeCategoryManage(oper, Action.IS_LOCKED, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge locked of category {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.IS_LOCKED, Status.BLOCKED,
					category);
			return false;
		}

		boolean e = categoryDAO.isCategoryLocked(category);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("category {}[{}] is locked", name, id);
			} else {
				logger.debug("category {}[{}] isn't locked", name, id);
			}
		}

		this.logManage(name, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterCategoryManage(oper, Action.IS_LOCKED, Status.SUCCESS,
				category);
		return e;
	}

	@Override
	public boolean isCategoryRemoveable(User oper, Category category) {
		String name = category.getName();
		Long id = category.getId();

		if (!this.beforeCategoryManage(oper, Action.IS_REMOVEABLE, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge removeable of category {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.IS_REMOVEABLE, null, Status.BLOCKED,
					oper);
			this.afterCategoryManage(oper, Action.IS_REMOVEABLE,
					Status.BLOCKED, category);
			return false;
		}

		boolean e = categoryDAO.isCategoryRemoveable(category);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("category {}[{}] is removeable", name, id);
			} else {
				logger.debug("category {}[{}] isn't removeable", name, id);
			}
		}

		this.logManage(name, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterCategoryManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS,
				category);
		return e;
	}

	@Override
	public boolean isCategoryDeleteable(User oper, Category category) {
		String name = category.getName();
		Long id = category.getId();

		if (!this.beforeCategoryManage(oper, Action.IS_DELETEABLE, category)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge deleteable of category {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.IS_DELETEABLE, null, Status.BLOCKED,
					oper);
			this.afterCategoryManage(oper, Action.IS_DELETEABLE,
					Status.BLOCKED, category);
			return false;
		}

		boolean e = categoryDAO.isCategoryDeleteable(category);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("category {}[{}] is deleteable", name, id);
			} else {
				logger.debug("category {}[{}] isn't deleteable", name, id);
			}
		}

		this.logManage(name, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterCategoryManage(oper, Action.IS_DELETEABLE, Status.SUCCESS,
				category);
		return e;
	}

	@Override
	public Category getCategory(User oper, long id) {
		if (!this.beforeCategoryManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get category of id {}, but it's blocked",
						id);
			}

			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}

		Category category = categoryDAO.queryCategoryById(id);

		String name = null;
		if (logger.isDebugEnabled()) {
			if (category != null) {
				name = category.getName();
				logger.debug("get category {}[{}]", name, id);
			} else {
				logger.debug("category of id {} is not exists", id);
			}
		}

		this.logManage(name, Action.GET, id, Status.SUCCESS, oper);
		this.afterCategoryManage(oper, Action.GET, Status.SUCCESS, category, id);
		return category;
	}

	@Override
	public Category getCategory(User oper, String name) {
		if (!this.beforeCategoryManage(oper, Action.GET, null, name)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get category {}, but it's blocked", name);
			}

			this.logManage(name, Action.GET, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.GET, Status.BLOCKED, null,
					name);
			return null;
		}

		Category category = categoryDAO.queryCategoryByName(name);

		Long id = null;
		if (logger.isDebugEnabled()) {
			if (category != null) {
				id = category.getId();
				logger.debug("get category {}[{}]", name, id);
			} else {
				logger.debug("category {} is not exists", name);
			}
		}

		this.logManage(name, Action.GET, id, Status.SUCCESS, oper);
		this.afterCategoryManage(oper, Action.GET, Status.SUCCESS, category,
				name);
		return category;
	}

	@Override
	public List<Category> getCategorys(User oper, QueryBase query) {
		if (!this.beforeCategoryManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query categorys, but it's blocked");
			}

			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.QUERY, Status.BLOCKED, null,
					query);
			return null;
		}

		List<Category> results = categoryDAO.queryCategorys(query);
		if (query != null) {
			query.setResults(results);
			long total = categoryDAO.countCategorys(query);
			query.setTotal(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query categorys with parameters {}",
						query.getParameters());
			} else {
				logger.debug("query categorys");
			}
		}

		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterCategoryManage(oper, Action.QUERY, Status.SUCCESS, null,
				query);
		return results;
	}

	@Override
	public long countCategorys(User oper, QueryBase query) {
		if (!this.beforeCategoryManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count categorys, but it's blocked");
			}

			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterCategoryManage(oper, Action.COUNT, Status.BLOCKED, null,
					query);
			return -1;
		}

		long c = categoryDAO.countCategorys(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count categorys with parameters {} of {}",
						query.getParameters(), c);
			} else {
				logger.debug("count categorys of {}", c);
			}
		}

		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterCategoryManage(oper, Action.COUNT, Status.SUCCESS, null,
				query);
		return c;
	}

	private void logManage(String content, Short action, Long obj,
			Short status, User oper) {
		ManageLog manageLog = new ManageLog(ManageLog.MANAGE_LOG_XBLOG,
				content, action, ManageLogType.CATAGORY, obj,
				oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}

	private boolean beforeCategoryManage(User oper, short action,
			Category category, Object... args) {
		if (categoryManageListeners != null) {
			for (CategoryManageListener listener : categoryManageListeners) {
				if (!listener
						.beforeCategoryManage(oper, action, category, args)) {
					return false;
				}
			}
		}
		return true;
	}

	private void afterCategoryManage(User oper, short action, short result,
			Category category, Object... args) {
		if (categoryManageListeners != null) {
			for (CategoryManageListener listener : categoryManageListeners) {
				listener.afterCategoryManage(oper, action, result, category,
						args);
			}
		}
	}

	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public CategoryDAO getCategoryDAO() {
		return this.categoryDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}

	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setCategoryManageListeners(
			List<CategoryManageListener> categoryManageListeners) {
		this.categoryManageListeners = categoryManageListeners;
	}

	public List<CategoryManageListener> getCategoryManageListeners() {
		return categoryManageListeners;
	}

	public void registerCategoryManageListener(
			CategoryManageListener categoryManageListener) {
		if (categoryManageListeners == null) {
			categoryManageListeners = new ArrayList<CategoryManageListener>();
		}
		categoryManageListeners.add(categoryManageListener);
	}

}
