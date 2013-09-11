package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xblog.Permission;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleRolePermission;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:39 PM
 * @describe 
 */
public class ArticleRolePermissionDAOTest extends AbstractTest {

	private ArticleRolePermissionDAO articleRolePermissionDAO;
	private Article article;
	private Role role;
	private int permission = Permission.COMMENT;
	private Long id = 1L;
	
	public ArticleRolePermissionDAOTest() {
		articleRolePermissionDAO = context.getBean(ArticleRolePermissionDAO.class);
		article = new Article("TTTT");
		article.setId(1L);
		role = new Role("Admin");
		role.setId(1L);
	}
	
	@Test
	public void testAddArticleRolePermission() {
		ArticleRolePermission articleRolePermission = new ArticleRolePermission(article, role, permission);
		articleRolePermission.setOwner(1L);
		articleRolePermission.setModifier(1L);
		articleRolePermissionDAO.addArticleRolePermission(articleRolePermission);
	}
	
	@Test
	public void testGetArticleRolePermission() {
		ArticleRolePermission articleRolePermission = articleRolePermissionDAO.queryArticleRolePermission(id);
		printArticleRolePermission(articleRolePermission);
	}
	
	@Test
	public void testQueryArticleRolePermission() {
		QueryBase query = new QueryBase();
		List<ArticleRolePermission> articleRolePermissions = articleRolePermissionDAO.queryArticleRolePermissions(query);
		printArticleRolePermission(articleRolePermissions);
	}
	
	@Test
	public void testUpdateArticleRolePermission() {
		ArticleRolePermission articleRolePermission = articleRolePermissionDAO.queryArticleRolePermission(id);
		printArticleRolePermission(articleRolePermission);
		
		articleRolePermissionDAO.updateArticleRolePermission(articleRolePermission);
		
		articleRolePermission = articleRolePermissionDAO.queryArticleRolePermission(id);
		printArticleRolePermission(articleRolePermission);
	}
	
	@Test
	public void testDeleteArticleRolePermission() {
		ArticleRolePermission articleRolePermission = articleRolePermissionDAO.queryArticleRolePermission(id);
		articleRolePermissionDAO.deleteArticleRolePermission(articleRolePermission);
	}
	
}
