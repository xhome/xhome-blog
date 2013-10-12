package org.xhome.xblog.core.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.ManageLogService;
import org.xhome.xblog.Article;
import org.xhome.xblog.BlogException;
import org.xhome.xblog.ManageLogType;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.dao.ArticleDAO;
import org.xhome.xblog.core.dao.TagDAO;
import org.xhome.xblog.core.listener.ArticleManageListener;
import org.xhome.xblog.core.listener.ArticleTagManageListener;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:53:33 PM
 * @describe 
 */
@Service
public class ArticleServiceImpl implements ArticleService {
	
	@Autowired(required = false)
	private ArticleDAO	articleDAO;
	@Autowired(required = false)
	private TagDAO tagDAO;
	@Autowired(required = false)
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<ArticleManageListener> articleManageListeners;
	@Autowired(required = false)
	private List<ArticleTagManageListener> articleTagManageListeners;
	
	private Logger	logger;
	
	public ArticleServiceImpl() {
		logger = LoggerFactory.getLogger(ArticleService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addArticle(User oper, Article article) {
		String title = article.getTitle();
		
		if (!this.beforeArticleManage(oper, Action.ADD, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add article {}, but it's blocked", title);
			}
			
			this.logManageArticle(null, Action.ADD, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.ADD, Status.BLOCKED, article);
			return Status.BLOCKED;
		}
		
		article.setStatus(Status.OK);
		article.setVersion((short)0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		article.setCreated(t);
		article.setModified(t);
		
		short r = articleDAO.addArticle(article) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add article {}", title);
			} else {
				logger.debug("fail to add article {}", title);
			}
		}

		this.logManageArticle(null, Action.ADD, article.getId(), r, oper);
		this.afterArticleManage(oper, Action.ADD, r, article);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateArticle(User oper, Article article) {
		String title = article.getTitle();
		Long id = article.getId();
		
		if (!this.beforeArticleManage(oper, Action.UPDATE, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update article {}[{}], but it's blocked", title, id);
			}
			
			this.logManageArticle(title, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.UPDATE, Status.BLOCKED, article);
			return Status.BLOCKED;
		}
		
		Article old = articleDAO.queryArticle(id);
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update article {}[{}], but it's not exists", title, id);
			}
			
			this.logManageArticle(title, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterArticleManage(oper, Action.UPDATE, Status.NOT_EXISTS, article);
			return Status.NOT_EXISTS;
		}
		
		String oldTitle = old.getTitle();
		
		if (!old.getVersion().equals(article.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update article {}[{}], but version not match", oldTitle, id);
			}
			
			this.logManageArticle(oldTitle, Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			this.afterArticleManage(oper, Action.UPDATE, Status.VERSION_NOT_MATCH, article);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update article {}[{}]", oldTitle, id);
			}
			
			this.logManageArticle(oldTitle, Action.UPDATE, id, status, oper);
			this.afterArticleManage(oper, Action.UPDATE, Status.EXISTS, article);
			return status;
		}
		
		article.setOwner(old.getOwner());
		article.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		article.setModified(t);
		
		short r  = articleDAO.updateArticle(article) == 1 ? Status.SUCCESS : Status.ERROR;
		if (r == Status.SUCCESS) {
			article.incrementVersion();
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update article {}[{}]", oldTitle, id);
			} else {
				logger.debug("fail to update article {}[{}]", oldTitle, id);
			}
		}

		this.logManageArticle(oldTitle, Action.UPDATE, id, r, oper);
		this.afterArticleManage(oper, Action.UPDATE, r, article);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockArticle(User oper, Article article) {
		String title = article.getTitle();
		Long id = article.getId();
		
		if (!this.beforeArticleManage(oper, Action.LOCK, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock article {}[{}], but it's blocked", title, id);
			}
			
			this.logManageArticle(title, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.LOCK, Status.BLOCKED, article);
			return Status.BLOCKED;
		}
		
		short r = articleDAO.lockArticle(article) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock article {}[{}]", title, id);
			} else {
				logger.debug("fail to lock article {}[{}]", title, id);
			}
		}

