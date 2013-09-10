package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.ManageLog;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:31:21 PM
 * @describe 
 */
public class ManageLogManageAdapter implements ManageLogManageListener {

	public boolean beforeManageLogManage(User oper, short action, ManageLog manageLog, Object ...args) {
		return true;
	}
	
	public void afterManageLogManage(User oper, short action, short result, ManageLog manageLog, Object ...args) {}
	
}
