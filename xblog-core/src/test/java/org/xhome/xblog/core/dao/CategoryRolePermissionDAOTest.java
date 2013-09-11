package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xblog.Permission;
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryRolePermission;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:39 PM
 * @describe 
 */
public class CategoryRolePermissionDAOTest extends AbstractTest {

	private CategoryRolePermissionDAO categoryRolePermissionDAO;
	private Category category;
	private Role role;
	private int permission = Permission.COMMENT;
	private Long id = 1L;
	
	public CategoryRolePermissionDAOTest() {
		categoryRolePermissionDAO = context.getBean(CategoryRolePermissionDAO.class);
		category = new Category("Category");
		category.setId(1L);
		role = new Role("Admin");
		role.setId(1L);
	}
	
	@Test
	public void testAddCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = new CategoryRolePermission(category, role, permission);
		categoryRolePermission.setOwner(1L);
		categoryRolePermission.setModifier(1L);
		categoryRolePermissionDAO.addCategoryRolePermission(categoryRolePermission);
	}
	
	@Test
	public void testGetCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionDAO.queryCategoryRolePermission(id);
		printCategoryRolePermission(categoryRolePermission);
	}
	
	@Test
	public void testQueryCategoryRolePermission() {
		QueryBase query = new QueryBase();
		List<CategoryRolePermission> categoryRolePermissions = categoryRolePermissionDAO.queryCategoryRolePermissions(query);
		printCategoryRolePermission(categoryRolePermissions);
	}
	
	@Test
	public void testUpdateCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionDAO.queryCategoryRolePermission(id);
		printCategoryRolePermission(categoryRolePermission);
		
		categoryRolePermissionDAO.updateCategoryRolePermission(categoryRolePermission);
		
		categoryRolePermission = categoryRolePermissionDAO.queryCategoryRolePermission(id);
		printCategoryRolePermission(categoryRolePermission);
	}
	
	@Test
	public void testDeleteCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionDAO.queryCategoryRolePermission(id);
		categoryRolePermissionDAO.deleteCategoryRolePermission(categoryRolePermission);
	}
	
}
