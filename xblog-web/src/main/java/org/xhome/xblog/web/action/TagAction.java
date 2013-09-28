package org.xhome.xblog.web.action;

import javax.servlet.http.HttpServletRequest;

import org.jhat.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.http.response.Result;
import org.xhome.util.StringUtils;
import org.xhome.validator.CommonValidator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.web.util.ValidatorUtils;
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
public class TagAction {

	@Autowired(required = false)
	private TagService tagService;
	private Logger logger = LoggerFactory.getLogger(TagAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_TAG_ADD			= "xblog/tag/add.do";
	public final static String	RM_TAG_UPDATE		= "xblog/tag/update.do";
	public final static String	RM_TAG_LOCK			= "xblog/tag/lock.do";
	public final static String	RM_TAG_UNLOCK		= "xblog/tag/unlock.do";
	public final static String	RM_TAG_REMOVE		= "xblog/tag/remove.do";
	public final static String	RM_TAG_DELETE		= "xblog/tag/delete.do";
	
	public final static String	RM_TAG_EXISTS		= "xblog/tag/exists.do";
	public final static String	RM_TAG_UPDATEABLE	= "xblog/tag/updateable.do";
	public final static String	RM_TAG_LOCKED		= "xblog/tag/locked.do";
	public final static String	RM_TAG_REMOVEABLE	= "xblog/tag/removeable.do";
	public final static String	RM_TAG_DELETEABLE	= "xblog/tag/deleteable.do";
	public final static String	RM_TAG_GET			= "xblog/tag/get.do";
	public final static String	RM_TAG_QUERY		= "xblog/tag/query.do";
	public final static String	RM_TAG_COUNT		= "xblog/tag/count.do";
	
	@InitBinder
	public void initBinder(HttpServletRequest request, WebDataBinder binder) {
		String uri = request.getRequestURI();
		commonValidator.setValidators(validatorMapping.getValidatorByUri(uri));
		binder.setValidator(commonValidator);
		if (logger.isDebugEnabled()) {
			logger.debug("init binder for " + uri);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_ADD, method = RequestMethod.POST)
	public Object addTag(@Validated @RequestAttribute("tag") Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setOwner(request, tag);
			AuthUtils.setModifier(request, tag);
			status = (short) tagService.addTag(user, tag);
			if (status == Status.SUCCESS) {
				msg = "添加标签" + tag.getName() + "成功";
				r = new Result(status, msg, tag);
			} else {
				msg = "添加标签" + tag.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_UPDATE, method = RequestMethod.POST)
	public Object updateTag(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, tag);
			status = (short) tagService.updateTag(user, tag);
			if (status == Status.SUCCESS) {
				msg = "更新标签[" + tag.getId() + "]" + tag.getName() + "成功";
				r = new Result(status, msg, tag);
			} else {
				msg = "更新标签[" + tag.getId() + "]" + tag.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_LOCK, method = RequestMethod.POST)
	public Object lockTag(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, tag);
			status = (short) tagService.lockTag(user, tag);
			if (status == Status.SUCCESS) {
				msg = "锁定标签[" + tag.getId() + "]" + tag.getName() + "成功";
				r = new Result(status, msg, tag);
			} else {
				msg = "锁定标签[" + tag.getId() + "]" + tag.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_UNLOCK, method = RequestMethod.POST)
	public Object unlockTag(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, tag);
			status = (short) tagService.unlockTag(user, tag);
			if (status == Status.SUCCESS) {
				msg = "解锁标签[" + tag.getId() + "]" + tag.getName() + "成功";
				r = new Result(status, msg, tag);
			} else {
				msg = "解锁标签[" + tag.getId() + "]" + tag.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_REMOVE, method = RequestMethod.POST)
	public Object removeTag(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, tag);
			status = (short) tagService.removeTag(user, tag);
			if (status == Status.SUCCESS) {
				msg = "移除标签[" + tag.getId() + "]" + tag.getName() + "成功";
				r = new Result(status, msg, tag);
			} else {
				msg = "移除标签[" + tag.getId() + "]" + tag.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_DELETE, method = RequestMethod.POST)
	public Object deleteTag(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			status = (short) tagService.deleteTag(user, tag);
			if (status == Status.SUCCESS) {
				msg = "删除标签[" + tag.getId() + "]" + tag.getName() + "成功";
				r = new Result(status, msg, tag);
			} else {
				msg = "删除标签[" + tag.getId() + "]" + tag.getName() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_EXISTS, method = RequestMethod.GET)
	public Object isTagExists(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = tagService.isTagExists(user, tag);
			if (is) {
				msg = "查询标签" + tag.getName() + "存在";
				r = new Result(status, msg, true);
			} else {
				msg = "查询标签" + tag.getName() + "不存在";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_UPDATEABLE, method = RequestMethod.GET)
	public Object isTagUpdateable(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = tagService.isTagUpdateable(user, tag);
			if (is) {
				msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "不可以更新";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_LOCKED, method = RequestMethod.GET)
	public Object isTagLocked(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = tagService.isTagLocked(user, tag);
			if (is) {
				msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "未被锁定";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_REMOVEABLE, method = RequestMethod.GET)
	public Object isTagRemoveable(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = tagService.isTagRemoveable(user, tag);
			if (is) {
				msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "不可以移除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_DELETEABLE, method = RequestMethod.GET)
	public Object isTagDeleteable(@Validated Tag tag, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User user = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			boolean is = tagService.isTagDeleteable(user, tag);
			if (is) {
				msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到标签[" + tag.getId() + "]" + tag.getName() + "不可以删除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + user.getName() + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_GET, method = RequestMethod.GET)
	public Object getTag(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name", required = false) String name, HttpServletRequest request) {
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
		Result r = new Result(status, msg, tag);
		
		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_QUERY, method = RequestMethod.GET)
	public Object getTags(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "查询标签信息");
			} else {
				query = new QueryBase();
				logger.info("用户" + uname + "查询标签信息");
			}
		}
		tagService.getTags(user, query);
		
		String msg = "条件查询标签信息";
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_TAG_COUNT, method = RequestMethod.GET)
	public Object countTags(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("用户" + uname + "按条件" + query.getParameters() + "统计标签信息");
			} else {
				logger.info("用户" + uname + "统计标签信息");
			}
		}
		long count = tagService.countTags(user, query);
		
		String msg = "条件统计标签信息，共" + count;
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[" + status + "]" + uname + msg);
		}
		
		return r;
	}

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public TagService getTagService() {
		return tagService;
	}
	
}
