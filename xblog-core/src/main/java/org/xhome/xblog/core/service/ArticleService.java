package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Article;
import org.xhome.xblog.BlogException;
import org.xhome.xblog.Tag;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:21:14 PM
 * @describe
 */
@Service
public interface ArticleService {

	public int addArticle(User oper, Article article) throws BlogException;

	public int updateArticle(User oper, Article article) throws BlogException;

	public int lockArticle(User oper, Article article);

	public int unlockArticle(User oper, Article article);

	public int removeArticle(User oper, Article article);

	public int removeArticles(User oper, List<Article> articles);

	public int deleteArticle(User oper, Article article);

	public int deleteArticles(User oper, List<Article> articles);

	public boolean isArticleUpdateable(User oper, Article article);

	public boolean isArticleLocked(User oper, Article article);

	public boolean isArticleRemoveable(User oper, Article article);

	public boolean isArticleDeleteable(User oper, Article article);

	public Article getArticle(User oper, long id);

	public List<Article> getArticles(User oper, QueryBase query);

	public long countArticles(User oper, QueryBase query);

	public int addArticleTag(User oper, Article article, Tag tag)
			throws BlogException;

	public int addArticleTag(User oper, Article article, List<Tag> tags)
			throws BlogException;

	public int lockArticleTag(User oper, Article article, Tag tag);

	public int lockArticleTag(User oper, Article article, List<Tag> tags);

	public int unlockArticleTag(User oper, Article article, Tag tag);

	public int unlockArticleTag(User oper, Article article, List<Tag> tags);

	public int removeArticleTag(User oper, Article article);

	public int removeArticleTag(User oper, Article article, Tag tag);

	public int removeArticleTag(User oper, Article article, List<Tag> tags);

	public int deleteArticleTag(User oper, Article article);

	public int deleteArticleTag(User oper, Article article, Tag tag);

	public int deleteArticleTag(User oper, Article article, List<Tag> tags);

	public boolean hasArticleTag(User oper, Article article, Tag tag);

	public boolean isArticleTagUpdateable(User oper, Article article, Tag tag);

	public boolean isArticleTagLocked(User oper, Article article, Tag tag);

	public boolean isArticleTagRemoveable(User oper, Article article, Tag tag);

	public boolean isArticleTagDeleteable(User oper, Article article, Tag tag);

	/**
	 * 文章增加阅读数
	 * 
	 * @param article
	 * @return
	 */
	public int increaseRead(User oper, Article article);

}
