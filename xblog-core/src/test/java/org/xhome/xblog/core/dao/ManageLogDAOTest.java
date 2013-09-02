package org.xhome.xblog.core.dao;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.xhome.common.constant.Action;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.ManageLog;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project auth
 * @author jhat
 * @email cpf624@126.com
 * @date Feb 1, 201310:17:16 AM
 */
public class ManageLogDAOTest extends AbstractTest {
	
	private ManageLogDAO	manageLogDAO;
	
	public ManageLogDAOTest() {
		manageLogDAO = context.getBean(ManageLogDAO.class);
	}
	
	@Test
	public void testAddManageLog() {
		ManageLog manageLog = new ManageLog(Action.ADD, ManageLog.TYPE_CATAGORY, 10L, 1L);
		manageLog.setCreated(new Timestamp(System.currentTimeMillis()));
		manageLogDAO.addManageLog(manageLog);
	}
	
	@Test
	public void testQueryManageLog() {
		QueryBase query = new QueryBase();
		List<ManageLog> manageLogs = manageLogDAO.queryManageLogs(query);
		printManageLog(manageLogs);
	}
	
}
