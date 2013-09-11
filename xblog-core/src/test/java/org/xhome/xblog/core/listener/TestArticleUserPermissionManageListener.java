package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.ArticleUserPermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:58:52 PM
 * @describe 
 */
public class TestArticleUserPermissionManageListener implements ArticleUserPermissionManageListener {

	private Logger logger = LoggerFactory.getLogger(TestArticleUserPermissionManageListener.class);
	
	@Override
	public boolean beforeArticleUserPermissionManage(User oper, short action, ArticleUserPermission articleUserPermission,
			Object... args) {
		logger.debug("TEST BEFORE ARTICLE USER PERMISSION MANAGE LISTENER {} {} {}", oper.getName(), action, articleUserPermission != null ? articleUserPermission.getId() : "NULL");
		return true;
	}

	@Override
	public void afterArticleUserPermissionManage(User oper, short action, short result,
			ArticleUserPermission articleUserPermission, Object... args) {
		logger.debug("TEST AFTER ARTICLE USER PERMISSION MANAGE LISTENER {} {} {}", oper.getName(), action, articleUserPermission != null ? articleUserPermission.getId() : "NULL");
	}

}