		this.logManageArticle(title, Action.LOCK, id, r, oper);
		this.afterArticleManage(oper, Action.LOCK, r, article);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockArticle(User oper, Article article) {
		String title = article.getTitle();
		Long id = article.getId();
		
		if (!this.beforeArticleManage(oper, Action.UNLOCK, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to unlock article {}[{}], but it's blocked", title, id);
			}
			
			this.logManageArticle(title, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.UNLOCK, Status.BLOCKED, article);
			return Status.BLOCKED;
		}
		
		short r = articleDAO.unlockArticle(article) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock article {}[{}]", title, id);
			} else {
				logger.debug("fail to unlock article {}[{}]", title, id);
			}
		}

		this.logManageArticle(title, Action.UNLOCK, id, r, oper);
		this.afterArticleManage(oper, Action.UNLOCK, r, article);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeArticle(User oper, Article article) {
		String title = article.getTitle();
		Long id = article.getId();
		
		if (!this.beforeArticleManage(oper, Action.REMOVE, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove article {}[{}], but it's blocked", title, id);
			}
			
			this.logManageArticle(title, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.REMOVE, Status.BLOCKED, article);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (articleDAO.isArticleRemoveable(article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove article {}[{}]", title, id);
			}
			articleDAO.removeArticle(article);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("article {}[{}] isn't removeable", title, id);
			}
			r = Status.NO_REMOVE;
		}
		
		this.logManageArticle(title, Action.REMOVE, id, r, oper);
		this.afterArticleManage(oper, Action.REMOVE, r, article);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteArticle(User oper, Article article) {
		String title = article.getTitle();
		Long id = article.getId();
		
		if (!this.beforeArticleManage(oper, Action.DELETE, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete article {}[{}], but it's blocked", title, id);
			}
			
			this.logManageArticle(title, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.DELETE, Status.BLOCKED, article);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (articleDAO.isArticleDeleteable(article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete article {}[{}]", title, id);
			}
			articleDAO.deleteArticle(article);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("article {}[{}] isn't deleteable", title, id);
			}
			r = Status.NO_DELETE;
		}

		this.logManageArticle(title, Action.DELETE, id, r, oper);
		this.afterArticleManage(oper, Action.DELETE, r, article);
		return r;
	}
	
	@Override
	public boolean isArticleUpdateable(User oper, Article article) {
		String title = article.getTitle();
		Long id = article.getId();
		
		if (!this.beforeArticleManage(oper, Action.IS_UPDATEABLE, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge updateable of article {}[{}], but it's blocked", title, id);
			}
			
			this.logManageArticle(title, Action.IS_UPDATEABLE, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, article);
			return false;
		}
		
		boolean e = articleDAO.isArticleUpdateable(article);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}] is updateable", title, id);
			} else {
				logger.debug("article {}[{}] isn't updateable", title, id);
			}
		}
		
		this.logManageArticle(title, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterArticleManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, article);
		return e;
	}
	
	@Override
	public boolean isArticleLocked(User oper, Article article) {
		String title = article.getTitle();
		Long id = article.getId();
		
		if (!this.beforeArticleManage(oper, Action.IS_LOCKED, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge locked of article {}[{}], but it's blocked", title, id);
			}
			
			this.logManageArticle(title, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.IS_LOCKED, Status.BLOCKED, article);
			return false;
		}
		
		boolean e = articleDAO.isArticleLocked(article);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}] is locked", title, id);
			} else {
				logger.debug("article {}[{}] isn't locked", title, id);
			}
		}

		this.logManageArticle(title, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterArticleManage(oper, Action.IS_LOCKED, Status.SUCCESS, article);
		return e;
	}
	
	@Override
	public boolean isArticleRemoveable(User oper, Article article) {
		String title = article.getTitle();
		Long id = article.getId();
		
		if (!this.beforeArticleManage(oper, Action.IS_REMOVEABLE, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge removeable of article {}[{}], but it's blocked", title, id);
			}
			
			this.logManageArticle(title, Action.IS_REMOVEABLE, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, article);
			return false;
		}
		
		boolean e = articleDAO.isArticleRemoveable(article);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}] is removeable", title, id);
			} else {
				logger.debug("article {}[{}] isn't removeable", title, id);
			}
		}
		
		this.logManageArticle(title, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterArticleManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, article);
		return e;
	}
	
	@Override
	public boolean isArticleDeleteable(User oper, Article article) {
		String title = article.getTitle();
		Long id = article.getId();
		
		if (!this.beforeArticleManage(oper, Action.IS_DELETEABLE, article)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge deleteable of article {}[{}], but it's blocked", title, id);
			}
			
			this.logManageArticle(title, Action.IS_DELETEABLE, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, article);
			return false;
		}
		
		boolean e = articleDAO.isArticleDeleteable(article);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}] is deleteable", title, id);
			} else {
				logger.debug("article {}[{}] isn't deleteable", title, id);
			}
		}

		this.logManageArticle(title, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterArticleManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, article);
		return e;
	}
	
	@Override
	public Article getArticle(User oper, long id) {
		if (!this.beforeArticleManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get article of id {}, but it's blocked", id);
			}
			
			this.logManageArticle("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}
		
		Article article = articleDAO.queryArticle(id);
		
		String title = null;
		if (logger.isDebugEnabled()) {
			if (article != null) {
				title = article.getTitle();
				logger.debug("get article {}[{}]", title, id);
			} else {
				logger.debug("article of id {} is not exists", id);
			}
		}
		
		this.logManageArticle(title, Action.GET, id, Status.SUCCESS, oper);
		this.afterArticleManage(oper, Action.GET, Status.SUCCESS, article, id);
		return article;
	}
	
	@Override
	public List<Article> getArticles(User oper) {
		return getArticles(oper, null);
	}
	
	@Override
	public List<Article> getArticles(User oper, QueryBase query) {
		if (!this.beforeArticleManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query articles, but it's blocked");
			}
			
			this.logManageArticle(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
		List<Article> results = articleDAO.queryArticles(query);
		if (query != null) {
			query.setResults(results);
			long total = articleDAO.countArticles(query);
			query.setTotalRow(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query articles with parameters {}", query.getParameters());
			} else {
				logger.debug("query articles");
			}
		}
		
		this.logManageArticle(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterArticleManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return results;
	}
	
	@Override
	public long countArticles(User oper) {
		return countArticles(oper, null);
	}
	
	@Override
	public long countArticles(User oper, QueryBase query) {
		if (!this.beforeArticleManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count articles, but it's blocked");
			}
			
			this.logManageArticle(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterArticleManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = articleDAO.countArticles(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count articles with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count articles of {}", c);
			}
		}
		
		this.logManageArticle(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterArticleManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private int doAddRole(User oper, Article article, Tag tag)  throws BlogException {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.ADD, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.ADD, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.ADD, Status.BLOCKED, article, tag);
			return Status.BLOCKED;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		articleTag.put("owner", article.getOwner());
		articleTag.put("modifier", article.getModifier());
		articleTag.put("status", Status.OK);
		articleTag.put("version", 0L);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		articleTag.put("created", t);
		articleTag.put("modified", t);
		
		if (articleDAO.hasArticleTag(articleTag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("article {}[{}] already has tag {}[{}]", title, articleId, tagName, tagId);
			}
			
			this.logManageArticleTag(mstr, Action.ADD, null, Status.EXISTS, oper);
			return Status.SUCCESS;
		}
		
		short r = articleDAO.addArticleTag(articleTag) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add tag {}[{}] for article {}[{}]", tagName, tagId, title, articleId);
			} else {
				logger.debug("fail to add tag {}[{}] for article {}[{}]", tagName, tagId, title, articleId);
			}
		}

		this.logManageArticleTag(mstr, Action.ADD, null, r, oper);
		this.afterArticleTagManage(oper, Action.ADD, r, article, tag);
		
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addArticleTag(User oper, Article article, Tag tag)  throws BlogException {
		return this.doAddRole(oper, article, tag);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addArticleTag(User oper, Article article, List<Tag> tags)  throws BlogException {
		if (logger.isDebugEnabled()) {
			logger.debug("add tags for article {}[{}]", article.getTitle(), article.getId());
		}
		
		if (tags != null) {
			int r = Status.ERROR;
			for (Tag tag : tags) {
				r = this.doAddRole(oper, article, tag);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockArticleTag(User oper, Article article, Tag tag) {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.LOCK, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.LOCK, Status.BLOCKED, article, tag);
			return Status.BLOCKED;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		articleTag.put("modifier", oper != null ? oper.getId() : article.getModifier());
		
		short r = articleDAO.lockArticleTag(articleTag) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock tag {}[{}] for article {}[{}]", tagName, tagName, title, articleId);
			} else {
				logger.debug("fail to lock tag {}[{}] for article {}[{}]", tagName, tagName, title, articleId);
			}
		}

		this.logManageArticleTag(mstr, Action.LOCK, null, r, oper);
		this.afterArticleTagManage(oper, Action.LOCK, r, article, tag);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockArticleTag(User oper, Article article, List<Tag> tags) {
		if (logger.isDebugEnabled()) {
			logger.debug("lock tags for article {}[{}]", article.getTitle(), article.getId());
		}
		
		if (tags != null) {
			int r = Status.ERROR;
			for (Tag tag : tags) {
				r = this.lockArticleTag(oper, article, tag);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockArticleTag(User oper, Article article, Tag tag) {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.UNLOCK, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.UNLOCK, Status.BLOCKED, article, tag);
			return Status.BLOCKED;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		articleTag.put("modifier", oper != null ? oper.getId() : article.getModifier());
		
		short r = articleDAO.unlockArticleTag(articleTag) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock tag {}[{}] for article {}[{}]", tagName, tagId, title, articleId);
			} else {
				logger.debug("fail to unlock tag {}[{}] for article {}[{}]", tagName, tagId, title, articleId);
			}
		}

		this.logManageArticleTag(mstr, Action.UNLOCK, null, r, oper);
		this.afterArticleTagManage(oper, Action.UNLOCK, r, article, tag);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockArticleTag(User oper, Article article, List<Tag> tags) {
		if (logger.isDebugEnabled()) {
			logger.debug("unlock tags for article {}[{}]", article.getTitle(), article.getId());
		}
		
		if (tags != null) {
			int r = Status.ERROR;
			for (Tag tag : tags) {
				r = this.unlockArticleTag(oper, article, tag);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeArticleTag(User oper, Article article, Tag tag) {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.REMOVE, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.REMOVE, Status.BLOCKED, article, tag);
			return Status.BLOCKED;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		
		short r = Status.SUCCESS;
		if (articleDAO.isArticleTagDeleteable(articleTag)) {
			articleTag.put("modifier", oper != null ? oper.getId() : article.getModifier());
			articleDAO.removeArticleTag(articleTag);
			if (logger.isDebugEnabled()) {
				logger.debug("remove tag {}[{}] from article {}[{}]", tagName, tagId, title, articleId);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("article {}[{}]'s tag {}[{}] isn't removeable", title, articleId, tagName, tagId);
			}
			r = Status.NO_REMOVE;
		}

		this.logManageArticleTag(mstr, Action.REMOVE, null, r, oper);
		this.afterArticleTagManage(oper, Action.REMOVE, r, article, tag);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeArticleTag(User oper, Article article, List<Tag> tags) {
		if (logger.isDebugEnabled()) {
			logger.debug("remove tags for article {}[{}]", article.getTitle(), article.getId());
		}
		
		if (tags != null) {
			int r = Status.ERROR;
			for (Tag tag : tags) {
				r = this.removeArticleTag(oper, article, tag);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteArticleTag(User oper, Article article, Tag tag) {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.DELETE, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.DELETE, Status.BLOCKED, article, tag);
			return Status.BLOCKED;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		
		short r = Status.SUCCESS;
		if (articleDAO.isArticleTagDeleteable(articleTag)) {
			articleDAO.deleteArticleTag(articleTag);
			if (logger.isDebugEnabled()) {
				logger.debug("delete tag {}[{}] from article {}[{}]", tagName, tagId, title, articleId);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("article {}[{}]'s tag {}[{}] isn't deleteable", title, articleId, tagName, tagId);
			}
			r = Status.NO_DELETE;
		}

		this.logManageArticleTag(mstr, Action.DELETE, null, r, oper);
		this.afterArticleTagManage(oper, Action.DELETE, r, article, tag);
		return Status.SUCCESS;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteArticleTag(User oper, Article article, List<Tag> tags) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete tags for article {}[{}]", article.getTitle(), article.getId());
		}
		
		if (tags != null) {
			int r = Status.ERROR;
			for (Tag tag : tags) {
				r = this.deleteArticleTag(oper, article, tag);
				if (r != Status.SUCCESS) return r;
			}
		}
		
		return Status.SUCCESS;
	}
	
	@Override
	public boolean hasArticleTag(User oper, Article article, Tag tag) {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.IS_EXISTS, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge exists of tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.IS_EXISTS, Status.BLOCKED, article, tag);
			return false;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		
		boolean h = articleDAO.hasArticleTag(articleTag);
		
		if (logger.isDebugEnabled()) {
			if (h) {
				logger.debug("article {}[{}] has tag {}[{}]", title, articleId, tagName, tagId);
			} else {
				logger.debug("article {}[{}] hasn't tag {}[{}]", title, articleId, tagName, tagId);
			}
		}

		this.logManageArticleTag(mstr, Action.IS_EXISTS, null, Status.SUCCESS, oper);
		this.afterArticleTagManage(oper, Action.IS_EXISTS, Status.SUCCESS, article, tag);
		return h;
	}
	
	@Override
	public boolean isArticleTagUpdateable(User oper, Article article, Tag tag) {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.IS_UPDATEABLE, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge updateable of tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.IS_UPDATEABLE, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, article, tag);
			return false;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		
		boolean e = articleDAO.isArticleTagUpdateable(articleTag);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}]'s tag {}[{}] is updateable", title, articleId, tagName, tagId);
			} else {
				logger.debug("article {}[{}]'s tag {}[{}] isn't updateable", title, articleId, tagName, tagId);
			}
		}

		this.logManageArticleTag(mstr, Action.IS_UPDATEABLE, null, Status.SUCCESS, oper);
		this.afterArticleTagManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, article, tag);
		return e;
	}
	
	@Override
	public boolean isArticleTagLocked(User oper, Article article, Tag tag) {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.IS_LOCKED, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge locked of tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.IS_LOCKED, Status.BLOCKED, article, tag);
			return false;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		
		boolean e = articleDAO.isArticleTagLocked(articleTag);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}]'s tag {}[{}] is locked", title, articleId, tagName, tagId);
			} else {
				logger.debug("article {}[{}]'s tag {}[{}] isn't locked", title, articleId, tagName, tagId);
			}
		}

		this.logManageArticleTag(mstr, Action.IS_LOCKED, null, Status.SUCCESS, oper);
		this.afterArticleTagManage(oper, Action.IS_LOCKED, Status.SUCCESS, article, tag);
		return e;
	}
	
	@Override
	public boolean isArticleTagRemoveable(User oper, Article article, Tag tag) {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.IS_REMOVEABLE, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge removeable of tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.IS_REMOVEABLE, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, article, tag);
			return false;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		
		boolean e = articleDAO.isArticleTagRemoveable(articleTag);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}]'s tag {}[{}] is removeable", title, articleId, tagName, tagId);
			} else {
				logger.debug("article {}[{}]'s tag {}[{}] isn't removeable", title, articleId, tagName, tagId);
			}
		}
		
		this.logManageArticleTag(mstr, Action.IS_REMOVEABLE, null, Status.SUCCESS, oper);
		this.afterArticleTagManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, article, tag);
		return e;
	}
	
	@Override
	public boolean isArticleTagDeleteable(User oper, Article article, Tag tag) {
		Long articleId = article.getId(), tagId = tag.getId();
		String title = article.getTitle(), tagName = tag.getName();
		String mstr = title + "#" + tagName;
		
		if (!this.beforeArticleTagManage(oper, Action.IS_DELETEABLE, article, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to judge deleteable of tag {}[{}] for article {}[{}], but it's blocked", tagName, tagId, title, articleId);
			}
			
			this.logManageArticleTag(mstr, Action.IS_DELETEABLE, null, Status.BLOCKED, oper);
			this.afterArticleTagManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, article, tag);
			return false;
		}
		
		Map<String, Object> articleTag = new HashMap<String, Object>();
		articleTag.put("article", article);
		articleTag.put("tag", tag);
		
		boolean e = articleDAO.isArticleTagDeleteable(articleTag);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("article {}[{}]'s tag {}[{}] is deleteable", title, articleId, tagName, tagId);
			} else {
				logger.debug("article {}[{}]'s tag {}[{}] isn't deleteable", title, articleId, tagName, tagId);
			}
		}
		
		this.logManageArticleTag(mstr, Action.IS_DELETEABLE, null, Status.SUCCESS, oper);
		this.afterArticleTagManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, article, tag);
		return e;
	}
	
	private void logManageArticle(String content, Short action, Long obj, Short status, User oper) {
		this.logManage(content, action, ManageLogType.ARTICLE, obj, status, oper);
	}
	
	private void logManageArticleTag(String content, Short action, Long obj, Short status, User oper) {
		this.logManage(content, action, ManageLogType.ARTICLE_TAG, obj, status, oper);
	}
	
	private void logManage(String content, Short action, Short type, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, type, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	private boolean beforeArticleManage(User oper, short action, Article article, Object ...args) {
		if (articleManageListeners != null) {
			for (ArticleManageListener listener : articleManageListeners) {
				if (!listener.beforeArticleManage(oper, action, article, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterArticleManage(User oper, short action, short result, Article article, Object ...args) {
		if (articleManageListeners != null) {
			for (ArticleManageListener listener : articleManageListeners) {
				listener.afterArticleManage(oper, action, result, article, args);
			}
		}
	}
	
	private boolean beforeArticleTagManage(User oper, short action, Article article, Tag tag, Object ...args) {
		if (articleTagManageListeners != null) {
			for (ArticleTagManageListener listener : articleTagManageListeners) {
				if (!listener.beforeArticleTagManage(oper, action, article, tag, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterArticleTagManage(User oper, short action, short result, Article article, Tag tag, Object ...args) {
		if (articleTagManageListeners != null) {
			for (ArticleTagManageListener listener : articleTagManageListeners) {
				listener.afterArticleTagManage(oper, action, result, article, tag, args);
			}
		}
	}
	
	public void setArticleDAO(ArticleDAO articleDAO) {
		this.articleDAO = articleDAO;
	}
	
	public ArticleDAO getArticleDAO() {
		return this.articleDAO;
	}
	
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}
	
	public TagDAO getTagDAO() {
		return this.tagDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}
	
	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setArticleManageListeners(List<ArticleManageListener> articleManageListeners) {
		this.articleManageListeners = articleManageListeners;
	}

	public List<ArticleManageListener> getArticleManageListeners() {
		return articleManageListeners;
	}
	
	public void registerArticleManageListener(ArticleManageListener articleManageListener) {
		if (articleManageListeners == null) {
			articleManageListeners = new ArrayList<ArticleManageListener>();
		}
		articleManageListeners.add(articleManageListener);
	}
	
	public void setArticleTagManageListeners(List<ArticleTagManageListener> articleTagManageListeners) {
		this.articleTagManageListeners = articleTagManageListeners;
	}

	public List<ArticleTagManageListener> getArticleTagManageListeners() {
		return articleTagManageListeners;
	}
	
	public void registerArticleTagManageListener(ArticleTagManageListener articleTagManageListener) {
		if (articleTagManageListeners == null) {
			synchronized (this) {
				if (articleTagManageListeners == null) {
					articleTagManageListeners = new ArrayList<ArticleTagManageListener>();
				}
			}
		}
		articleTagManageListeners.add(articleTagManageListener);
	}
	
}
