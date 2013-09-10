package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.Article;
import org.xhome.xblog.Tag;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 10, 201311:28:58 PM
 * @description 
 */
public class TestArticleTagManageListener implements ArticleTagManageListener {

	private Logger logger = LoggerFactory.getLogger(TestArticleTagManageListener.class);
	
	@Override
	public boolean beforeArticleTagManage(User oper, short action, Article article, Tag tag,
			Object... args) {
		logger.debug("TEST BEFORE ARTICLE TAG MANAGE LISTENER {} {} {}", oper.getName(), action, article != null ? article.getTitle() : "NULL");
		return true;
	}

	@Override
	public void afterArticleTagManage(User oper, short action, short result,
			Article article, Tag tag, Object... args) {
		logger.debug("TEST AFTER ARTICLE TAG MANAGE LISTENER {} {} {}", oper.getName(), action, article != null ? article.getTitle() : "NULL");
	}

}
