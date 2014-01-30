package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.CategoryUserPermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:20:06 PM
 * @describe
 */
@Service
public interface CategoryUserPermissionService {

	public int addCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission);

	public int updateCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission);

	public int lockCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission);

	public int unlockCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission);

	public int removeCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission);

	public int deleteCategoryUserPermission(User oper,
			CategoryUserPermission categoryUserPermission);

	public boolean isCategoryUserPermissionExists(User oper,
			CategoryUserPermission categoryUserPermission);

	public boolean isCategoryUserPermissionUpdateable(User oper,
			CategoryUserPermission categoryUserPermission);

	public boolean isCategoryUserPermissionLocked(User oper,
			CategoryUserPermission categoryUserPermission);

	public boolean isCategoryUserPermissionRemoveable(User oper,
			CategoryUserPermission categoryUserPermission);

	public boolean isCategoryUserPermissionDeleteable(User oper,
			CategoryUserPermission categoryUserPermission);

	public CategoryUserPermission getCategoryUserPermission(User oper, long id);

	public List<CategoryUserPermission> getCategoryUserPermissions(User oper,
			QueryBase query);

	public long countCategoryUserPermissions(User oper, QueryBase query);

}
