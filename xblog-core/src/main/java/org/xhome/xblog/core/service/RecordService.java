package org.xhome.xblog.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Record;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:22:50 PM
 * @describe 
 */
@Service
public interface RecordService {
	
	public int logRecord(Record record);
	
	public List<Record> getRecords(User oper);
	
	public List<Record> getRecords(User oper, QueryBase query);
	
	public long countRecords(User oper);
	
	public long countRecords(User oper, QueryBase query);

}
