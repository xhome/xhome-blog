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
import org.xhome.xblog.CategoryUserPermission;
import org.xhome.xblog.ManageLogType;
import org.xhome.xblog.core.dao.CategoryUserPermissionDAO;
import org.xhome.xblog.core.listener.CategoryUserPermissionManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:33 PM
 * @describe
 */
@Service
public class CategoryUserPermissionServiceImpl implements
		CategoryUserPermissionService {

	@Autowired
	private CategoryUserPermissionDAO categoryUserPermissionDAO;
	@Autowired
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<CategoryUserPermissionManageListener> categoryUserPermissionManageListeners;

	private Logger logger;

	public CategoryUserPermissionServiceImpl() {
		logger = LoggerFactory.getLogger(CategoryUserPermissionService.class);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission) {
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper, Action.ADD,
				categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to add category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.ADD, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.ADD,
					Status.BLOCKED, categoryUserPermission);
			return Status.BLOCKED;
		}

		if (categoryUserPermissionDAO
				.isCategoryUserPermissionExists(categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to add category {}[{}] permission {} for user {}[{}], but it's already exists",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.ADD, null, Status.EXISTS, oper);
			this.afterCategoryUserPermissionManage(oper, Action.ADD,
					Status.EXISTS, categoryUserPermission);
			return Status.EXISTS;
		}

		categoryUserPermission.setStatus(Status.OK);
		categoryUserPermission.setVersion((short) 0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		categoryUserPermission.setCreated(t);
		categoryUserPermission.setModified(t);

		short r = categoryUserPermissionDAO
				.addCategoryUserPermission(categoryUserPermission) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to add category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug(
						"fail to add category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.ADD, categoryUserPermission.getId(), r,
				oper);
		this.afterCategoryUserPermissionManage(oper, Action.ADD, r,
				categoryUserPermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission) {
		long id = categoryUserPermission.getId();
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper, Action.UPDATE,
				categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.UPDATE,
					Status.BLOCKED, categoryUserPermission);
			return Status.BLOCKED;
		}

		CategoryUserPermission old = categoryUserPermissionDAO
				.queryCategoryUserPermission(id);

		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update category {}[{}] permission {} for user {}[{}], but it's not exists",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterCategoryUserPermissionManage(oper, Action.UPDATE,
					Status.NOT_EXISTS, categoryUserPermission);
			return Status.NOT_EXISTS;
		}

		if (!old.getVersion().equals(categoryUserPermission.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to update category {}[{}] permission {} for user {}[{}], but version not match",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.UPDATE, id, Status.VERSION_NOT_MATCH,
					oper);
			this.afterCategoryUserPermissionManage(oper, Action.UPDATE,
					Status.VERSION_NOT_MATCH, categoryUserPermission);
			return Status.VERSION_NOT_MATCH;
		}

		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"it's not allowed to update category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.UPDATE, id, status, oper);
			this.afterCategoryUserPermissionManage(oper, Action.UPDATE,
					Status.EXISTS, categoryUserPermission);
			return status;
		}

		categoryUserPermission.setOwner(old.getOwner());
		categoryUserPermission.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		categoryUserPermission.setModified(t);

		short r = categoryUserPermissionDAO
				.updateCategoryUserPermission(categoryUserPermission) == 1 ? Status.SUCCESS
				: Status.ERROR;
		if (r == Status.SUCCESS) {
			categoryUserPermission.incrementVersion();
		}

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to update category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug(
						"fail to update category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.UPDATE, id, r, oper);
		this.afterCategoryUserPermissionManage(oper, Action.UPDATE, r,
				categoryUserPermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission) {
		long id = categoryUserPermission.getId();
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper, Action.LOCK,
				categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to lock category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.LOCK,
					Status.BLOCKED, categoryUserPermission);
			return Status.BLOCKED;
		}

		short r = categoryUserPermissionDAO
				.lockCategoryUserPermission(categoryUserPermission) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to lock category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug(
						"fail to lock category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.LOCK, id, r, oper);
		this.afterCategoryUserPermissionManage(oper, Action.LOCK, r,
				categoryUserPermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission) {
		long id = categoryUserPermission.getId();
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper, Action.UNLOCK,
				categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to unlock category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.UNLOCK,
					Status.BLOCKED, categoryUserPermission);
			return Status.BLOCKED;
		}

		short r = categoryUserPermissionDAO
				.unlockCategoryUserPermission(categoryUserPermission) == 1 ? Status.SUCCESS
				: Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug(
						"success to unlock category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug(
						"fail to unlock category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.UNLOCK, id, r, oper);
		this.afterCategoryUserPermissionManage(oper, Action.UNLOCK, r,
				categoryUserPermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission) {
		long id = categoryUserPermission.getId();
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper, Action.REMOVE,
				categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to remove category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.REMOVE,
					Status.BLOCKED, categoryUserPermission);
			return Status.BLOCKED;
		}

		short r = Status.SUCCESS;
		if (categoryUserPermissionDAO
				.isCategoryUserPermissionRemoveable(categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"remove category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			}
			categoryUserPermissionDAO
					.removeCategoryUserPermission(categoryUserPermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] isn't removeable",
						categoryName, categoryId, permission, userName, userId);
			}
			r = Status.NO_REMOVE;
		}

		this.logManage(mstr, Action.REMOVE, id, r, oper);
		this.afterCategoryUserPermissionManage(oper, Action.REMOVE, r,
				categoryUserPermission);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission) {
		long id = categoryUserPermission.getId();
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper, Action.DELETE,
				categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to delete category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.DELETE,
					Status.BLOCKED, categoryUserPermission);
			return Status.BLOCKED;
		}

		short r = Status.SUCCESS;
		if (categoryUserPermissionDAO
				.isCategoryUserPermissionDeleteable(categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"delete category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			}
			categoryUserPermissionDAO
					.deleteCategoryUserPermission(categoryUserPermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] isn't deleteable",
						categoryName, categoryId, permission, userName, userId);
			}
			r = Status.NO_DELETE;
		}

		this.logManage(mstr, Action.DELETE, id, r, oper);
		this.afterCategoryUserPermissionManage(oper, Action.DELETE, r,
				categoryUserPermission);
		return r;
	}

	@Override
	public boolean isCategoryUserPermissionExists(User oper,
			CategoryUserPermission categoryUserPermission) {
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper, Action.IS_EXISTS,
				categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge exists of category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.IS_EXISTS,
					Status.BLOCKED, categoryUserPermission);
			return false;
		}

		boolean e = categoryUserPermissionDAO
				.isCategoryUserPermissionExists(categoryUserPermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"exists of category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug(
						"not exists of category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.IS_EXISTS, categoryUserPermission.getId(),
				Status.SUCCESS, oper);
		this.afterCategoryUserPermissionManage(oper, Action.IS_EXISTS,
				Status.SUCCESS, categoryUserPermission);
		return e;
	}

	@Override
	public boolean isCategoryUserPermissionUpdateable(User oper,
			CategoryUserPermission categoryUserPermission) {
		long id = categoryUserPermission.getId();
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper,
				Action.IS_UPDATEABLE, categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge updateable of category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.IS_UPDATEABLE, null, Status.BLOCKED,
					oper);
			this.afterCategoryUserPermissionManage(oper, Action.IS_UPDATEABLE,
					Status.BLOCKED, categoryUserPermission);
			return false;
		}

		boolean e = categoryUserPermissionDAO
				.isCategoryUserPermissionUpdateable(categoryUserPermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] is updateable",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] isn't updateable",
						categoryName, categoryId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterCategoryUserPermissionManage(oper, Action.IS_UPDATEABLE,
				Status.SUCCESS, categoryUserPermission);
		return e;
	}

	@Override
	public boolean isCategoryUserPermissionLocked(User oper,
			CategoryUserPermission categoryUserPermission) {
		long id = categoryUserPermission.getId();
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper, Action.IS_LOCKED,
				categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge locked of category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.IS_LOCKED,
					Status.BLOCKED, categoryUserPermission);
			return false;
		}

		boolean e = categoryUserPermissionDAO
				.isCategoryUserPermissionLocked(categoryUserPermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] is locked",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] isn't locked",
						categoryName, categoryId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterCategoryUserPermissionManage(oper, Action.IS_LOCKED,
				Status.SUCCESS, categoryUserPermission);
		return e;
	}

	@Override
	public boolean isCategoryUserPermissionRemoveable(User oper,
			CategoryUserPermission categoryUserPermission) {
		long id = categoryUserPermission.getId();
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper,
				Action.IS_REMOVEABLE, categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge removeable of category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.IS_REMOVEABLE, null, Status.BLOCKED,
					oper);
			this.afterCategoryUserPermissionManage(oper, Action.IS_REMOVEABLE,
					Status.BLOCKED, categoryUserPermission);
			return false;
		}

		boolean e = categoryUserPermissionDAO
				.isCategoryUserPermissionRemoveable(categoryUserPermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] is removeable",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] isn't removeable",
						categoryName, categoryId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterCategoryUserPermissionManage(oper, Action.IS_REMOVEABLE,
				Status.SUCCESS, categoryUserPermission);
		return e;
	}

	@Override
	public boolean isCategoryUserPermissionDeleteable(User oper,
			CategoryUserPermission categoryUserPermission) {
		long id = categoryUserPermission.getId();
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
				.getId() : null;
		String categoryName = category != null ? category.getName() : "", userName = user != null ? user
				.getName() : "";
		int permission = categoryUserPermission.getPermission();
		String mstr = categoryName + "(" + categoryId + ")" + ", " + userName
				+ "(" + userId + ")";

		if (!this.beforeCategoryUserPermissionManage(oper,
				Action.IS_DELETEABLE, categoryUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge deleteable of category {}[{}] permission {} for user {}[{}], but it's blocked",
						categoryName, categoryId, permission, userName, userId);
			}

			this.logManage(mstr, Action.IS_DELETEABLE, null, Status.BLOCKED,
					oper);
			this.afterCategoryUserPermissionManage(oper, Action.IS_DELETEABLE,
					Status.BLOCKED, categoryUserPermission);
			return false;
		}

		boolean e = categoryUserPermissionDAO
				.isCategoryUserPermissionDeleteable(categoryUserPermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] is deleteable",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug(
						"category {}[{}] permission {} for user {}[{}] isn't deleteable",
						categoryName, categoryId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterCategoryUserPermissionManage(oper, Action.IS_DELETEABLE,
				Status.SUCCESS, categoryUserPermission);
		return e;
	}

	@Override
	public CategoryUserPermission getCategoryUserPermission(User oper, long id) {
		if (!this
				.beforeCategoryUserPermissionManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to get category user permission of id {}, but it's blocked",
						id);
			}

			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.GET,
					Status.BLOCKED, null, id);
			return null;
		}

		CategoryUserPermission categoryUserPermission = categoryUserPermissionDAO
				.queryCategoryUserPermission(id);

		String mstr = null;
		if (logger.isDebugEnabled()) {
			if (categoryUserPermission != null) {
				Category category = categoryUserPermission.getCategory();
				User user = categoryUserPermission.getUser();
				Long categoryId = category != null ? category.getId() : null, userId = user != null ? user
						.getId() : null;
				String categoryName = category != null ? category.getName()
						: "", userName = user != null ? user.getName() : "";
				int permission = categoryUserPermission.getPermission();
				mstr = categoryName + "(" + categoryId + ")" + ", " + userName
						+ "(" + userId + ")";
				logger.debug(
						"get category {}[{}] permission {} for user {}[{}]",
						categoryName, categoryId, permission, userName, userId);
			} else {
				logger.debug("category user permission of id {} is not exists",
						id);
			}
		}

		this.logManage(mstr, Action.GET, id, Status.SUCCESS, oper);
		this.afterCategoryUserPermissionManage(oper, Action.GET,
				Status.SUCCESS, categoryUserPermission, id);
		return categoryUserPermission;
	}

	@Override
	public List<CategoryUserPermission> getCategoryUserPermissions(User oper,
			QueryBase query) {
		if (!this.beforeCategoryUserPermissionManage(oper, Action.QUERY, null,
				query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query category user permissions, but it's blocked");
			}

			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.QUERY,
					Status.BLOCKED, null, query);
			return null;
		}

		List<CategoryUserPermission> results = categoryUserPermissionDAO
				.queryCategoryUserPermissions(query);
		if (query != null) {
			query.setResults(results);
			long total = categoryUserPermissionDAO
					.countCategoryUserPermissions(query);
			query.setTotal(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug(
						"query category user permissions with parameters {}",
						query.getParameters());
			} else {
				logger.debug("query category user permissions");
			}
		}

		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterCategoryUserPermissionManage(oper, Action.QUERY,
				Status.SUCCESS, null, query);
		return results;
	}

	@Override
	public long countCategoryUserPermissions(User oper, QueryBase query) {
		if (!this.beforeCategoryUserPermissionManage(oper, Action.COUNT, null,
				query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count category user permissions, but it's blocked");
			}

			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterCategoryUserPermissionManage(oper, Action.COUNT,
					Status.BLOCKED, null, query);
			return -1;
		}

		long c = categoryUserPermissionDAO.countCategoryUserPermissions(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug(
						"count category user permissions with parameters {} of {}",
						query.getParameters(), c);
			} else {
				logger.debug("count category user permissions of {}", c);
			}
		}

		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterCategoryUserPermissionManage(oper, Action.COUNT,
				Status.SUCCESS, null, query);
		return c;
	}

	private void logManage(String content, Short action, Long obj,
			Short status, User oper) {
		ManageLog manageLog = new ManageLog(ManageLog.MANAGE_LOG_XBLOG,
				content, action, ManageLogType.TAG_USER_PERMISSION, obj,
				oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}

	private boolean beforeCategoryUserPermissionManage(User oper, short action,
			CategoryUserPermission categoryUserPermission, Object... args) {
		if (categoryUserPermissionManageListeners != null) {
			for (CategoryUserPermissionManageListener listener : categoryUserPermissionManageListeners) {
				if (!listener.beforeCategoryUserPermissionManage(oper, action,
						categoryUserPermission, args)) {
					return false;
				}
			}
		}
		return true;
	}

	private void afterCategoryUserPermissionManage(User oper, short action,
			short result, CategoryUserPermission categoryUserPermission,
			Object... args) {
		if (categoryUserPermissionManageListeners != null) {
			for (CategoryUserPermissionManageListener listener : categoryUserPermissionManageListeners) {
				listener.afterCategoryUserPermissionManage(oper, action,
						result, categoryUserPermission, args);
			}
		}
	}

	public void setCategoryUserPermissionDAO(
			CategoryUserPermissionDAO categoryUserPermissionDAO) {
		this.categoryUserPermissionDAO = categoryUserPermissionDAO;
	}

	public CategoryUserPermissionDAO getCategoryUserPermissionDAO() {
		return this.categoryUserPermissionDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}

	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setCategoryUserPermissionManageListeners(
			List<CategoryUserPermissionManageListener> categoryUserPermissionManageListeners) {
		this.categoryUserPermissionManageListeners = categoryUserPermissionManageListeners;
	}

	public List<CategoryUserPermissionManageListener> getCategoryUserPermissionManageListeners() {
		return categoryUserPermissionManageListeners;
	}

	public void registerCategoryUserPermissionManageListener(
			CategoryUserPermissionManageListener categoryUserPermissionManageListener) {
		if (categoryUserPermissionManageListeners == null) {
			categoryUserPermissionManageListeners = new ArrayList<CategoryUserPermissionManageListener>();
		}
		categoryUserPermissionManageListeners
				.add(categoryUserPermissionManageListener);
	}

}
