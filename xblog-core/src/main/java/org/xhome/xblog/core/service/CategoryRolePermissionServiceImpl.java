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
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.ManageLogService;
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryRolePermission;
import org.xhome.xblog.ManageLogType;
import org.xhome.xblog.core.dao.CategoryRolePermissionDAO;
import org.xhome.xblog.core.listener.CategoryRolePermissionManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:33 PM
 * @describe
 */
@Service
public class CategoryRolePermissionServiceImpl implements
		CategoryRolePermissionService {

	@Autowired
	private CategoryRolePermissionDAO categoryRolePermissionDAO;
	@Autowired
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<CategoryRolePermissionManageListener> categoryRolePermissionManageListeners;

	private Logger logger;

	public CategoryRolePermissionServiceImpl() {
		logger = LoggerFactory.getLogger(CategoryRolePermissionService.class);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission) {
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = categoryRolePermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeCategoryRolePermissionManage(oper, Action.ADD,
				categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to add category {}[{}] permission {} for role {}[{}], but it's blocked",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.ADD, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.ADD,
					Status.BLOCKED, categoryRolePermission);
			return Status.BLOCKED;
		}

		if (categoryRolePermissionDAO
				.isCategoryRolePermissionExists(categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to add category {}[{}] permission {} for role {}[{}], but it's already exists",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.ADD, null, Status.EXISTS, oper);
			this.afterCategoryRolePermissionManage(oper, Action.ADD,
					Status.EXISTS, categoryRolePermission);
			return Status.EXISTS;
		}

		categoryRolePermission.setStatus(Status.OK);
		categoryRolePermission.setVersion((short) 0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		categoryRolePermission.setCreated(t);
		categoryRolePermission.setModified(t);

		short r = categoryRolePermissionDAO
				.addCategoryRolePermission(categoryRolePermission) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to add category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			} else {
				logger.debug(
						"fail to add category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.ADD, categoryRolePermission.getId(), r,
				oper);
		this.afterCategoryRolePermissionManage(oper, Action.ADD, r,
				categoryRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission) {
		long id = categoryRolePermission.getId();
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = categoryRolePermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeCategoryRolePermissionManage(oper, Action.UPDATE,
				categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update category {}[{}] permission {} for role {}[{}], but it's blocked",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.UPDATE,
					Status.BLOCKED, categoryRolePermission);
			return Status.BLOCKED;
		}

		CategoryRolePermission old = categoryRolePermissionDAO
				.queryCategoryRolePermission(id);

		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update category {}[{}] permission {} for role {}[{}], but it's not exists",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterCategoryRolePermissionManage(oper, Action.UPDATE,
					Status.NOT_EXISTS, categoryRolePermission);
			return Status.NOT_EXISTS;
		}
		categoryRolePermission.setOwner(old.getOwner());
		categoryRolePermission.setCreated(old.getCreated());

		if (!old.getVersion().equals(categoryRolePermission.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update category {}[{}] permission {} for role {}[{}], but version not match",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UPDATE, id, Status.VERSION_NOT_MATCH,
					oper);
			this.afterCategoryRolePermissionManage(oper, Action.UPDATE,
					Status.VERSION_NOT_MATCH, categoryRolePermission);
			return Status.VERSION_NOT_MATCH;
		}

		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"it's not allowed to update category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UPDATE, id, status, oper);
			this.afterCategoryRolePermissionManage(oper, Action.UPDATE,
					Status.EXISTS, categoryRolePermission);
			return status;
		}

		Timestamp t = new Timestamp(System.currentTimeMillis());
		categoryRolePermission.setModified(t);

		short r = categoryRolePermissionDAO
				.updateCategoryRolePermission(categoryRolePermission) == 1 ? Status.SUCCESS
				: Status.ERROR;
		if (r == Status.SUCCESS) {
			categoryRolePermission.incrementVersion();
		}

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to update category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			} else {
				logger.debug(
						"fail to update category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.UPDATE, id, r, oper);
		this.afterCategoryRolePermissionManage(oper, Action.UPDATE, r,
				categoryRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission) {
		long id = categoryRolePermission.getId();
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = categoryRolePermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeCategoryRolePermissionManage(oper, Action.LOCK,
				categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to lock category {}[{}] permission {} for role {}[{}], but it's blocked",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.LOCK,
					Status.BLOCKED, categoryRolePermission);
			return Status.BLOCKED;
		}

		short r = categoryRolePermissionDAO
				.lockCategoryRolePermission(categoryRolePermission) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to lock category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			} else {
				logger.debug(
						"fail to lock category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.LOCK, id, r, oper);
		this.afterCategoryRolePermissionManage(oper, Action.LOCK, r,
				categoryRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission) {
		long id = categoryRolePermission.getId();
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = categoryRolePermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeCategoryRolePermissionManage(oper, Action.UNLOCK,
				categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to unlock category {}[{}] permission {} for role {}[{}], but it's blocked",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.UNLOCK,
					Status.BLOCKED, categoryRolePermission);
			return Status.BLOCKED;
		}

		short r = categoryRolePermissionDAO
				.unlockCategoryRolePermission(categoryRolePermission) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to unlock category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			} else {
				logger.debug(
						"fail to unlock category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.UNLOCK, id, r, oper);
		this.afterCategoryRolePermissionManage(oper, Action.UNLOCK, r,
				categoryRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission) {
		long id = categoryRolePermission.getId();
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = categoryRolePermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeCategoryRolePermissionManage(oper, Action.DELETE,
				categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to delete category {}[{}] permission {} for role {}[{}], but it's blocked",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.DELETE,
					Status.BLOCKED, categoryRolePermission);
			return Status.BLOCKED;
		}

		short r = Status.SUCCESS;
		if (categoryRolePermissionDAO
				.isCategoryRolePermissionDeleteable(categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"delete category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			}
			categoryRolePermissionDAO
					.deleteCategoryRolePermission(categoryRolePermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"category {}[{}] permission {} for role {}[{}] isn't deleteable",
						categoryName, categoryId, permission, roleName, roleId);
			}
			r = Status.NO_DELETE;
		}

		this.logManage(mstr, Action.DELETE, id, r, oper);
		this.afterCategoryRolePermissionManage(oper, Action.DELETE, r,
				categoryRolePermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteCategoryRolePermissions(User oper,
			List<CategoryRolePermission> categoryRolePermissions) {
		int r = Status.SUCCESS;
		for (CategoryRolePermission categoryRolePermission : categoryRolePermissions) {
			r = this.deleteCategoryRolePermission(oper, categoryRolePermission);
			if (r != Status.SUCCESS) {
				throw new RuntimeException(
						"fail to delete CategoryRolePermission ["
								+ categoryRolePermission.getId() + "]");
			}
		}
		return r;
	}

	@Override
	public boolean isCategoryRolePermissionExists(User oper,
			CategoryRolePermission categoryRolePermission) {
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = categoryRolePermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeCategoryRolePermissionManage(oper, Action.IS_EXISTS,
				categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge exists of category {}[{}] permission {} for role {}[{}], but it's blocked",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.IS_EXISTS,
					Status.BLOCKED, categoryRolePermission);
			return false;
		}

		boolean e = categoryRolePermissionDAO
				.isCategoryRolePermissionExists(categoryRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"exists of category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			} else {
				logger.debug(
						"not exists of category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_EXISTS, categoryRolePermission.getId(),
				Status.SUCCESS, oper);
		this.afterCategoryRolePermissionManage(oper, Action.IS_EXISTS,
				Status.SUCCESS, categoryRolePermission);
		return e;
	}

	@Override
	public boolean isCategoryRolePermissionUpdateable(User oper,
			CategoryRolePermission categoryRolePermission) {
		long id = categoryRolePermission.getId();
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = categoryRolePermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeCategoryRolePermissionManage(oper,
				Action.IS_UPDATEABLE, categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge updateable of category {}[{}] permission {} for role {}[{}], but it's blocked",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.IS_UPDATEABLE, null, Status.BLOCKED,
					oper);
			this.afterCategoryRolePermissionManage(oper, Action.IS_UPDATEABLE,
					Status.BLOCKED, categoryRolePermission);
			return false;
		}

		boolean e = categoryRolePermissionDAO
				.isCategoryRolePermissionUpdateable(categoryRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"category {}[{}] permission {} for role {}[{}] is updateable",
						categoryName, categoryId, permission, roleName, roleId);
			} else {
				logger.debug(
						"category {}[{}] permission {} for role {}[{}] isn't updateable",
						categoryName, categoryId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterCategoryRolePermissionManage(oper, Action.IS_UPDATEABLE,
				Status.SUCCESS, categoryRolePermission);
		return e;
	}

	@Override
	public boolean isCategoryRolePermissionLocked(User oper,
			CategoryRolePermission categoryRolePermission) {
		long id = categoryRolePermission.getId();
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = categoryRolePermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeCategoryRolePermissionManage(oper, Action.IS_LOCKED,
				categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge locked of category {}[{}] permission {} for role {}[{}], but it's blocked",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.IS_LOCKED,
					Status.BLOCKED, categoryRolePermission);
			return false;
		}

		boolean e = categoryRolePermissionDAO
				.isCategoryRolePermissionLocked(categoryRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"category {}[{}] permission {} for role {}[{}] is locked",
						categoryName, categoryId, permission, roleName, roleId);
			} else {
				logger.debug(
						"category {}[{}] permission {} for role {}[{}] isn't locked",
						categoryName, categoryId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterCategoryRolePermissionManage(oper, Action.IS_LOCKED,
				Status.SUCCESS, categoryRolePermission);
		return e;
	}

	@Override
	public boolean isCategoryRolePermissionDeleteable(User oper,
			CategoryRolePermission categoryRolePermission) {
		long id = categoryRolePermission.getId();
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", roleName = role != null ? role
				.getName() : null;
		;
		int permission = categoryRolePermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
				+ "(" + roleId + ")";

		if (!this.beforeCategoryRolePermissionManage(oper,
				Action.IS_DELETEABLE, categoryRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge deleteable of category {}[{}] permission {} for role {}[{}], but it's blocked",
						categoryName, categoryId, permission, roleName, roleId);
			}

			this.logManage(mstr, Action.IS_DELETEABLE, null, Status.BLOCKED,
					oper);
			this.afterCategoryRolePermissionManage(oper, Action.IS_DELETEABLE,
					Status.BLOCKED, categoryRolePermission);
			return false;
		}

		boolean e = categoryRolePermissionDAO
				.isCategoryRolePermissionDeleteable(categoryRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"category {}[{}] permission {} for role {}[{}] is deleteable",
						categoryName, categoryId, permission, roleName, roleId);
			} else {
				logger.debug(
						"category {}[{}] permission {} for role {}[{}] isn't deleteable",
						categoryName, categoryId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterCategoryRolePermissionManage(oper, Action.IS_DELETEABLE,
				Status.SUCCESS, categoryRolePermission);
		return e;
	}

	@Override
	public CategoryRolePermission getCategoryRolePermission(User oper, long id) {
		if (!this
				.beforeCategoryRolePermissionManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to get category role permission of id {}, but it's blocked",
						id);
			}

			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.GET,
					Status.BLOCKED, null, id);
			return null;
		}

		CategoryRolePermission categoryRolePermission = categoryRolePermissionDAO
				.queryCategoryRolePermission(id);

		String mstr = null;
		if (logger.isDebugEnabled()) {
			if (categoryRolePermission != null) {
				Category category = categoryRolePermission.getCategory();
				Role role = categoryRolePermission.getRole();
				Long categoryId = category != null ? category.getId() : null, roleId = role != null ? role
						.getId() : null;
				String categoryName = category != null ? category.getName()
						: "", roleName = role != null ? role.getName() : null;
				;
				int permission = categoryRolePermission.getPermission();
				mstr = categoryName + "(" + categoryId + ")" + ", " + roleName
						+ "(" + roleId + ")";
				logger.debug(
						"get category {}[{}] permission {} for role {}[{}]",
						categoryName, categoryId, permission, roleName, roleId);
			} else {
				logger.debug("category role permission of id {} is not exists",
						id);
			}
		}

		this.logManage(mstr, Action.GET, id, Status.SUCCESS, oper);
		this.afterCategoryRolePermissionManage(oper, Action.GET,
				Status.SUCCESS, categoryRolePermission, id);
		return categoryRolePermission;
	}

	@Override
	public List<CategoryRolePermission> getCategoryRolePermissions(User oper,
			QueryBase query) {
		if (!this.beforeCategoryRolePermissionManage(oper, Action.QUERY, null,
				query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query category role permissions, but it's blocked");
			}

			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.QUERY,
					Status.BLOCKED, null, query);
			return null;
		}

		List<CategoryRolePermission> results = categoryRolePermissionDAO
				.queryCategoryRolePermissions(query);
		if (query != null) {
			query.setResults(results);
			long total = categoryRolePermissionDAO
					.countCategoryRolePermissions(query);
			query.setTotal(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug(
						"query category role permissions with parameters {}",
						query.getParameters());
			} else {
				logger.debug("query category role permissions");
			}
		}

		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterCategoryRolePermissionManage(oper, Action.QUERY,
				Status.SUCCESS, null, query);
		return results;
	}

	@Override
	public long countCategoryRolePermissions(User oper, QueryBase query) {
		if (!this.beforeCategoryRolePermissionManage(oper, Action.COUNT, null,
				query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count category role permissions, but it's blocked");
			}

			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterCategoryRolePermissionManage(oper, Action.COUNT,
					Status.BLOCKED, null, query);
			return -1;
		}

		long c = categoryRolePermissionDAO.countCategoryRolePermissions(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug(
						"count category role permissions with parameters {} of {}",
						query.getParameters(), c);
			} else {
				logger.debug("count category role permissions of {}", c);
			}
		}

		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterCategoryRolePermissionManage(oper, Action.COUNT,
				Status.SUCCESS, null, query);
		return c;
	}

	private void logManage(String content, Short action, Long obj,
			Short status, User oper) {
		ManageLog manageLog = new ManageLog(ManageLog.MANAGE_LOG_XBLOG,
				content, action, ManageLogType.TAG_ROLE_PERMISSION, obj,
				oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}

	private boolean beforeCategoryRolePermissionManage(User oper, short action,
			CategoryRolePermission categoryRolePermission, Object... args) {
		if (categoryRolePermissionManageListeners != null) {
			for (CategoryRolePermissionManageListener listener : categoryRolePermissionManageListeners) {
				if (!listener.beforeCategoryRolePermissionManage(oper, action,
						categoryRolePermission, args)) {
					return false;
				}
			}
		}
		return true;
	}

	private void afterCategoryRolePermissionManage(User oper, short action,
			short result, CategoryRolePermission categoryRolePermission,
			Object... args) {
		if (categoryRolePermissionManageListeners != null) {
			for (CategoryRolePermissionManageListener listener : categoryRolePermissionManageListeners) {
				listener.afterCategoryRolePermissionManage(oper, action,
						result, categoryRolePermission, args);
			}
		}
	}

	public void setCategoryRolePermissionDAO(
			CategoryRolePermissionDAO categoryRolePermissionDAO) {
		this.categoryRolePermissionDAO = categoryRolePermissionDAO;
	}

	public CategoryRolePermissionDAO getCategoryRolePermissionDAO() {
		return this.categoryRolePermissionDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}

	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setCategoryRolePermissionManageListeners(
			List<CategoryRolePermissionManageListener> categoryRolePermissionManageListeners) {
		this.categoryRolePermissionManageListeners = categoryRolePermissionManageListeners;
	}

	public List<CategoryRolePermissionManageListener> getCategoryRolePermissionManageListeners() {
		return categoryRolePermissionManageListeners;
	}

	public void registerCategoryRolePermissionManageListener(
			CategoryRolePermissionManageListener categoryRolePermissionManageListener) {
		if (categoryRolePermissionManageListeners == null) {
			categoryRolePermissionManageListeners = new ArrayList<CategoryRolePermissionManageListener>();
		}
		categoryRolePermissionManageListeners
				.add(categoryRolePermissionManageListener);
	}

}
