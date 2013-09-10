package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Record;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:40:00 PM
 * @describe 
 */
public class RecordManageAdapter implements RecordManageListener {

	public boolean beforeRecordManage(User oper, short action, Record record, Object ...args) {
		return true;
	}
	
	public void afterRecordManage(User oper, short action, short result, Record record, Object ...args) {}
	
}
