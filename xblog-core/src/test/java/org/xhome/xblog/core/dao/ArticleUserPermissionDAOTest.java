package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Permission;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleUserPermission;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:39 PM
 * @describe 
 */
public class ArticleUserPermissionDAOTest extends AbstractTest {

	private ArticleUserPermissionDAO articleUserPermissionDAO;
	private Article article;
	private User user;
	private int permission = Permission.COMMENT;
	private Long id = 1L;
	
	public ArticleUserPermissionDAOTest() {
		articleUserPermissionDAO = context.getBean(ArticleUserPermissionDAO.class);
		article = new Article("Article");
		article.setId(1L);
		user = new User("Jhat");
		user.setId(1L);
	}
	
	@Test
	public void testAddArticleUserPermission() {
		ArticleUserPermission articleUserPermission = new ArticleUserPermission(article, user, permission);
		articleUserPermission.setOwner(1L);
		articleUserPermission.setModifier(1L);
		articleUserPermissionDAO.addArticleUserPermission(articleUserPermission);
	}
	
	@Test
	public void testGetArticleUserPermission() {
		ArticleUserPermission articleUserPermission = articleUserPermissionDAO.queryArticleUserPermission(id);
		printArticleUserPermission(articleUserPermission);
	}
	
	@Test
	public void testQueryArticleUserPermission() {
		QueryBase query = new QueryBase();
		List<ArticleUserPermission> articleUserPermissions = articleUserPermissionDAO.queryArticleUserPermissions(query);
		printArticleUserPermission(articleUserPermissions);
	}
	
	@Test
	public void testUpdateArticleUserPermission() {
		ArticleUserPermission articleUserPermission = articleUserPermissionDAO.queryArticleUserPermission(id);
		printArticleUserPermission(articleUserPermission);
		
		articleUserPermissionDAO.updateArticleUserPermission(articleUserPermission);
		
		articleUserPermission = articleUserPermissionDAO.queryArticleUserPermission(id);
		printArticleUserPermission(articleUserPermission);
	}
	
	@Test
	public void testDeleteArticleUserPermission() {
		ArticleUserPermission articleUserPermission = articleUserPermissionDAO.queryArticleUserPermission(id);
		articleUserPermissionDAO.deleteArticleUserPermission(articleUserPermission);
	}
	
}
