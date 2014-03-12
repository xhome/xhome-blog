package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryUserPermission;
import org.xhome.xblog.Permission;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestCategoryUserPermissionManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class CategoryUserPermissionServiceTest extends AbstractTest {

	private CategoryUserPermissionService categoryUserPermissionService;
	private Category category;
	private User user;
	private int permission = Permission.COMMENT;
	private Long id = 1L;

	public CategoryUserPermissionServiceTest() {
		categoryUserPermissionService = context
				.getBean(CategoryUserPermissionServiceImpl.class);
		oper.setId(101L);
		category = new Category("Category");
		category.setId(1L);
		user = new User("Jhat");
		user.setId(1L);

		((CategoryUserPermissionServiceImpl) categoryUserPermissionService)
				.registerCategoryUserPermissionManageListener(new TestCategoryUserPermissionManageListener());
	}

	@Test
	public void testAddCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = new CategoryUserPermission(
				category, user, permission);
		categoryUserPermission.setOwner(1L);
		categoryUserPermission.setModifier(1L);
		categoryUserPermissionService.addCategoryUserPermission(oper,
				categoryUserPermission);
	}

	@Test
	public void testGetCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionService
				.getCategoryUserPermission(oper, id);
		printCategoryUserPermission(categoryUserPermission);
	}

	@Test
	public void testCountCategoryUserPermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", categoryUserPermissionService
				.countCategoryUserPermissions(oper, query));
	}

	@Test
	public void testGetCategoryUserPermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("category", "1");
		List<CategoryUserPermission> categoryUserPermissions = categoryUserPermissionService
				.getCategoryUserPermissions(oper, query);
		printCategoryUserPermission(categoryUserPermissions);
	}

	@Test
	public void testIsCategoryUserPermissionUpdateable() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionService
				.getCategoryUserPermission(oper, id);
		logger.info("{}", categoryUserPermissionService
				.isCategoryUserPermissionUpdateable(oper,
						categoryUserPermission));
	}

	@Test
	public void testIsCategoryUserPermissionDeleteable() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionService
				.getCategoryUserPermission(oper, id);
		logger.info("{}", categoryUserPermissionService
				.isCategoryUserPermissionDeleteable(oper,
						categoryUserPermission));
	}

	@Test
	public void testIsCategoryUserPermissionLocked() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionService
				.getCategoryUserPermission(oper, id);
		logger.info("{}", categoryUserPermissionService
				.isCategoryUserPermissionLocked(oper, categoryUserPermission));
	}

	@Test
	public void testLockCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionService
				.getCategoryUserPermission(oper, id);
		categoryUserPermissionService.lockCategoryUserPermission(oper,
				categoryUserPermission);
	}

	@Test
	public void testUnlockCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionService
				.getCategoryUserPermission(oper, id);
		categoryUserPermissionService.unlockCategoryUserPermission(oper,
				categoryUserPermission);
	}

	@Test
	public void testUpdateCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionService
				.getCategoryUserPermission(oper, id);
		categoryUserPermission.setId(id);
		// categoryUserPermission.setVersion(11);
		int r = categoryUserPermissionService.updateCategoryUserPermission(
				oper, categoryUserPermission);
		logger.info("result:" + r);
	}

	@Test
	public void testIsCategoryUserPermissionExists() {
		logger.info("{}", categoryUserPermissionService
				.isCategoryUserPermissionExists(oper,
						new CategoryUserPermission(category, user, permission)));
	}

	@Test
	public void testDeleteCategoryUserPermission() {
		CategoryUserPermission categoryUserPermission = categoryUserPermissionService
				.getCategoryUserPermission(oper, id);
		categoryUserPermissionService.deleteCategoryUserPermission(oper,
				categoryUserPermission);
	}

}
