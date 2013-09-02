package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.ManageLog;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 201311:34:46 PM
 * @describe 
 */
public interface ManageLogDAO {
	
	public int addManageLog(ManageLog manageLog);
	
	public List<ManageLog> queryManageLogs(QueryBase query);
	
	public long countManageLogs(QueryBase query);
	
}
