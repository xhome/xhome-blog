package org.xhome.xblog.core.dao;

import java.util.List;
import java.util.Map;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Article;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 2, 201310:55:20 PM
 * @describe
 */
public interface ArticleDAO {

	public int addArticle(Article article);

	public int updateArticle(Article article);

	public int lockArticle(Article article);

	public int unlockArticle(Article article);

	public int deleteArticle(Article article);

	public boolean isArticleUpdateable(Article article);

	public boolean isArticleLocked(Article article);

	public boolean isArticleDeleteable(Article article);

	public Article queryArticle(Long id);

	public List<Article> queryArticles(QueryBase query);

	public long countArticles(QueryBase query);

	public int addArticleTag(Map<String, Object> articleTag);

	public int lockArticleTag(Map<String, Object> articleTag);

	public int unlockArticleTag(Map<String, Object> articleTag);

	public int deleteArticleTag(Map<String, Object> articleTag);

	public boolean hasArticleTag(Map<String, Object> articleTag);

	public boolean isArticleTagUpdateable(Map<String, Object> articleTag);

	public boolean isArticleTagLocked(Map<String, Object> articleTag);

	public boolean isArticleTagDeleteable(Map<String, Object> articleTag);

	/**
	 * 文章增加阅读数
	 * 
	 * @param article
	 * @return
	 */
	public int increaseRead(Article article);

	/**
	 * 文章增加评论数
	 * 
	 * @param article
	 * @return
	 */
	public int increaseComment(Article article);

	/**
	 * 文章减少评论数
	 * 
	 * @param article
	 * @return
	 */
	public int decreaseComment(Article article);

}
