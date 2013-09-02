package org.xhome.xblog.core.dao;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.xhome.common.constant.Agent;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Article;
import org.xhome.xblog.Record;
import org.xhome.xauth.User;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project auth
 * @author jhat
 * @email cpf624@126.com
 * @date Feb 1, 201310:17:16 AM
 */
public class RecordDAOTest extends AbstractTest {
	
	private RecordDAO	recordDAO;
	
	public RecordDAOTest() {
		recordDAO = context.getBean(RecordDAO.class);
	}
	
	@Test
	public void testAddRecord() {
		User user = new User("jhat");
		user.setId(2L);
		Article article = new Article("Article");
		article.setId(1L);
		Record record = new Record(article, user, "127.0.0.1", Agent.CHROME, "chrome 20.01 ubuntu 64bit");
		record.setCreated(new Timestamp(System.currentTimeMillis()));
		recordDAO.addRecord(record);
	}
	
	@Test
	public void testQueryRecord() {
		QueryBase query = new QueryBase();
		List<Record> records = recordDAO.queryRecords(query);
		printRecord(records);
	}
	
}
