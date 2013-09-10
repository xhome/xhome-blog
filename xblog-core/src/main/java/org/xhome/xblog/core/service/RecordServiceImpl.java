package org.xhome.xblog.core.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Action;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.ManageLog;
import org.xhome.xblog.Record;
import org.xhome.xblog.core.dao.RecordDAO;
import org.xhome.xblog.core.listener.RecordManageListener;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:37:18 PM
 * @describe 
 */
@Service
public class RecordServiceImpl implements RecordService {
	
	@Autowired(required = false)
	private RecordDAO	recordDAO;
	@Autowired(required = false)
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<RecordManageListener> recordManageListeners;
	
	private Logger		logger;
	
	public RecordServiceImpl() {
		logger = LoggerFactory.getLogger(RecordService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int logRecord(Record record) {
		record.setCreated(new Timestamp(System.currentTimeMillis()));
		int r = recordDAO.addRecord(record) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add record for {}", record.getUser().getName());
			} else {
				logger.debug("fail to add record for {}", record.getUser().getName());
			}
		}
		
		return r;
	}
	
	@Override
	public List<Record> getRecords(User oper) {
		return getRecords(oper, null);
	}
	
	@Override
	public List<Record> getRecords(User oper, QueryBase query) {
		if (!this.beforeRecordManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query records, but it's blocked");
			}
			
			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterRecordManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
		List<Record> records = recordDAO.queryRecords(query);
		if (query != null) {
			query.setResults(records);
			long total = recordDAO.countRecords(query);
			query.setTotalRow(total);
		}
		
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query user records with parameters {}", query.getParameters());
			} else {
				logger.debug("query user records");
			}
		}

		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterRecordManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return records;
	}
	
	@Override
	public long countRecords(User oper) {
		return countRecords(oper, null);
	}
	
	@Override
	public long countRecords(User oper, QueryBase query) {
		if (!this.beforeRecordManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count records, but it's blocked");
			}
			
			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterRecordManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = recordDAO.countRecords(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count user records with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count user records of {}", c);
			}
		}

		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterRecordManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private void logManage(String content, Short action, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, ManageLog.TYPE_RECORD, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	private boolean beforeRecordManage(User oper, short action, Record record, Object ...args) {
		if (recordManageListeners != null) {
			for (RecordManageListener listener : recordManageListeners) {
				if (!listener.beforeRecordManage(oper, action, record, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterRecordManage(User oper, short action, short result, Record record, Object ...args) {
		if (recordManageListeners != null) {
			for (RecordManageListener listener : recordManageListeners) {
				listener.afterRecordManage(oper, action, result, record, args);
			}
		}
	}
	
	public void setRecordDAO(RecordDAO recordDAO) {
		this.recordDAO = recordDAO;
	}
	
	public RecordDAO getRecordDAO() {
		return this.recordDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}
	
	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}
	
	public void setRecordManageListeners(List<RecordManageListener> recordManageListeners) {
		this.recordManageListeners = recordManageListeners;
	}

	public List<RecordManageListener> getRecordManageListeners() {
		return recordManageListeners;
	}
	
	public void registerRecordManageListener(RecordManageListener recordManageListener) {
		if (recordManageListeners == null) {
			recordManageListeners = new ArrayList<RecordManageListener>();
		}
		recordManageListeners.add(recordManageListener);
	}
	
}
