package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryRolePermission;
import org.xhome.xblog.Permission;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestCategoryRolePermissionManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class CategoryRolePermissionServiceTest extends AbstractTest {

	private CategoryRolePermissionService categoryRolePermissionService;
	private Category category;
	private Role role;
	private int permission = Permission.COMMENT;
	private Long id = 1L;

	public CategoryRolePermissionServiceTest() {
		categoryRolePermissionService = context
				.getBean(CategoryRolePermissionServiceImpl.class);
		oper.setId(101L);
		category = new Category("Category");
		category.setId(1L);
		role = new Role("Admin");
		role.setId(1L);

		((CategoryRolePermissionServiceImpl) categoryRolePermissionService)
				.registerCategoryRolePermissionManageListener(new TestCategoryRolePermissionManageListener());
	}

	@Test
	public void testAddCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = new CategoryRolePermission(
				category, role, permission);
		categoryRolePermission.setOwner(1L);
		categoryRolePermission.setModifier(1L);
		categoryRolePermissionService.addCategoryRolePermission(oper,
				categoryRolePermission);
	}

	@Test
	public void testGetCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionService
				.getCategoryRolePermission(oper, id);
		printCategoryRolePermission(categoryRolePermission);
	}

	@Test
	public void testCountCategoryRolePermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", categoryRolePermissionService
				.countCategoryRolePermissions(oper, query));
	}

	@Test
	public void testGetCategoryRolePermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("category", "1");
		List<CategoryRolePermission> categoryRolePermissions = categoryRolePermissionService
				.getCategoryRolePermissions(oper, query);
		printCategoryRolePermission(categoryRolePermissions);
	}

	@Test
	public void testIsCategoryRolePermissionUpdateable() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionService
				.getCategoryRolePermission(oper, id);
		logger.info("{}", categoryRolePermissionService
				.isCategoryRolePermissionUpdateable(oper,
						categoryRolePermission));
	}

	@Test
	public void testIsCategoryRolePermissionDeleteable() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionService
				.getCategoryRolePermission(oper, id);
		logger.info("{}", categoryRolePermissionService
				.isCategoryRolePermissionDeleteable(oper,
						categoryRolePermission));
	}

	@Test
	public void testIsCategoryRolePermissionLocked() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionService
				.getCategoryRolePermission(oper, id);
		logger.info("{}", categoryRolePermissionService
				.isCategoryRolePermissionLocked(oper, categoryRolePermission));
	}

	@Test
	public void testLockCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionService
				.getCategoryRolePermission(oper, id);
		categoryRolePermissionService.lockCategoryRolePermission(oper,
				categoryRolePermission);
	}

	@Test
	public void testUnlockCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionService
				.getCategoryRolePermission(oper, id);
		categoryRolePermissionService.unlockCategoryRolePermission(oper,
				categoryRolePermission);
	}

	@Test
	public void testUpdateCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionService
				.getCategoryRolePermission(oper, id);
		categoryRolePermission.setId(id);
		// categoryRolePermission.setVersion(11);
		int r = categoryRolePermissionService.updateCategoryRolePermission(
				oper, categoryRolePermission);
		logger.info("result:" + r);
	}

	@Test
	public void testIsCategoryRolePermissionExists() {
		logger.info("{}", categoryRolePermissionService
				.isCategoryRolePermissionExists(oper,
						new CategoryRolePermission(category, role, permission)));
	}

	@Test
	public void testRemoveCategoryRolePermission() {
		CategoryRolePermission categoryRolePermission = categoryRolePermissionService
				.getCategoryRolePermission(oper, id);
		categoryRolePermissionService.removeCategoryRolePermission(oper,
				categoryRolePermission);
		categoryRolePermissionService.deleteCategoryRolePermission(oper,
				categoryRolePermission);
	}

}
