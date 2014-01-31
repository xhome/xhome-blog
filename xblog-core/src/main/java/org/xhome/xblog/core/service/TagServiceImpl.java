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
import org.xhome.xauth.ManageLog;
import org.xhome.xauth.User;
import org.xhome.xauth.core.service.ManageLogService;
import org.xhome.xblog.ManageLogType;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.dao.TagDAO;
import org.xhome.xblog.core.listener.TagManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:33 PM
 * @describe
 */
@Service
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDAO tagDAO;
	@Autowired
	private ManageLogService manageLogService;
	@Autowired(required = false)
	private List<TagManageListener> tagManageListeners;

	private Logger logger;

	public TagServiceImpl() {
		logger = LoggerFactory.getLogger(TagService.class);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int addTag(User oper, Tag tag) {
		String name = tag.getName();

		if (!this.beforeTagManage(oper, Action.ADD, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add tag {}, but it's blocked", name);
			}

			this.logManage(name, Action.ADD, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.ADD, Status.BLOCKED, tag);
			return Status.BLOCKED;
		}

		if (tagDAO.isTagExists(tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to add tag {}, but it's already exists", name);
			}

			this.logManage(name, Action.ADD, null, Status.EXISTS, oper);
			this.afterTagManage(oper, Action.ADD, Status.EXISTS, tag);
			return Status.EXISTS;
		}

		tag.setStatus(Status.OK);
		tag.setVersion((short) 0);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		tag.setCreated(t);
		tag.setModified(t);

		short r = tagDAO.addTag(tag) == 1 ? Status.SUCCESS : Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to add tag {}", name);
			} else {
				logger.debug("fail to add tag {}", name);
			}
		}

		this.logManage(name, Action.ADD, tag.getId(), r, oper);
		this.afterTagManage(oper, Action.ADD, r, tag);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int updateTag(User oper, Tag tag) {
		String name = tag.getName();
		Long id = tag.getId();

		if (!this.beforeTagManage(oper, Action.UPDATE, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.UPDATE, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.UPDATE, Status.BLOCKED, tag);
			return Status.BLOCKED;
		}

		Tag old = tagDAO.queryTagById(id);

		if (old == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}], but it's not exists",
						name, id);
			}

			this.logManage(name, Action.UPDATE, id, Status.NOT_EXISTS, oper);
			this.afterTagManage(oper, Action.UPDATE, Status.NOT_EXISTS, tag);
			return Status.NOT_EXISTS;
		}

		String oldName = old.getName();

		if (!old.getVersion().equals(tag.getVersion())) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}], but version not match",
						oldName, id);
			}

			this.logManage(oldName, Action.UPDATE, id,
					Status.VERSION_NOT_MATCH, oper);
			this.afterTagManage(oper, Action.UPDATE, Status.VERSION_NOT_MATCH,
					tag);
			return Status.VERSION_NOT_MATCH;
		}

		short status = old.getStatus();
		if (status == Status.NO_UPDATE || status == Status.LOCK) {
			if (logger.isDebugEnabled()) {
				logger.debug("it's not allowed to update tag {}[{}]", oldName,
						id);
			}

			this.logManage(oldName, Action.UPDATE, id, status, oper);
			this.afterTagManage(oper, Action.UPDATE, Status.EXISTS, tag);
			return status;
		}

		if (!oldName.equals(name) && tagDAO.isTagExists(tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to update tag {}[{}] to {}, but it's exists",
						oldName, id, name);
			}
			this.logManage(oldName, Action.UPDATE, id, Status.EXISTS, oper);
			this.afterTagManage(oper, Action.UPDATE, Status.EXISTS, tag);
			return Status.EXISTS;
		}

		tag.setOwner(old.getOwner());
		tag.setCreated(old.getCreated());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		tag.setModified(t);

		short r = tagDAO.updateTag(tag) == 1 ? Status.SUCCESS : Status.ERROR;
		if (r == Status.SUCCESS) {
			tag.incrementVersion();
		}

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to update tag {}[{}]", oldName, id);
			} else {
				logger.debug("fail to update tag {}[{}]", oldName, id);
			}
		}

		this.logManage(oldName, Action.UPDATE, id, r, oper);
		this.afterTagManage(oper, Action.UPDATE, r, tag);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int lockTag(User oper, Tag tag) {
		String name = tag.getName();
		Long id = tag.getId();

		if (!this.beforeTagManage(oper, Action.LOCK, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to lock tag {}[{}], but it's blocked", name,
						id);
			}

			this.logManage(name, Action.LOCK, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.LOCK, Status.BLOCKED, tag);
			return Status.BLOCKED;
		}

		short r = tagDAO.lockTag(tag) == 1 ? Status.SUCCESS : Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to lock tag {}[{}]", name, id);
			} else {
				logger.debug("fail to lock tag {}[{}]", name, id);
			}
		}

		this.logManage(name, Action.LOCK, id, r, oper);
		this.afterTagManage(oper, Action.LOCK, r, tag);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int unlockTag(User oper, Tag tag) {
		String name = tag.getName();
		Long id = tag.getId();

		if (!this.beforeTagManage(oper, Action.UNLOCK, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to unlock tag {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.UNLOCK, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.UNLOCK, Status.BLOCKED, tag);
			return Status.BLOCKED;
		}

		short r = tagDAO.unlockTag(tag) == 1 ? Status.SUCCESS : Status.ERROR;

		if (logger.isDebugEnabled()) {
			if (r == Status.SUCCESS) {
				logger.debug("success to unlock tag {}[{}]", name, id);
			} else {
				logger.debug("fail to unlock tag {}[{}]", name, id);
			}
		}

		this.logManage(name, Action.UNLOCK, id, r, oper);
		this.afterTagManage(oper, Action.UNLOCK, r, tag);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeTag(User oper, Tag tag) {
		String name = tag.getName();
		Long id = tag.getId();

		if (!this.beforeTagManage(oper, Action.REMOVE, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to remove tag {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.REMOVE, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.REMOVE, Status.BLOCKED, tag);
			return Status.BLOCKED;
		}

		short r = Status.SUCCESS;
		if (tagDAO.isTagRemoveable(tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove tag {}[{}]", name, id);
			}
			tagDAO.removeTag(tag);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("tag {}[{}] isn't removeable", name, id);
			}
			r = Status.NO_REMOVE;
		}

		this.logManage(name, Action.REMOVE, id, r, oper);
		this.afterTagManage(oper, Action.REMOVE, r, tag);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int removeTags(User oper, List<Tag> tags) {
		int r = Status.SUCCESS;
		for (Tag tag : tags) {
			r = this.removeTag(oper, tag);
			if (r != Status.SUCCESS) {
				throw new RuntimeException("fail to remove tag [" + tag.getId()
						+ "]" + tag.getName());
			}
		}
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteTag(User oper, Tag tag) {
		String name = tag.getName();
		Long id = tag.getId();

		if (!this.beforeTagManage(oper, Action.DELETE, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to delete tag {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.DELETE, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.DELETE, Status.BLOCKED, tag);
			return Status.BLOCKED;
		}

		short r = Status.SUCCESS;
		if (tagDAO.isTagDeleteable(tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("delete tag {}[{}]", name, id);
			}
			tagDAO.deleteTag(tag);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("tag {}[{}] isn't deleteable", name, id);
			}
			r = Status.NO_DELETE;
		}

		this.logManage(name, Action.DELETE, id, r, oper);
		this.afterTagManage(oper, Action.DELETE, r, tag);
		return r;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public int deleteTags(User oper, List<Tag> tags) {
		int r = Status.SUCCESS;
		for (Tag tag : tags) {
			r = this.deleteTag(oper, tag);
			if (r != Status.SUCCESS) {
				throw new RuntimeException("fail to delete tag [" + tag.getId()
						+ "]" + tag.getName());
			}
		}
		return r;
	}

	@Override
	public boolean isTagExists(User oper, Tag tag) {
		String name = tag.getName();

		if (!this.beforeTagManage(oper, Action.IS_EXISTS, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to juge exists of tag {}, but it's blocked",
						name);
			}

			this.logManage(name, Action.IS_EXISTS, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.IS_EXISTS, Status.BLOCKED, tag);
			return false;
		}

		boolean e = tagDAO.isTagExists(tag);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("exists of tag {}", name);
			} else {
				logger.debug("not exists of tag {}", name);
			}
		}

		this.logManage(name, Action.IS_EXISTS, tag.getId(), Status.SUCCESS,
				oper);
		this.afterTagManage(oper, Action.IS_EXISTS, Status.SUCCESS, tag);
		return e;
	}

	@Override
	public boolean isTagUpdateable(User oper, Tag tag) {
		String name = tag.getName();
		Long id = tag.getId();

		if (!this.beforeTagManage(oper, Action.IS_UPDATEABLE, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge updateable of tag {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.IS_UPDATEABLE, null, Status.BLOCKED,
					oper);
			this.afterTagManage(oper, Action.IS_UPDATEABLE, Status.BLOCKED, tag);
			return false;
		}

		boolean e = tagDAO.isTagUpdateable(tag);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] is updateable", name, id);
			} else {
				logger.debug("tag {}[{}] isn't updateable", name, id);
			}
		}

		this.logManage(name, Action.IS_UPDATEABLE, id, Status.SUCCESS, oper);
		this.afterTagManage(oper, Action.IS_UPDATEABLE, Status.SUCCESS, tag);
		return e;
	}

	@Override
	public boolean isTagLocked(User oper, Tag tag) {
		String name = tag.getName();
		Long id = tag.getId();

		if (!this.beforeTagManage(oper, Action.IS_LOCKED, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge locked of tag {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.IS_LOCKED, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.IS_LOCKED, Status.BLOCKED, tag);
			return false;
		}

		boolean e = tagDAO.isTagLocked(tag);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] is locked", name, id);
			} else {
				logger.debug("tag {}[{}] isn't locked", name, id);
			}
		}

		this.logManage(name, Action.IS_LOCKED, id, Status.SUCCESS, oper);
		this.afterTagManage(oper, Action.IS_LOCKED, Status.SUCCESS, tag);
		return e;
	}

	@Override
	public boolean isTagRemoveable(User oper, Tag tag) {
		String name = tag.getName();
		Long id = tag.getId();

		if (!this.beforeTagManage(oper, Action.IS_REMOVEABLE, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge removeable of tag {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.IS_REMOVEABLE, null, Status.BLOCKED,
					oper);
			this.afterTagManage(oper, Action.IS_REMOVEABLE, Status.BLOCKED, tag);
			return false;
		}

		boolean e = tagDAO.isTagRemoveable(tag);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] is removeable", name, id);
			} else {
				logger.debug("tag {}[{}] isn't removeable", name, id);
			}
		}

		this.logManage(name, Action.IS_REMOVEABLE, id, Status.SUCCESS, oper);
		this.afterTagManage(oper, Action.IS_REMOVEABLE, Status.SUCCESS, tag);
		return e;
	}

	@Override
	public boolean isTagDeleteable(User oper, Tag tag) {
		String name = tag.getName();
		Long id = tag.getId();

		if (!this.beforeTagManage(oper, Action.IS_DELETEABLE, tag)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"try to juge deleteable of tag {}[{}], but it's blocked",
						name, id);
			}

			this.logManage(name, Action.IS_DELETEABLE, null, Status.BLOCKED,
					oper);
			this.afterTagManage(oper, Action.IS_DELETEABLE, Status.BLOCKED, tag);
			return false;
		}

		boolean e = tagDAO.isTagDeleteable(tag);

		if (logger.isDebugEnabled()) {
			if (e) {
				logger.debug("tag {}[{}] is deleteable", name, id);
			} else {
				logger.debug("tag {}[{}] isn't deleteable", name, id);
			}
		}

		this.logManage(name, Action.IS_DELETEABLE, id, Status.SUCCESS, oper);
		this.afterTagManage(oper, Action.IS_DELETEABLE, Status.SUCCESS, tag);
		return e;
	}

	@Override
	public Tag getTag(User oper, long id) {
		if (!this.beforeTagManage(oper, Action.GET, null, id)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get tag of id {}, but it's blocked", id);
			}

			this.logManage("" + id, Action.GET, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.GET, Status.BLOCKED, null, id);
			return null;
		}

		Tag tag = tagDAO.queryTagById(id);

		String name = null;
		if (logger.isDebugEnabled()) {
			if (tag != null) {
				name = tag.getName();
				logger.debug("get tag {}[{}]", name, id);
			} else {
				logger.debug("tag of id {} is not exists", id);
			}
		}

		this.logManage(name, Action.GET, id, Status.SUCCESS, oper);
		this.afterTagManage(oper, Action.GET, Status.SUCCESS, tag, id);
		return tag;
	}

	@Override
	public Tag getTag(User oper, String name) {
		if (!this.beforeTagManage(oper, Action.GET, null, name)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to get tag {}, but it's blocked", name);
			}

			this.logManage(name, Action.GET, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.GET, Status.BLOCKED, null, name);
			return null;
		}

		Tag tag = tagDAO.queryTagByName(name);

		Long id = null;
		if (logger.isDebugEnabled()) {
			if (tag != null) {
				id = tag.getId();
				logger.debug("get tag {}[{}]", name, id);
			} else {
				logger.debug("tag {} is not exists", name);
			}
		}

		this.logManage(name, Action.GET, id, Status.SUCCESS, oper);
		this.afterTagManage(oper, Action.GET, Status.SUCCESS, tag, name);
		return tag;
	}

	@Override
	public List<Tag> getTags(User oper, QueryBase query) {
		if (!this.beforeTagManage(oper, Action.QUERY, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to query tags, but it's blocked");
			}

			this.logManage(null, Action.QUERY, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.QUERY, Status.BLOCKED, null, query);
			return null;
		}

		List<Tag> results = tagDAO.queryTags(query);
		if (query != null) {
			query.setResults(results);
			long total = tagDAO.countTags(query);
			query.setTotal(total);
		}

		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("query tags with parameters {}",
						query.getParameters());
			} else {
				logger.debug("query tags");
			}
		}

		this.logManage(null, Action.QUERY, null, Status.SUCCESS, oper);
		this.afterTagManage(oper, Action.QUERY, Status.SUCCESS, null, query);
		return results;
	}

	@Override
	public long countTags(User oper, QueryBase query) {
		if (!this.beforeTagManage(oper, Action.COUNT, null, query)) {
			if (logger.isDebugEnabled()) {
				logger.debug("try to count tags, but it's blocked");
			}

			this.logManage(null, Action.COUNT, null, Status.BLOCKED, oper);
			this.afterTagManage(oper, Action.COUNT, Status.BLOCKED, null, query);
			return -1;
		}

		long c = tagDAO.countTags(query);
		if (logger.isDebugEnabled()) {
			if (query != null) {
				logger.debug("count tags with parameters {} of {}",
						query.getParameters(), c);
			} else {
				logger.debug("count tags of {}", c);
			}
		}

		this.logManage(null, Action.COUNT, null, Status.SUCCESS, oper);
		this.afterTagManage(oper, Action.COUNT, Status.SUCCESS, null, query);
		return c;
	}

	private void logManage(String content, Short action, Long obj,
			Short status, User oper) {
		ManageLog manageLog = new ManageLog(ManageLog.MANAGE_LOG_XBLOG,
				content, action, ManageLogType.TAG, obj, oper == null ? null
						: oper.getId());
		manageLog.setStatus(status);
		manageLogService.logManage(manageLog);
	}

	private boolean beforeTagManage(User oper, short action, Tag tag,
			Object... args) {
		if (tagManageListeners != null) {
			for (TagManageListener listener : tagManageListeners) {
				if (!listener.beforeTagManage(oper, action, tag, args)) {
					return false;
				}
			}
		}
		return true;
	}

	private void afterTagManage(User oper, short action, short result, Tag tag,
			Object... args) {
		if (tagManageListeners != null) {
			for (TagManageListener listener : tagManageListeners) {
				listener.afterTagManage(oper, action, result, tag, args);
			}
		}
	}

	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}

	public TagDAO getTagDAO() {
		return this.tagDAO;
	}

	public void setManageLogService(ManageLogService manageLogService) {
		this.manageLogService = manageLogService;
	}

	public ManageLogService getManageLogService() {
		return this.manageLogService;
	}

	public void setTagManageListeners(List<TagManageListener> tagManageListeners) {
		this.tagManageListeners = tagManageListeners;
	}

	public List<TagManageListener> getTagManageListeners() {
		return tagManageListeners;
	}

	public void registerTagManageListener(TagManageListener tagManageListener) {
		if (tagManageListeners == null) {
			tagManageListeners = new ArrayList<TagManageListener>();
		}
		tagManageListeners.add(tagManageListener);
	}

}
