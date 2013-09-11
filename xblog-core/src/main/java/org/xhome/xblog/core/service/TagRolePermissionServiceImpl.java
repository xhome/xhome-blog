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
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xblog.ManageLog;
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagRolePermission;
import org.xhome.xblog.core.dao.TagRolePermissionDAO;
import org.xhome.xblog.core.listener.TagRolePermissionManageListener;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:53:33 PM
 * @describe 
 */
@Service
public class TagRolePermissionServiceImpl implements TagRolePermissionService {
	
	@Autowired(required = false)
	private TagRolePermissionDAO	tagRolePermissionDAO;
	@Autowired(required = false)
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<TagRolePermissionManageListener> tagRolePermissionManageListeners;
	
	private Logger	logger;
	
	public TagRolePermissionServiceImpl() {
		logger = LoggerFactory.getLogger(TagRolePermissionService.class);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addTagRolePermission(User oper, TagRolePermission tagRolePermission) {
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.ADD, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.ADD, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.ADD, Status.BLOCKED, tagRolePermission);
			return Status.BLOCKED;
		}
		
		if (tagRolePermissionDAO.isTagRolePermissionExists(tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add tag {}[{}] permission {} for role {}[{}], but it's already exists", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.ADD, null, Status.EXISTS, oper);
			this.afterTagRolePermissionManage(oper, Action.ADD, Status.EXISTS, tagRolePermission);
			return Status.EXISTS;
		}
		
		tagRolePermission.setStatus(Status.OK);
		tagRolePermission.setVersion((short)0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		tagRolePermission.setCreated(t);
		tagRolePermission.setModified(t);
		
		short r = tagRolePermissionDAO.addTagRolePermission(tagRolePermission) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("fail to add tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.ADD, tagRolePermission.getId(), r, oper);
		this.afterTagRolePermissionManage(oper, Action.ADD, r, tagRolePermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateTagRolePermission(User oper, TagRolePermission tagRolePermission) {
		long id = tagRolePermission.getId();
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.UPDATE, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.UPDATE, Status.BLOCKED, tagRolePermission);
			return Status.BLOCKED;
		}
		
		TagRolePermission old = tagRolePermissionDAO.queryTagRolePermission(id);
		
		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}] permission {} for role {}[{}], but it's not exists", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterTagRolePermissionManage(oper, Action.UPDATE, Status.NOT_EXISTS, tagRolePermission);
			return Status.NOT_EXISTS;
		}
		
