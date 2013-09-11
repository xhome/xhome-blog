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
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagUserPermission;
import org.xhome.xblog.core.dao.TagUserPermissionDAO;
import org.xhome.xblog.core.listener.TagUserPermissionManageListener;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:53:33 PM
 * @describe 
 */
@Service
public class TagUserPermissionServiceImpl implements TagUserPermissionService {
	
	@Autowired(required = false)
	private TagUserPermissionDAO	tagUserPermissionDAO;
	@Autowired(required = false)
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<TagUserPermissionManageListener> tagUserPermissionManageListeners;
	
	private Logger	logger;
	
	public TagUserPermissionServiceImpl() {
		logger = LoggerFactory.getLogger(TagUserPermissionService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addTagUserPermission(User oper, TagUserPermission tagUserPermission) {
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.ADD, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.ADD, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.ADD, Status.BLOCKED, tagUserPermission);
			return Status.BLOCKED;
		}
		
		if (tagUserPermissionDAO.isTagUserPermissionExists(tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add tag {}[{}] permission {} for user {}[{}], but it's already exists", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.ADD, null, Status.EXISTS, oper);
			this.afterTagUserPermissionManage(oper, Action.ADD, Status.EXISTS, tagUserPermission);
			return Status.EXISTS;
		}
		
		tagUserPermission.setStatus(Status.OK);
		tagUserPermission.setVersion((short)0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		tagUserPermission.setCreated(t);
		tagUserPermission.setModified(t);
		
		short r = tagUserPermissionDAO.addTagUserPermission(tagUserPermission) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("fail to add tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.ADD, tagUserPermission.getId(), r, oper);
		this.afterTagUserPermissionManage(oper, Action.ADD, r, tagUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateTagUserPermission(User oper, TagUserPermission tagUserPermission) {
		long id = tagUserPermission.getId();
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.UPDATE, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.UPDATE, Status.BLOCKED, tagUserPermission);
			return Status.BLOCKED;
		}
		
		TagUserPermission old = tagUserPermissionDAO.queryTagUserPermission(id);
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}] permission {} for user {}[{}], but it's not exists", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterTagUserPermissionManage(oper, Action.UPDATE, Status.NOT_EXISTS, tagUserPermission);
			return Status.NOT_EXISTS;
		}
		
		if (!old.getVersion().equals(tagUserPermission.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}] permission {} for user {}[{}], but version not match", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			this.afterTagUserPermissionManage(oper, Action.UPDATE, Status.VERSION_NOT_MATCH, tagUserPermission);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UPDATE, id, status, oper);
			this.afterTagUserPermissionManage(oper, Action.UPDATE, Status.EXISTS, tagUserPermission);
			return status;
		}
		
		tagUserPermission.setOwner(old.getOwner());
		tagUserPermission.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		tagUserPermission.setModified(t);
		
