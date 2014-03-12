package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.CategoryUserPermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 11, 201310:31:47 PM
 * @describe
 */
public interface CategoryUserPermissionDAO {

	public int addCategoryUserPermission(CategoryUserPermission permission);

	public int updateCategoryUserPermission(CategoryUserPermission permission);

	public int lockCategoryUserPermission(CategoryUserPermission permission);

	public int unlockCategoryUserPermission(CategoryUserPermission permission);

	public int deleteCategoryUserPermission(CategoryUserPermission permission);

	public boolean isCategoryUserPermissionExists(
			CategoryUserPermission permission);

	public boolean isCategoryUserPermissionUpdateable(
			CategoryUserPermission permission);

	public boolean isCategoryUserPermissionLocked(
			CategoryUserPermission permission);

	public boolean isCategoryUserPermissionDeleteable(
			CategoryUserPermission permission);

	public CategoryUserPermission queryCategoryUserPermission(Long id);

	public List<CategoryUserPermission> queryCategoryUserPermissions(
			QueryBase query);

	public long countCategoryUserPermissions(QueryBase query);

}
