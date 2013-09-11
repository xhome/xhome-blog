package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.TagUserPermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:58:52 PM
 * @describe 
 */
public class TestTagUserPermissionManageListener implements TagUserPermissionManageListener {

	private Logger logger = LoggerFactory.getLogger(TestTagUserPermissionManageListener.class);
	
	@Override
	public boolean beforeTagUserPermissionManage(User oper, short action, TagUserPermission tagUserPermission,
			Object... args) {
		logger.debug("TEST BEFORE TAG USER PERMISSION MANAGE LISTENER {} {} {}", oper.getName(), action, tagUserPermission != null ? tagUserPermission.getId() : "NULL");
		return true;
	}

	@Override
	public void afterTagUserPermissionManage(User oper, short action, short result,
			TagUserPermission tagUserPermission, Object... args) {
		logger.debug("TEST AFTER TAG USER PERMISSION MANAGE LISTENER {} {} {}", oper.getName(), action, tagUserPermission != null ? tagUserPermission.getId() : "NULL");
	}

}
