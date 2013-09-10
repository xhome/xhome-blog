package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.Article;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:58:52 PM
 * @describe 
 */
public class TestArticleManageListener implements ArticleManageListener {

	private Logger logger = LoggerFactory.getLogger(TestArticleManageListener.class);
	
	@Override
	public boolean beforeArticleManage(User oper, short action, Article article,
			Object... args) {
		logger.debug("TEST BEFORE ARTICLE MANAGE LISTENER {} {} {}", oper.getName(), action, article != null ? article.getTitle() : "NULL");
		return true;
	}

	@Override
	public void afterArticleManage(User oper, short action, short result,
			Article article, Object... args) {
		logger.debug("TEST AFTER ARTICLE MANAGE LISTENER {} {} {}", oper.getName(), action, article != null ? article.getTitle() : "NULL");
	}

}
