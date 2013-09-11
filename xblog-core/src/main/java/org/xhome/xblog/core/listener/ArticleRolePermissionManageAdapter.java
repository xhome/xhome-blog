package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.ArticleUserPermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:52:54 PM
 * @describe 
 */
public class ArticleRolePermissionManageAdapter implements ArticleUserPermissionManageListener {

	public boolean beforeArticleUserPermissionManage(User oper, short action, ArticleUserPermission articleUserPermission, Object ...args) {
		return true;
	}
	
	public void afterArticleUserPermissionManage(User oper, short action, short result, ArticleUserPermission articleUserPermission, Object ...args) {}
	
}
