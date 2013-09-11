package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.CategoryUserPermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:52:54 PM
 * @describe 
 */
public class CategoryUserPermissionManageAdapter implements CategoryUserPermissionManageListener {

	public boolean beforeCategoryUserPermissionManage(User oper, short action, CategoryUserPermission categoryUserPermission, Object ...args) {
		return true;
	}
	
	public void afterCategoryUserPermissionManage(User oper, short action, short result, CategoryUserPermission categoryUserPermission, Object ...args) {}
	
}
