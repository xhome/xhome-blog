package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.CategoryRolePermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:20:06 PM
 * @describe
 */
@Service
public interface CategoryRolePermissionService {

	public int addCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission);

	public int updateCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission);

	public int lockCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission);

	public int unlockCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission);

	public int removeCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission);

	public int removeCategoryRolePermissions(User oper,
			List<CategoryRolePermission> categoryRolePermissions);

	public int deleteCategoryRolePermission(User oper,
			CategoryRolePermission categoryRolePermission);

	public int deleteCategoryRolePermissions(User oper,
			List<CategoryRolePermission> categoryRolePermissions);

	public boolean isCategoryRolePermissionExists(User oper,
			CategoryRolePermission categoryRolePermission);

	public boolean isCategoryRolePermissionUpdateable(User oper,
			CategoryRolePermission categoryRolePermission);

	public boolean isCategoryRolePermissionLocked(User oper,
			CategoryRolePermission categoryRolePermission);

	public boolean isCategoryRolePermissionRemoveable(User oper,
			CategoryRolePermission categoryRolePermission);

	public boolean isCategoryRolePermissionDeleteable(User oper,
			CategoryRolePermission categoryRolePermission);

	public CategoryRolePermission getCategoryRolePermission(User oper, long id);

	public List<CategoryRolePermission> getCategoryRolePermissions(User oper,
			QueryBase query);

	public long countCategoryRolePermissions(User oper, QueryBase query);

}
