package org.xhome.xblog.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhome.xauth.User;
import org.xhome.xblog.Record;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:45:00 PM
 * @describe
 */
public class TestRecordManageListener implements RecordManageListener {

	private Logger logger = LoggerFactory
			.getLogger(TestRecordManageListener.class);

	@Override
	public boolean beforeRecordManage(User oper, short action, Record record,
			Object... args) {
		logger.debug("TEST BEFORE RECORD MANAGE LISTENER {} {} ",
				oper.getName(), action);
		return true;
	}

	@Override
	public void afterRecordManage(User oper, short action, short result,
			Record record, Object... args) {
		logger.debug("TEST AFTER RECORD MANAGE LISTENER {} {}", oper.getName(),
				action);
	}

}
