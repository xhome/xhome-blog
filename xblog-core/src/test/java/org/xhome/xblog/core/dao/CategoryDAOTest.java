package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Category;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:39 PM
 * @describe 
 */
public class CategoryDAOTest extends AbstractTest {

	private CategoryDAO categoryDAO;
	
	public CategoryDAOTest() {
		categoryDAO = context.getBean(CategoryDAO.class);
	}
	
	@Test
	public void testAddCategory() {
		Category category = new Category("TestCategory");
		category.setOwner(1L);
		category.setModifier(1L);
		categoryDAO.addCategory(category);
	}
	
	@Test
	public void testGetCategory() {
		Category category = categoryDAO.queryCategoryById(1L);
		printCategory(category);
	}
	
	@Test
	public void testQueryCategory() {
		QueryBase query = new QueryBase();
		List<Category> categorys = categoryDAO.queryCategorys(query);
		printCategory(categorys);
	}
	
	@Test
	public void testUpdateCategory() {
		Category category = categoryDAO.queryCategoryByName("TestCategory");
		printCategory(category);
		
		categoryDAO.updateCategory(category);
		
		category = categoryDAO.queryCategoryByName("TestCategory");
		printCategory(category);
	}
	
	@Test
	public void testDeleteCategory() {
		Category category = categoryDAO.queryCategoryByName("TestCategory");
		categoryDAO.deleteCategory(category);
	}
	
}
