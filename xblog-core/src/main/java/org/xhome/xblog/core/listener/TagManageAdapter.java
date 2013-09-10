package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Tag;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:52:54 PM
 * @describe 
 */
public class TagManageAdapter implements TagManageListener {

	public boolean beforeTagManage(User oper, short action, Tag tag, Object ...args) {
		return true;
	}
	
	public void afterTagManage(User oper, short action, short result, Tag tag, Object ...args) {}
	
}
