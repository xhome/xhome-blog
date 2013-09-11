package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.TagUserPermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:52:54 PM
 * @describe 
 */
public class TagRolePermissionManageAdapter implements TagUserPermissionManageListener {

	public boolean beforeTagUserPermissionManage(User oper, short action, TagUserPermission tagUserPermission, Object ...args) {
		return true;
	}
	
	public void afterTagUserPermissionManage(User oper, short action, short result, TagUserPermission tagUserPermission, Object ...args) {}
	
}