		if (!old.getVersion().equals(tagRolePermission.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}] permission {} for role {}[{}], but version not match", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.UPDATE, id, Status.VERSION_NOT_MATCH, oper);
			this.afterTagRolePermissionManage(oper, Action.UPDATE, Status.VERSION_NOT_MATCH, tagRolePermission);
			return Status.VERSION_NOT_MATCH;
		}
		
		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.UPDATE, id, status, oper);
			this.afterTagRolePermissionManage(oper, Action.UPDATE, Status.EXISTS, tagRolePermission);
			return status;
		}
		
		tagRolePermission.setOwner(old.getOwner());
		tagRolePermission.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		tagRolePermission.setModified(t);
		
		short r  = tagRolePermissionDAO.updateTagRolePermission(tagRolePermission) == 1 ? Status.SUCCESS : Status.ERROR;
		if (r == Status.SUCCESS) {
			tagRolePermission.incrementVersion();
		}
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("fail to update tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.UPDATE, id, r, oper);
		this.afterTagRolePermissionManage(oper, Action.UPDATE, r, tagRolePermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockTagRolePermission(User oper, TagRolePermission tagRolePermission) {
		long id = tagRolePermission.getId();
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.LOCK, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.LOCK, Status.BLOCKED, tagRolePermission);
			return Status.BLOCKED;
		}
		
		short r = tagRolePermissionDAO.lockTagRolePermission(tagRolePermission) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("fail to lock tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.LOCK, id, r, oper);
		this.afterTagRolePermissionManage(oper, Action.LOCK, r, tagRolePermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockTagRolePermission(User oper, TagRolePermission tagRolePermission) {
		long id = tagRolePermission.getId();
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.UNLOCK, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to unlock tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.UNLOCK, Status.BLOCKED, tagRolePermission);
			return Status.BLOCKED;
		}
		
		short r = tagRolePermissionDAO.unlockTagRolePermission(tagRolePermission) == 1 ? Status.SUCCESS : Status.ERROR;
		
		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("fail to unlock tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.UNLOCK, id, r, oper);
		this.afterTagRolePermissionManage(oper, Action.UNLOCK, r, tagRolePermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeTagRolePermission(User oper, TagRolePermission tagRolePermission) {
		long id = tagRolePermission.getId();
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.REMOVE, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.REMOVE, Status.BLOCKED, tagRolePermission);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (tagRolePermissionDAO.isTagRolePermissionRemoveable(tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			}
			tagRolePermissionDAO.removeTagRolePermission(tagRolePermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("tag {}[{}] permission {} for role {}[{}] isn't removeable", tagName, tagId, permission, roleName, roleId);
			}
			r = Status.NO_REMOVE;
		}
		
		this.logManage(mstr, Action.REMOVE, id, r, oper);
		this.afterTagRolePermissionManage(oper, Action.REMOVE, r, tagRolePermission);
		return r;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteTagRolePermission(User oper, TagRolePermission tagRolePermission) {
		long id = tagRolePermission.getId();
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.DELETE, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.DELETE, Status.BLOCKED, tagRolePermission);
			return Status.BLOCKED;
		}
		
		short r = Status.SUCCESS;
		if (tagRolePermissionDAO.isTagRolePermissionDeleteable(tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			}
			tagRolePermissionDAO.deleteTagRolePermission(tagRolePermission);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("tag {}[{}] permission {} for role {}[{}] isn't deleteable", tagName, tagId, permission, roleName, roleId);
			}
			r = Status.NO_DELETE;
		}

		this.logManage(mstr, Action.DELETE, id, r, oper);
		this.afterTagRolePermissionManage(oper, Action.DELETE, r, tagRolePermission);
		return r;
	}
	
	@Override
	public boolean isTagRolePermissionExists(User oper, TagRolePermission tagRolePermission) {
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.IS_EXISTS, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge exists of tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.IS_EXISTS, Status.BLOCKED, tagRolePermission);
			return false;
		}
		
		boolean e = tagRolePermissionDAO.isTagRolePermissionExists(tagRolePermission);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("not exists of tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			}
		}
		
		this.logManage(mstr, Action.IS_EXISTS, tagRolePermission.getId(), Status.SUCCESS, oper);
		this.afterTagRolePermissionManage(oper, Action.IS_EXISTS, Status.SUCCESS, tagRolePermission);
		return e;
	}
	
	@Override
	public boolean isTagRolePermissionUpdateable(User oper, TagRolePermission tagRolePermission) {
		long id = tagRolePermission.getId();
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.IS_UPDATEABLE, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge updateable of tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.IS_UPDATEABLE, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, tagRolePermission);
			return false;
		}
		
		boolean e = tagRolePermissionDAO.isTagRolePermissionUpdateable(tagRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] permission {} for role {}[{}] is updateable", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("tag {}[{}] permission {} for role {}[{}] isn't updateable", tagName, tagId, permission, roleName, roleId);
			}
		}
		
		this.logManage(mstr, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterTagRolePermissionManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, tagRolePermission);
		return e;
	}
	
	@Override
	public boolean isTagRolePermissionLocked(User oper, TagRolePermission tagRolePermission) {
		long id = tagRolePermission.getId();
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.IS_LOCKED, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge locked of tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.IS_LOCKED, Status.BLOCKED, tagRolePermission);
			return false;
		}
		
		boolean e = tagRolePermissionDAO.isTagRolePermissionLocked(tagRolePermission);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] permission {} for role {}[{}] is locked", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("tag {}[{}] permission {} for role {}[{}] isn't locked", tagName, tagId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterTagRolePermissionManage(oper, Action.IS_LOCKED, Status.SUCCESS, tagRolePermission);
		return e;
	}
	
	@Override
	public boolean isTagRolePermissionRemoveable(User oper, TagRolePermission tagRolePermission) {
		long id = tagRolePermission.getId();
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.IS_REMOVEABLE, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge removeable of tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.IS_REMOVEABLE, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, tagRolePermission);
			return false;
		}
		
		boolean e = tagRolePermissionDAO.isTagRolePermissionRemoveable(tagRolePermission);
		
		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] permission {} for role {}[{}] is removeable", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("tag {}[{}] permission {} for role {}[{}] isn't removeable", tagName, tagId, permission, roleName, roleId);
			}
		}
		
		this.logManage(mstr, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterTagRolePermissionManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, tagRolePermission);
		return e;
	}
	
	@Override
	public boolean isTagRolePermissionDeleteable(User oper, TagRolePermission tagRolePermission) {
		long id = tagRolePermission.getId();
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
		String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
		int permission = tagRolePermission.getPermission();
		String mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
		
		if (!this.beforeTagRolePermissionManage(oper, Action.IS_DELETEABLE, tagRolePermission)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge deleteable of tag {}[{}] permission {} for role {}[{}], but it's blocked", tagName, tagId, permission, roleName, roleId);
			}
			
			this.logManage(mstr, Action.IS_DELETEABLE, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, tagRolePermission);
			return false;
		}
		
		boolean e = tagRolePermissionDAO.isTagRolePermissionDeleteable(tagRolePermission);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] permission {} for role {}[{}] is deleteable", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("tag {}[{}] permission {} for role {}[{}] isn't deleteable", tagName, tagId, permission, roleName, roleId);
			}
		}

		this.logManage(mstr, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterTagRolePermissionManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, tagRolePermission);
		return e;
	}
	
	@Override
	public TagRolePermission getTagRolePermission(User oper, long id) {
		if (!this.beforeTagRolePermissionManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get tag role permission of id {}, but it's blocked", id);
			}
			
			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}
		
		TagRolePermission tagRolePermission = tagRolePermissionDAO.queryTagRolePermission(id);
		
		String mstr = null;
		if (logger.isDebugEnabled()) {
			if (tagRolePermission != null) {
				Tag tag = tagRolePermission.getTag();
				Role role = tagRolePermission.getRole();
				Long tagId = tag != null ? tag.getId() : null, roleId = role != null ? role.getId() : null;
				String tagName = tag != null ? tag.getName() : "", roleName = role != null ? role.getName() : null;;
				int permission = tagRolePermission.getPermission();
				mstr = tagName + "(" + tagId + ")" + ", " + roleName + "(" + roleId + ")";
				logger.debug("get tag {}[{}] permission {} for role {}[{}]", tagName, tagId, permission, roleName, roleId);
			} else {
				logger.debug("tag role permission of id {} is not exists", id);
			}
		}
		
		this.logManage(mstr, Action.GET, id, Status.SUCCESS, oper);
		this.afterTagRolePermissionManage(oper, Action.GET, Status.SUCCESS, tagRolePermission, id);
		return tagRolePermission;
	}
	
	@Override
	public List<TagRolePermission> getTagRolePermissions(User oper) {
		return getTagRolePermissions(oper, null);
	}
	
	@Override
	public List<TagRolePermission> getTagRolePermissions(User oper, QueryBase query) {
		if (!this.beforeTagRolePermissionManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query tag role permissions, but it's blocked");
			}
			
			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}
		
		List<TagRolePermission> results = tagRolePermissionDAO.queryTagRolePermissions(query);
		if (query != null) {
			query.setResults(results);
			long total = tagRolePermissionDAO.countTagRolePermissions(query);
			query.setTotalRow(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query tag role permissions with parameters {}", query.getParameters());
			} else {
				logger.debug("query tag role permissions");
			}
		}
		
		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterTagRolePermissionManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return results;
	}
	
	@Override
	public long countTagRolePermissions(User oper) {
		return countTagRolePermissions(oper, null);
	}
	
	@Override
	public long countTagRolePermissions(User oper, QueryBase query) {
		if (!this.beforeTagRolePermissionManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count tag role permissions, but it's blocked");
			}
			
			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterTagRolePermissionManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}
		
		long c = tagRolePermissionDAO.countTagRolePermissions(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count tag role permissions with parameters {} of {}", query.getParameters(), c);
			} else {
				logger.debug("count tag role permissions of {}", c);
			}
		}
		
		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterTagRolePermissionManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}
	
	private void logManage(String content, Short action, Long obj, Short status, User oper) {
		ManageLog manageLog = new ManageLog(content, action, ManageLog.TYPE_TAG_ROLE_PERMISSION, obj, oper == null ? null : oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}
	
	private boolean beforeTagRolePermissionManage(User oper, short action, TagRolePermission tagRolePermission, Object ...args) {
		if (tagRolePermissionManageListeners != null) {
			for (TagRolePermissionManageListener listener : tagRolePermissionManageListeners) {
				if (!listener.beforeTagRolePermissionManage(oper, action, tagRolePermission, args)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void afterTagRolePermissionManage(User oper, short action, short result, TagRolePermission tagRolePermission, Object ...args) {
		if (tagRolePermissionManageListeners != null) {
			for (TagRolePermissionManageListener listener : tagRolePermissionManageListeners) {
				listener.afterTagRolePermissionManage(oper, action, result, tagRolePermission, args);
			}
		}
	}
	
	public void setTagRolePermissionDAO(TagRolePermissionDAO tagRolePermissionDAO) {
		this.tagRolePermissionDAO = tagRolePermissionDAO;
	}
	
	public TagRolePermissionDAO getTagRolePermissionDAO() {
		return this.tagRolePermissionDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}
	
	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setTagRolePermissionManageListeners(List<TagRolePermissionManageListener> tagRolePermissionManageListeners) {
		this.tagRolePermissionManageListeners = tagRolePermissionManageListeners;
	}

	public List<TagRolePermissionManageListener> getTagRolePermissionManageListeners() {
		return tagRolePermissionManageListeners;
	}
	
	public void registerTagRolePermissionManageListener(TagRolePermissionManageListener tagRolePermissionManageListener) {
		if (tagRolePermissionManageListeners == null) {
			tagRolePermissionManageListeners = new ArrayList<TagRolePermissionManageListener>();
		}
		tagRolePermissionManageListeners.add(tagRolePermissionManageListener);
	}
	
}
