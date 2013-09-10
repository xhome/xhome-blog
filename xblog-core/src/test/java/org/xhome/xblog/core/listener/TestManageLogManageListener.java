package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.ManageLog;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:44:07 PM
 * @describe 
 */
public class TestManageLogManageListener implements ManageLogManageListener {

	private Logger logger = LoggerFactory.getLogger(TestManageLogManageListener.class);
	
	@Override
	public boolean beforeManageLogManage(User oper, short action, ManageLog manageLog,
			Object... args) {
		logger.debug("TEST BEFORE MANAGELOG MANAGE LISTENER {} {} ", oper.getName(), action);
		return true;
	}

	@Override
	public void afterManageLogManage(User oper, short action, short result,
			ManageLog manageLog, Object... args) {
		logger.debug("TEST AFTER MANAGELOG MANAGE LISTENER {} {}", oper.getName(), action);
	}

}
