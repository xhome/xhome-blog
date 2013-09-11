package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Permission;
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryUserPermission;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:39 PM
 * @describe 
 */
public class CategoryUserPermissionDAOTest extends AbstractTest {

	private CategoryUserPermissionDAO categoryUserPermissionDAO;
	private Category category;
	private User user;
	private int permission = Permission.COMMENT;
	private Long id = 1L;
	
	public CategoryUserPermissionDAOTest() {
		categoryUserPermissionDAO = context.getBean(CategoryUserPermissionDAO.class);
		category = new Category("Category");
		category.setId(1L);
		user = new User("Jhat");
		user.setId(1L);
	}
	
	@Test
	public void testAddCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = new CategoryUserPermission(category, user, permission);
		categoryUserPermission.setOwner(1L);
		categoryUserPermission.setModifier(1L);
		categoryUserPermissionDAO.addCategoryUserPermission(categoryUserPermission);
	}
	
	@Test
	public void testGetCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionDAO.queryCategoryUserPermission(id);
		printCategoryUserPermission(categoryUserPermission);
	}
	
	@Test
	public void testQueryCategoryUserPermission() {
		QueryBase query = new QueryBase();
		List<CategoryUserPermission> categoryUserPermissions = categoryUserPermissionDAO.queryCategoryUserPermissions(query);
		printCategoryUserPermission(categoryUserPermissions);
	}
	
	@Test
	public void testUpdateCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionDAO.queryCategoryUserPermission(id);
		printCategoryUserPermission(categoryUserPermission);
		
		categoryUserPermissionDAO.updateCategoryUserPermission(categoryUserPermission);
		
		categoryUserPermission = categoryUserPermissionDAO.queryCategoryUserPermission(id);
		printCategoryUserPermission(categoryUserPermission);
	}
	
	@Test
	public void testDeleteCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionDAO.queryCategoryUserPermission(id);
		categoryUserPermissionDAO.deleteCategoryUserPermission(categoryUserPermission);
	}
	
}
