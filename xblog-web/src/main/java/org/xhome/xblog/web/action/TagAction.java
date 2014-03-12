package org.xhome.xblog.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.xhome.util.StringUtils;
import org.xhome.web.action.AbstractAction;
import org.xhome.web.response.CommonResult;
import org.xhome.web.response.DataResult;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.service.TagService;

/**
 * @project xblog-web
 * @blogor jhat
 * @email cpf624@126.com
 * @date Aug 13, 201310:49:20 PM
 * @description
 */
@Controller
public class TagAction extends AbstractAction {

	@Autowired
	private TagService tagService;

	public final static String RM_TAG_ADD = "xblog/tag/add";
	public final static String RM_TAG_UPDATE = "xblog/tag/update";
	public final static String RM_TAG_LOCK = "xblog/tag/lock";
	public final static String RM_TAG_UNLOCK = "xblog/tag/unlock";
	public final static String RM_TAG_DELETE = "xblog/tag/delete";

	public final static String RM_TAG_EXISTS = "xblog/tag/exists";
	public final static String RM_TAG_UPDATEABLE = "xblog/tag/updateable";
	public final static String RM_TAG_LOCKED = "xblog/tag/locked";
	public final static String RM_TAG_DELETEABLE = "xblog/tag/deleteable";
	public final static String RM_TAG_GET = "xblog/tag/get";
	public final static String RM_TAG_QUERY = "xblog/tag/query";
	public final static String RM_TAG_COUNT = "xblog/tag/count";

	@RequestMapping(value = RM_TAG_ADD, method = RequestMethod.POST)
	public Object addTag(@Validated @RequestAttribute("tag") Tag tag,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, tag);
		AuthUtils.setModifier(request, tag);
		status = (short) tagService.addTag(user, tag);
		if (status == Status.SUCCESS) {
			msg = "添加标签" + tag.getName() + "成功";
		} else {
			msg = "添加标签" + tag.getName() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, tag);
	}

	@RequestMapping(value = RM_TAG_UPDATE, method = RequestMethod.POST)
	public Object updateTag(@Validated @RequestAttribute("tag") Tag tag,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, tag);
		status = (short) tagService.updateTag(user, tag);
		if (status == Status.SUCCESS) {
			msg = "更新标签[" + tag.getId() + "]" + tag.getName() + "成功";
		} else {
			msg = "更新标签[" + tag.getId() + "]" + tag.getName() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, tag);
	}

	// @RequestMapping(value = RM_TAG_LOCK, method = RequestMethod.POST)
	public Object lockTag(@Validated @RequestAttribute("tag") Tag tag,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, tag);
		status = (short) tagService.lockTag(user, tag);
		if (status == Status.SUCCESS) {
			msg = "锁定标签[" + tag.getId() + "]" + tag.getName() + "成功";
		} else {
			msg = "锁定标签[" + tag.getId() + "]" + tag.getName() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, tag);
	}

	// @RequestMapping(value = RM_TAG_UNLOCK, method = RequestMethod.POST)
	public Object unlockTag(@Validated @RequestAttribute("tag") Tag tag,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, tag);
		status = (short) tagService.unlockTag(user, tag);
		if (status == Status.SUCCESS) {
			msg = "解锁标签[" + tag.getId() + "]" + tag.getName() + "成功";
		} else {
			msg = "解锁标签[" + tag.getId() + "]" + tag.getName() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, tag);
	}

	@RequestMapping(value = RM_TAG_DELETE, method = RequestMethod.POST)
	public Object deleteTag(
			@Validated @RequestAttribute("tags") List<Tag> tags,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		for (Tag tag : tags) {
			AuthUtils.setModifier(request, tag);
		}
		try {
			status = (short) tagService.deleteTags(user, tags);
		} catch (RuntimeException e) {
			status = Status.ERROR;
		}
		if (status == Status.SUCCESS) {
			msg = "删除标签成功";
		} else {
			msg = "删除标签失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, tags);
	}

	// @RequestMapping(value = RM_TAG_EXISTS, method = RequestMethod.GET)
	public Object isTagExists(@Validated @RequestAttribute("tag") Tag tag,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean exists = tagService.isTagExists(user, tag);
		if (exists) {
			msg = "查询标签" + tag.getName() + "存在";
		} else {
			msg = "查询标签" + tag.getName() + "不存在";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, exists);
	}

	// @RequestMapping(value = RM_TAG_UPDATEABLE, method = RequestMethod.GET)
	public Object isTagUpdateable(@Validated @RequestAttribute("tag") Tag tag,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = tagService.isTagUpdateable(user, tag);
		if (updateable) {
			msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "可以更新";
		} else {
			msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	// @RequestMapping(value = RM_TAG_LOCKED, method = RequestMethod.GET)
	public Object isTagLocked(@Validated @RequestAttribute("tag") Tag tag,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = tagService.isTagLocked(user, tag);
		if (locked) {
			msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "已被锁定";
		} else {
			msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	// @RequestMapping(value = RM_TAG_DELETEABLE, method = RequestMethod.GET)
	public Object isTagDeleteable(@Validated @RequestAttribute("tag") Tag tag,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = tagService.isTagDeleteable(user, tag);
		if (deleteable) {
			msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "可以删除";
		} else {
			msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	// @RequestMapping(value = RM_TAG_GET, method = RequestMethod.GET)
	public Object getTag(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "name", required = false) String name,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		Tag tag = null;
		if (id != null) {
			logger.info("用户" + uname + "按ID[" + id + "]查询标签");
			tag = tagService.getTag(user, id);
		} else if (StringUtils.isNotEmpty(name)) {
			logger.info("用户" + uname + "按名称[" + name + "]查询标签");
			tag = tagService.getTag(user, name);
		}

		String msg = null;
		short status = Status.SUCCESS;

		if (tag != null) {
			msg = "标签[" + tag.getId() + "]" + tag.getName() + "查询成功";
		} else {
			status = Status.ERROR;
			msg = "标签查询失败";
		}

		if (logger.isInfoEnabled()) {

			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, tag);
	}

	@RequestMapping(value = RM_TAG_QUERY, method = RequestMethod.GET)
	public Object getTags(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (query == null) {
			query = new QueryBase();
		}
		if (logger.isInfoEnabled()) {
			logger.info("用户{}按条件{}查询标签信息", uname, query.getParameters());
		}
		tagService.getTags(user, query);

		String msg = "条件查询标签信息";
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new DataResult(status, msg, query);
	}

	// @RequestMapping(value = RM_TAG_COUNT, method = RequestMethod.GET)
	public Object countTags(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();

		if (query == null) {
			query = new QueryBase();
		}
		if (logger.isInfoEnabled()) {
			logger.info("用户{}按条件{}统计标签信息", uname, query.getParameters());
		}
		long count = tagService.countTags(user, query);

		String msg = "条件统计标签信息，共" + count;
		short status = Status.SUCCESS;

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public TagService getTagService() {
		return tagService;
	}

}
