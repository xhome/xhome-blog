package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Category;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestCategoryManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class CategoryServiceTest extends AbstractTest {

	private CategoryService categoryService;

	public CategoryServiceTest() {
		categoryService = context.getBean(CategoryServiceImpl.class);
		oper.setId(101L);

		((CategoryServiceImpl) categoryService)
				.registerCategoryManageListener(new TestCategoryManageListener());
	}

	@Test
	public void testAddCategory() {
		Category category = new Category("TestCategory");
		category.setOwner(1L);
		category.setModifier(1L);
		categoryService.addCategory(oper, category);
	}

	@Test
	public void testGetCategory() {
		Category category = categoryService.getCategory(oper, 1L);
		printCategory(category);

		category = categoryService.getCategory(oper, "TestCategory");
		printCategory(category);
	}

	@Test
	public void testCountCategorys() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", categoryService.countCategorys(oper, query));
	}

	@Test
	public void testGetCategorys() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		List<Category> categorys = categoryService.getCategorys(oper, query);
		printCategory(categorys);
	}

	@Test
	public void testIsCategoryUpdateable() {
		Category category = categoryService.getCategory(oper, 1L);
		logger.info("{}", categoryService.isCategoryUpdateable(oper, category));
	}

	@Test
	public void testIsCategoryDeleteable() {
		Category category = categoryService.getCategory(oper, 1L);
		logger.info("{}", categoryService.isCategoryDeleteable(oper, category));
	}

	@Test
	public void testIsCategoryLocked() {
		Category category = categoryService.getCategory(oper, 1L);
		logger.info("{}", categoryService.isCategoryLocked(oper, category));
	}

	@Test
	public void testLockCategory() {
		Category category = categoryService.getCategory(oper, 1L);
		categoryService.lockCategory(oper, category);
	}

	@Test
	public void testUnlockCategory() {
		Category category = categoryService.getCategory(oper, 1L);
		categoryService.unlockCategory(oper, category);
	}

	@Test
	public void testUpdateCategory() {
		Category category = categoryService.getCategory(oper, "TestCategory");
		category.setId(100L);
		// category.setVersion(11);
		int r = categoryService.updateCategory(oper, category);
		logger.info("result:" + r);
	}

	@Test
	public void testIsCategoryExists() {
		logger.info("{}", categoryService.isCategoryExists(oper, new Category(
				"TestCategory")));
	}

	@Test
	public void testDeleteCategory() {
		Category category = categoryService.getCategory(oper, "TestCategory");
		categoryService.deleteCategory(oper, category);
	}

}
