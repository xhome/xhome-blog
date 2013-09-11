package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.TagRolePermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:58:52 PM
 * @describe 
 */
public class TestTagRolePermissionManageListener implements TagRolePermissionManageListener {

	private Logger logger = LoggerFactory.getLogger(TestTagRolePermissionManageListener.class);
	
	@Override
	public boolean beforeTagRolePermissionManage(User oper, short action, TagRolePermission tagRolePermission,
			Object... args) {
		logger.debug("TEST BEFORE TAG ROLE PERMISSION MANAGE LISTENER {} {} {}", oper.getName(), action, tagRolePermission != null ? tagRolePermission.getId() : "NULL");
		return true;
	}

	@Override
	public void afterTagRolePermissionManage(User oper, short action, short result,
			TagRolePermission tagRolePermission, Object... args) {
		logger.debug("TEST AFTER TAG ROLE PERMISSION MANAGE LISTENER {} {} {}", oper.getName(), action, tagRolePermission != null ? tagRolePermission.getId() : "NULL");
	}

}
