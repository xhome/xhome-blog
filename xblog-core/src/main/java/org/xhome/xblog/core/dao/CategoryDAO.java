package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Article;
import org.xhome.xblog.Category;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 2, 201310:24:24 PM
 * @describe
 */
public interface CategoryDAO {

	public int addCategory(Category category);

	public int updateCategory(Category category);

	public int lockCategory(Category category);

	public int unlockCategory(Category category);

	public int deleteCategory(Category category);

	public boolean isCategoryExists(Category category);

	public boolean isCategoryUpdateable(Category category);

	public boolean isCategoryLocked(Category category);

	public boolean isCategoryDeleteable(Category category);

	public Category queryCategoryById(Long id);

	public Category queryCategoryByName(String name);

	public List<Category> queryCategorys(QueryBase query);

	public long countCategorys(QueryBase query);

	/**
	 * 增加分类文章总数
	 * 
	 * @param category
	 * @return
	 */
	public int increaseCategoryArticle(Category category);

	/**
	 * 减少分类文章总数
	 * 
	 * @param category
	 * @return
	 */
	public int decreaseCategoryArticle(Category category);

	/**
	 * 增加分类文章总数
	 * 
	 * @param article
	 * @return
	 */
	public int increaseArticle(Article article);

	/**
	 * 减少分类文章总数
	 * 
	 * @param article
	 * @return
	 */
	public int decreaseArticle(Article article);

}
