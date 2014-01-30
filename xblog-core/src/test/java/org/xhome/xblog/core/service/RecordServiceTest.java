package org.xhome.xblog.core.service;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.xhome.common.constant.Agent;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Article;
import org.xhome.xblog.Record;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestRecordManageListener;

/**
 * @project auth
 * @author jhat
 * @email cpf624@126.com
 * @date Feb 1, 201310:49:01 AM
 */
public class RecordServiceTest extends AbstractTest {

	private RecordService recordService;

	public RecordServiceTest() {
		recordService = context.getBean(RecordServiceImpl.class);
		oper.setId(103L);

		((RecordServiceImpl) recordService)
				.registerRecordManageListener(new TestRecordManageListener());
	}

	@Test
	public void testAddRecord() {
		User user = new User("jhat");
		user.setId(2L);
		Article article = new Article("Article");
		article.setId(1L);
		Record record = new Record(article, user, "127.0.0.1", Agent.CHROME,
				"chrome 20.01 ubuntu 64bit");
		record.setCreated(new Timestamp(System.currentTimeMillis()));
		recordService.logRecord(record);
	}

	@Test
	public void testGetRecords() {
		QueryBase query = new QueryBase();
		query.addParameter("user", "jhat");
		query.addParameter("auth", "2013-");
		query.addParameter("address", "192");
		query.addParameter("type", "0");
		query.addParameter("status", "0");
		List<Record> records = recordService.getRecords(oper, query);
		printRecord(records);
	}

}
