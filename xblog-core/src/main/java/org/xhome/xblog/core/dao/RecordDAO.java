package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Record;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 201311:34:54 PM
 * @describe 
 */
public interface RecordDAO {
	
	public int addRecord(Record record);
	
	public List<Record> queryRecords(QueryBase query);
	
	public long countRecords(QueryBase query);
	
}
