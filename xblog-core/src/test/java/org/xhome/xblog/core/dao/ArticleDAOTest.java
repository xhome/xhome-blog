package org.xhome.xblog.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Article;
import org.xhome.xblog.Category;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 201311:11:07 PM
 * @describe 
 */
public class ArticleDAOTest extends AbstractTest {

	private ArticleDAO	articleDAO;
	private CategoryDAO	categoryDAO;
	private TagDAO		tagDAO;
	private long id = 1L;
	
	public ArticleDAOTest() {
		articleDAO = context.getBean(ArticleDAO.class);
		categoryDAO = context.getBean(CategoryDAO.class);
		tagDAO = context.getBean(TagDAO.class);
	}
	
	@Test
	public void testAddArticle() {
		Article article = new Article("TestArticle");
		article.setContent("content");
		article.setOwner(1L);
		article.setModifier(1L);
		
		Category category = categoryDAO.queryCategoryById(1L);
		article.setCategory(category);
		
		articleDAO.addArticle(article);
		
		Tag tag = tagDAO.queryTagById(1L);
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		articleTag.put("owner", 1);
		articleTag.put("modifier", 1);
		articleTag.put("version", 0);
		articleTag.put("status", 1);
		articleDAO.addArticleTag(articleTag);
	}
	
	@Test
	public void testGetArticle() {
		Article article = articleDAO.queryArticle(1L);
		printArticle(article);
	}
	
	@Test
	public void testQueryArticle() {
		QueryBase query = new QueryBase();
		query.addParameter("category_name", "T");
		query.addParameter("tag_name", "e");
		List<Article> articles = articleDAO.queryArticles(query);
		printArticle(articles);
	}
	
	@Test
	public void testUpdateArticle() {
		Article article = articleDAO.queryArticle(id);
		printArticle(article);
		
		article.setAttribute(Article.PERMISSION_PRIVATE);
		articleDAO.updateArticle(article);
		
		article = articleDAO.queryArticle(id);
		printArticle(article);
	}
	
	@Test
	public void testDeleteArticle() {
		Article article = articleDAO.queryArticle(id);
		articleDAO.deleteArticle(article);
	}
	
	@Test
	public void testArticleTag() {
		Article article = articleDAO.queryArticle(1L);
		Tag tag = tagDAO.queryTagById(1L);
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		articleTag.put("modifier", 1);
//		logger.info(articleDAO.deleteArticleTag(articleTag));
	}
	
}
