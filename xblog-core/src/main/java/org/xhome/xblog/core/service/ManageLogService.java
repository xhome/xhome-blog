package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.ManageLog;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:22:33 PM
 * @describe 
 */
@Service
public interface ManageLogService {
	
	public int logManage(ManageLog manageLog);
	
	public List<ManageLog> getManageLogs(User oper);
	
	public List<ManageLog> getManageLogs(User oper, QueryBase query);
	
	public long countManageLogs(User oper);
	
	public long countManageLogs(User oper, QueryBase query);

}