		short r  = tagUserPermissionDAO.updateTagUserPermission(tagUserPermission) == 1 ? Status.SUCCESS : Status.ERROR;
		if (r == Status.SUCCESS) {
			tagUserPermission.incrementVersion();
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("fail to update tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.UPDATE, id, r, oper);
		this.afterTagUserPermissionManage(oper, Action.UPDATE, r, tagUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockTagUserPermission(User oper, TagUserPermission tagUserPermission) {
		long id = tagUserPermission.getId();
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.LOCK, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.LOCK, Status.BLOCKED, tagUserPermission);
			return Status.BLOCKED;
		}
		
		short r = tagUserPermissionDAO.lockTagUserPermission(tagUserPermission) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("fail to lock tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.LOCK, id, r, oper);
		this.afterTagUserPermissionManage(oper, Action.LOCK, r, tagUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockTagUserPermission(User oper, TagUserPermission tagUserPermission) {
		long id = tagUserPermission.getId();
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.UNLOCK, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to unlock tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.UNLOCK, Status.BLOCKED, tagUserPermission);
			return Status.BLOCKED;
		}
		
		short r = tagUserPermissionDAO.unlockTagUserPermission(tagUserPermission) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("fail to unlock tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.UNLOCK, id, r, oper);
		this.afterTagUserPermissionManage(oper, Action.UNLOCK, r, tagUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeTagUserPermission(User oper, TagUserPermission tagUserPermission) {
		long id = tagUserPermission.getId();
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.REMOVE, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.REMOVE, Status.BLOCKED, tagUserPermission);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (tagUserPermissionDAO.isTagUserPermissionRemoveable(tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			}
			tagUserPermissionDAO.removeTagUserPermission(tagUserPermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("tag {}[{}] permission {} for user {}[{}] isn't removeable", tagName, tagId, permission, userName, userId);
			}
			r = Status.NO_REMOVE;
		}
		
		this.logManage(mstr, Action.REMOVE, id, r, oper);
		this.afterTagUserPermissionManage(oper, Action.REMOVE, r, tagUserPermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteTagUserPermission(User oper, TagUserPermission tagUserPermission) {
		long id = tagUserPermission.getId();
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.DELETE, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.DELETE, Status.BLOCKED, tagUserPermission);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (tagUserPermissionDAO.isTagUserPermissionDeleteable(tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			}
			tagUserPermissionDAO.deleteTagUserPermission(tagUserPermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("tag {}[{}] permission {} for user {}[{}] isn't deleteable", tagName, tagId, permission, userName, userId);
			}
			r = Status.NO_DELETE;
		}

		this.logManage(mstr, Action.DELETE, id, r, oper);
		this.afterTagUserPermissionManage(oper, Action.DELETE, r, tagUserPermission);
		return r;
	}
	
	@Override
	public boolean isTagUserPermissionExists(User oper, TagUserPermission tagUserPermission) {
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.IS_EXISTS, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge exists of tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.IS_EXISTS, Status.BLOCKED, tagUserPermission);
			return false;
		}
		
		boolean e = tagUserPermissionDAO.isTagUserPermissionExists(tagUserPermission);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("not exists of tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			}
		}
		
		this.logManage(mstr, Action.IS_EXISTS, tagUserPermission.getId(), Status.SUCCESS, oper);
		this.afterTagUserPermissionManage(oper, Action.IS_EXISTS, Status.SUCCESS, tagUserPermission);
		return e;
	}
	
	@Override
	public boolean isTagUserPermissionUpdateable(User oper, TagUserPermission tagUserPermission) {
		long id = tagUserPermission.getId();
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.IS_UPDATEABLE, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge updateable of tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_UPDATEABLE, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, tagUserPermission);
			return false;
		}
		
		boolean e = tagUserPermissionDAO.isTagUserPermissionUpdateable(tagUserPermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] permission {} for user {}[{}] is updateable", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("tag {}[{}] permission {} for user {}[{}] isn't updateable", tagName, tagId, permission, userName, userId);
			}
		}
		
