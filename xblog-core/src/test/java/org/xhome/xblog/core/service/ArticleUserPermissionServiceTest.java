package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Permission;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleUserPermission;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestArticleUserPermissionManageListener;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:59:42 PM
 * @describe 
 */
public class ArticleUserPermissionServiceTest extends AbstractTest {
	
	private ArticleUserPermissionService	articleUserPermissionService;
	private Article article;
	private User user;
	private int permission = Permission.COMMENT;
	private Long id = 1L;
	
	public ArticleUserPermissionServiceTest() {
		articleUserPermissionService = context.getBean(ArticleUserPermissionServiceImpl.class);
		oper.setId(101L);
		article = new Article("Article");
		article.setId(1L);
		user = new User("Jhat");
		user.setId(1L);
		
		((ArticleUserPermissionServiceImpl)articleUserPermissionService).registerArticleUserPermissionManageListener(new TestArticleUserPermissionManageListener());
	}
	
	@Test
	public void testAddArticleUserPermission() {
		ArticleUserPermission articleUserPermission = new ArticleUserPermission(article, user, permission);
		articleUserPermission.setOwner(1L);
		articleUserPermission.setModifier(1L);
		articleUserPermissionService.addArticleUserPermission(oper, articleUserPermission);
	}
	
	@Test
	public void testGetArticleUserPermission() {
		ArticleUserPermission articleUserPermission = articleUserPermissionService.getArticleUserPermission(oper, id);
		printArticleUserPermission(articleUserPermission);
	}
	
	@Test
	public void testCountArticleUserPermissions() {
		logger.info("{}", articleUserPermissionService.countArticleUserPermissions(oper));
		
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", articleUserPermissionService.countArticleUserPermissions(oper, query));
	}
	
	@Test
	public void testGetArticleUserPermissions() {
		List<ArticleUserPermission> articleUserPermissions = articleUserPermissionService.getArticleUserPermissions(oper);
		printArticleUserPermission(articleUserPermissions);
		
		QueryBase query = new QueryBase();
		query.addParameter("article", "1");
		articleUserPermissions = articleUserPermissionService.getArticleUserPermissions(oper, query);
		printArticleUserPermission(articleUserPermissions);
	}
	
	@Test
	public void testIsArticleUserPermissionUpdateable(){
		ArticleUserPermission articleUserPermission = articleUserPermissionService.getArticleUserPermission(oper, id);
		logger.info("{}", articleUserPermissionService.isArticleUserPermissionUpdateable(oper, articleUserPermission));
	}
	
	@Test
	public void testIsArticleUserPermissionDeleteable(){
		ArticleUserPermission articleUserPermission = articleUserPermissionService.getArticleUserPermission(oper, id);
		logger.info("{}", articleUserPermissionService.isArticleUserPermissionDeleteable(oper, articleUserPermission));
	}
	
	@Test
	public void testIsArticleUserPermissionLocked(){
		ArticleUserPermission articleUserPermission = articleUserPermissionService.getArticleUserPermission(oper, id);
		logger.info("{}", articleUserPermissionService.isArticleUserPermissionLocked(oper, articleUserPermission));
	}
	
	@Test
	public void testLockArticleUserPermission(){
		ArticleUserPermission articleUserPermission = articleUserPermissionService.getArticleUserPermission(oper, id);
		articleUserPermissionService.lockArticleUserPermission(oper, articleUserPermission);
	}
	
	@Test
	public void testUnlockArticleUserPermission(){
		ArticleUserPermission articleUserPermission = articleUserPermissionService.getArticleUserPermission(oper, id);
		articleUserPermissionService.unlockArticleUserPermission(oper, articleUserPermission);
	}
	
	@Test
	public void testUpdateArticleUserPermission() {
		ArticleUserPermission articleUserPermission = articleUserPermissionService.getArticleUserPermission(oper, id);
		articleUserPermission.setId(id);
		// articleUserPermission.setVersion(11);
		int r = articleUserPermissionService.updateArticleUserPermission(oper, articleUserPermission);
		logger.info("result:" + r);
	}
	
	@Test
	public void testIsArticleUserPermissionExists() {
		logger.info("{}", articleUserPermissionService.isArticleUserPermissionExists(oper, new ArticleUserPermission(article, user, permission)));
	}
	
	@Test
	public void testRemoveArticleUserPermission() {
		ArticleUserPermission articleUserPermission = articleUserPermissionService.getArticleUserPermission(oper, id);
		articleUserPermissionService.removeArticleUserPermission(oper, articleUserPermission);
		articleUserPermissionService.deleteArticleUserPermission(oper, articleUserPermission);
	}
	
}
