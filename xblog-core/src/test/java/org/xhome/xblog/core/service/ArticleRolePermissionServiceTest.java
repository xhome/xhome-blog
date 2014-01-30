package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleRolePermission;
import org.xhome.xblog.Permission;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestArticleRolePermissionManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class ArticleRolePermissionServiceTest extends AbstractTest {

	private ArticleRolePermissionService articleRolePermissionService;
	private Article article;
	private Role role;
	private int permission = Permission.COMMENT;
	private Long id = 1L;

	public ArticleRolePermissionServiceTest() {
		articleRolePermissionService = context
				.getBean(ArticleRolePermissionServiceImpl.class);
		oper.setId(101L);
		article = new Article("Article");
		article.setId(1L);
		role = new Role("Admin");
		role.setId(1L);

		((ArticleRolePermissionServiceImpl) articleRolePermissionService)
				.registerArticleRolePermissionManageListener(new TestArticleRolePermissionManageListener());
	}

	@Test
	public void testAddArticleRolePermission() {
		ArticleRolePermission articleRolePermission = new ArticleRolePermission(
				article, role, permission);
		articleRolePermission.setOwner(1L);
		articleRolePermission.setModifier(1L);
		articleRolePermissionService.addArticleRolePermission(oper,
				articleRolePermission);
	}

	@Test
	public void testGetArticleRolePermission() {
		ArticleRolePermission articleRolePermission = articleRolePermissionService
				.getArticleRolePermission(oper, id);
		printArticleRolePermission(articleRolePermission);
	}

	@Test
	public void testCountArticleRolePermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", articleRolePermissionService
				.countArticleRolePermissions(oper, query));
	}

	@Test
	public void testGetArticleRolePermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("article", "1");
		List<ArticleRolePermission> articleRolePermissions = articleRolePermissionService
				.getArticleRolePermissions(oper, query);
		printArticleRolePermission(articleRolePermissions);
	}

	@Test
	public void testIsArticleRolePermissionUpdateable() {
		ArticleRolePermission articleRolePermission = articleRolePermissionService
				.getArticleRolePermission(oper, id);
		logger.info("{}", articleRolePermissionService
				.isArticleRolePermissionUpdateable(oper, articleRolePermission));
	}

	@Test
	public void testIsArticleRolePermissionDeleteable() {
		ArticleRolePermission articleRolePermission = articleRolePermissionService
				.getArticleRolePermission(oper, id);
		logger.info("{}", articleRolePermissionService
				.isArticleRolePermissionDeleteable(oper, articleRolePermission));
	}

	@Test
	public void testIsArticleRolePermissionLocked() {
		ArticleRolePermission articleRolePermission = articleRolePermissionService
				.getArticleRolePermission(oper, id);
		logger.info("{}", articleRolePermissionService
				.isArticleRolePermissionLocked(oper, articleRolePermission));
	}

	@Test
	public void testLockArticleRolePermission() {
		ArticleRolePermission articleRolePermission = articleRolePermissionService
				.getArticleRolePermission(oper, id);
		articleRolePermissionService.lockArticleRolePermission(oper,
				articleRolePermission);
	}

	@Test
	public void testUnlockArticleRolePermission() {
		ArticleRolePermission articleRolePermission = articleRolePermissionService
				.getArticleRolePermission(oper, id);
		articleRolePermissionService.unlockArticleRolePermission(oper,
				articleRolePermission);
	}

	@Test
	public void testUpdateArticleRolePermission() {
		ArticleRolePermission articleRolePermission = articleRolePermissionService
				.getArticleRolePermission(oper, id);
		articleRolePermission.setId(id);
		// articleRolePermission.setVersion(11);
		int r = articleRolePermissionService.updateArticleRolePermission(oper,
				articleRolePermission);
		logger.info("result:" + r);
	}

	@Test
	public void testIsArticleRolePermissionExists() {
		logger.info("{}", articleRolePermissionService
				.isArticleRolePermissionExists(oper, new ArticleRolePermission(
						article, role, permission)));
	}

	@Test
	public void testRemoveArticleRolePermission() {
		ArticleRolePermission articleRolePermission = articleRolePermissionService
				.getArticleRolePermission(oper, id);
		articleRolePermissionService.removeArticleRolePermission(oper,
				articleRolePermission);
		articleRolePermissionService.deleteArticleRolePermission(oper,
				articleRolePermission);
	}

}
