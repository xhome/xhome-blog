package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Article;
import org.xhome.xblog.BlogException;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestArticleManageListener;
import org.xhome.xblog.core.listener.TestArticleTagManageListener;
import org.xhome.xblog.core.listener.TestTagManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class ArticleServiceTest extends AbstractTest {

	private ArticleService articleService;
	private TagService tagService;
	private long id = 3L;

	public ArticleServiceTest() {
		articleService = context.getBean(ArticleServiceImpl.class);
		tagService = context.getBean(TagServiceImpl.class);
		oper.setId(101L);

		((ArticleServiceImpl) articleService)
				.registerArticleManageListener(new TestArticleManageListener());
		((ArticleServiceImpl) articleService)
				.registerArticleTagManageListener(new TestArticleTagManageListener());
		((TagServiceImpl) tagService)
				.registerTagManageListener(new TestTagManageListener());
	}

	@Test
	public void testAddArticle() {
		Article article = new Article("TestArticle");
		article.setOwner(1L);
		article.setModifier(1L);
		try {
			articleService.addArticle(oper, article);
		} catch (BlogException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetArticle() {
		Article article = articleService.getArticle(oper, id);
		printArticle(article);
	}

	@Test
	public void testCountArticles() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", articleService.countArticles(oper, query));
	}

	@Test
	public void testGetArticles() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		List<Article> articles = articleService.getArticles(oper, query);
		printArticle(articles);
	}

	@Test
	public void testIsArticleUpdateable() {
		Article article = articleService.getArticle(oper, id);
		logger.info("{}", articleService.isArticleUpdateable(oper, article));
	}

	@Test
	public void testIsArticleDeleteable() {
		Article article = articleService.getArticle(oper, id);
		logger.info("{}", articleService.isArticleDeleteable(oper, article));
	}

	@Test
	public void testIsArticleLocked() {
		Article article = articleService.getArticle(oper, id);
		logger.info("{}", articleService.isArticleLocked(oper, article));
	}

	@Test
	public void testLockArticle() {
		Article article = articleService.getArticle(oper, id);
		articleService.lockArticle(oper, article);
	}

	@Test
	public void testUnlockArticle() {
		Article article = articleService.getArticle(oper, id);
		articleService.unlockArticle(oper, article);
	}

	@Test
	public void testUpdateArticle() {
		Article article = articleService.getArticle(oper, id);
		article.setId(100L);
		// article.setVersion(11);
		try {
			int r = articleService.updateArticle(oper, article);
			logger.info("result:" + r);
		} catch (BlogException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRemoveArticle() {
		Article article = articleService.getArticle(oper, id);
		articleService.removeArticle(oper, article);
		articleService.deleteArticle(oper, article);
	}

	@Test
	public void testAddArticleTag() throws BlogException {
		Article article = articleService.getArticle(oper, id);
		Tag tag = tagService.getTag(oper, "TestTag");
		logger.debug("{}", articleService.addArticleTag(oper, article, tag));
	}

	@Test
	public void testHasArticleTag() {
		Article article = articleService.getArticle(oper, id);
		Tag tag = tagService.getTag(oper, "TestTag");
		logger.debug("{}", articleService.hasArticleTag(oper, article, tag));
	}

	@Test
	public void testIsArticleTagUpdateable() {
		Article article = articleService.getArticle(oper, 1L);
		Tag tag = tagService.getTag(oper, 1L);
		logger.info("{}",
				articleService.isArticleTagUpdateable(oper, article, tag));
	}

	@Test
	public void testIsArticleTagDeleteable() {
		Article article = articleService.getArticle(oper, 1L);
		Tag tag = tagService.getTag(oper, 1L);
		logger.info("{}",
				articleService.isArticleTagDeleteable(oper, article, tag));
	}

	@Test
	public void testIsArticleTagLocked() {
		Article article = articleService.getArticle(oper, 1L);
		Tag tag = tagService.getTag(oper, 1L);
		logger.info("{}", articleService.isArticleTagLocked(oper, article, tag));
	}

	@Test
	public void testLockArticleTag() {
		Article article = articleService.getArticle(oper, id);
		Tag tag = tagService.getTag(oper, 1L);
		articleService.lockArticleTag(oper, article, tag);
	}

	@Test
	public void testUnlockArticleTag() {
		Article article = articleService.getArticle(oper, id);
		Tag tag = tagService.getTag(oper, 1L);
		articleService.unlockArticleTag(oper, article, tag);
	}

	@Test
	public void testRemoveArticleTag() {
		Article article = articleService.getArticle(oper, id);
		Tag tag = tagService.getTag(oper, "TestTag");
		logger.debug("{}", articleService.removeArticleTag(oper, article, tag));
		// articleService.deleteArticleTag(oper, article, tag);
	}

}
