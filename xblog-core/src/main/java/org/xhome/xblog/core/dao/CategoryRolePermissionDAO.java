package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.CategoryRolePermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 11, 201310:32:06 PM
 * @describe
 */
public interface CategoryRolePermissionDAO {

	public int addCategoryRolePermission(CategoryRolePermission permission);

	public int updateCategoryRolePermission(CategoryRolePermission permission);

	public int lockCategoryRolePermission(CategoryRolePermission permission);

	public int unlockCategoryRolePermission(CategoryRolePermission permission);

	public int deleteCategoryRolePermission(CategoryRolePermission permission);

	public boolean isCategoryRolePermissionExists(
			CategoryRolePermission permission);

	public boolean isCategoryRolePermissionUpdateable(
			CategoryRolePermission permission);

	public boolean isCategoryRolePermissionLocked(
			CategoryRolePermission permission);

	public boolean isCategoryRolePermissionDeleteable(
			CategoryRolePermission permission);

	public CategoryRolePermission queryCategoryRolePermission(Long id);

	public List<CategoryRolePermission> queryCategoryRolePermissions(
			QueryBase query);

	public long countCategoryRolePermissions(QueryBase query);

}