		this.logManage(mstr, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterTagUserPermissionManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, tagUserPermission);
		return e;
	}
	
	@Override
	public boolean isTagUserPermissionLocked(User oper, TagUserPermission tagUserPermission) {
		long id = tagUserPermission.getId();
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.IS_LOCKED, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge locked of tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.IS_LOCKED, Status.BLOCKED, tagUserPermission);
			return false;
		}
		
		boolean e = tagUserPermissionDAO.isTagUserPermissionLocked(tagUserPermission);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] permission {} for user {}[{}] is locked", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("tag {}[{}] permission {} for user {}[{}] isn't locked", tagName, tagId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterTagUserPermissionManage(oper, Action.IS_LOCKED, Status.SUCCESS, tagUserPermission);
		return e;
	}
	
	@Override
	public boolean isTagUserPermissionRemoveable(User oper, TagUserPermission tagUserPermission) {
		long id = tagUserPermission.getId();
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.IS_REMOVEABLE, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge removeable of tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_REMOVEABLE, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, tagUserPermission);
			return false;
		}
		
		boolean e = tagUserPermissionDAO.isTagUserPermissionRemoveable(tagUserPermission);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] permission {} for user {}[{}] is removeable", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("tag {}[{}] permission {} for user {}[{}] isn't removeable", tagName, tagId, permission, userName, userId);
			}
		}
		
		this.logManage(mstr, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterTagUserPermissionManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, tagUserPermission);
		return e;
	}
	
	@Override
	public boolean isTagUserPermissionDeleteable(User oper, TagUserPermission tagUserPermission) {
		long id = tagUserPermission.getId();
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
		String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
		int permission = tagUserPermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
		
		if (!this.beforeTagUserPermissionManage(oper, Action.IS_DELETEABLE, tagUserPermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge deleteable of tag {}[{}] permission {} for user {}[{}], but it's blocked", tagName, tagId, permission, userName, userId);
			}
			
			this.logManage(mstr, Action.IS_DELETEABLE, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, tagUserPermission);
			return false;
		}
		
		boolean e = tagUserPermissionDAO.isTagUserPermissionDeleteable(tagUserPermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] permission {} for user {}[{}] is deleteable", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("tag {}[{}] permission {} for user {}[{}] isn't deleteable", tagName, tagId, permission, userName, userId);
			}
		}

		this.logManage(mstr, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterTagUserPermissionManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, tagUserPermission);
		return e;
	}
	
	@Override
	public TagUserPermission getTagUserPermission(User oper, long id) {
		if (!this.beforeTagUserPermissionManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get tag user permission of id {}, but it's blocked", id);
			}
			
			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}
		
		TagUserPermission tagUserPermission = tagUserPermissionDAO.queryTagUserPermission(id);
		
		String mstr = null;
		if (logger.isDebugEnabled()) {
			if (tagUserPermission != null) {
				Tag tag = tagUserPermission.getTag();
				User user = tagUserPermission.getUser();
				Long tagId = tag != null ? tag.getId() : null, userId = user != null ? user.getId() : null;
				String tagName = tag != null ? tag.getName() : "", userName = user != null ? user.getName() : "";
				int permission = tagUserPermission.getPermission();
				mstr = tagName + "(" + tagId + ")" + ", " + userName + "(" + userId + ")";
				logger.debug("get tag {}[{}] permission {} for user {}[{}]", tagName, tagId, permission, userName, userId);
			} else {
				logger.debug("tag user permission of id {} is not exists", id);
			}
		}
		
		this.logManage(mstr, Action.GET, id, Status.SUCCESS, oper);
		this.afterTagUserPermissionManage(oper, Action.GET, Status.SUCCESS, tagUserPermission, id);
		return tagUserPermission;
	}
	
	@Override
	public List<TagUserPermission> getTagUserPermissions(User oper) {
		return getTagUserPermissions(oper, null);
	}
	
	@Override
	public List<TagUserPermission> getTagUserPermissions(User oper, QueryBase query) {
		if (!this.beforeTagUserPermissionManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query tag user permissions, but it's blocked");
			}
			
			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
		List<TagUserPermission> results = tagUserPermissionDAO.queryTagUserPermissions(query);
		if (query != null) {
			query.setResults(results);
			long total = tagUserPermissionDAO.countTagUserPermissions(query);
			query.setTotalRow(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query tag user permissions with parameters {}", query.getParameters());
			} else {
				logger.debug("query tag user permissions");
			}
		}
		
		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterTagUserPermissionManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return results;
	}
	
	@Override
	public long countTagUserPermissions(User oper) {
		return countTagUserPermissions(oper, null);
	}
	
	@Override
	public long countTagUserPermissions(User oper, QueryBase query) {
		if (!this.beforeTagUserPermissionManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count tag user permissions, but it's blocked");
			}
			
			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterTagUserPermissionManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = tagUserPermissionDAO.countTagUserPermissions(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count tag user permissions with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count tag user permissions of {}", c);
			}
		}
		
		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterTagUserPermissionManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private void logManage(String content, Short action, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, ManageLog.TYPE_TAG_USER_PERMISSION, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	private boolean beforeTagUserPermissionManage(User oper, short action, TagUserPermission tagUserPermission, Object ...args) {
		if (tagUserPermissionManageListeners != null) {
			for (TagUserPermissionManageListener listener : tagUserPermissionManageListeners) {
				if (!listener.beforeTagUserPermissionManage(oper, action, tagUserPermission, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterTagUserPermissionManage(User oper, short action, short result, TagUserPermission tagUserPermission, Object ...args) {
		if (tagUserPermissionManageListeners != null) {
			for (TagUserPermissionManageListener listener : tagUserPermissionManageListeners) {
				listener.afterTagUserPermissionManage(oper, action, result, tagUserPermission, args);
			}
		}
	}
	
	public void setTagUserPermissionDAO(TagUserPermissionDAO tagUserPermissionDAO) {
		this.tagUserPermissionDAO = tagUserPermissionDAO;
	}
	
	public TagUserPermissionDAO getTagUserPermissionDAO() {
		return this.tagUserPermissionDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}
	
	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setTagUserPermissionManageListeners(List<TagUserPermissionManageListener> tagUserPermissionManageListeners) {
		this.tagUserPermissionManageListeners = tagUserPermissionManageListeners;
	}

	public List<TagUserPermissionManageListener> getTagUserPermissionManageListeners() {
		return tagUserPermissionManageListeners;
	}
	
	public void registerTagUserPermissionManageListener(TagUserPermissionManageListener tagUserPermissionManageListener) {
		if (tagUserPermissionManageListeners == null) {
			tagUserPermissionManageListeners = new ArrayList<TagUserPermissionManageListener>();
		}
		tagUserPermissionManageListeners.add(tagUserPermissionManageListener);
	}
	
}
