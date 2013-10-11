package org.xhome.xblog.web.action;

import java.util.List;

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
import org.xhome.http.util.RequestUtils;
import org.xhome.validator.CommonValidator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.Article;
import org.xhome.xblog.BlogException;
import org.xhome.xblog.Record;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.service.ArticleService;
import org.xhome.xblog.core.service.RecordService;
import org.xhome.xblog.web.util.ValidatorUtils;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Oct 7, 201310:34:02 PM
 * @describe 
 */
@Controller
public class ArticleAction {

	@Autowired(required = false)
	private ArticleService articleService;
	@Autowired(required = false)
	private RecordService recordService;
	private Logger logger = LoggerFactory.getLogger(ArticleAction.class);
	private CommonValidator		commonValidator 	= new CommonValidator();
	private	ValidatorMapping	validatorMapping	= ValidatorMapping.getInstance();
	
	public final static String	RM_ARTICLE_ADD			= "xblog/article/add.do";
	public final static String	RM_ARTICLE_UPDATE		= "xblog/article/update.do";
	public final static String	RM_ARTICLE_LOCK			= "xblog/article/lock.do";
	public final static String	RM_ARTICLE_UNLOCK		= "xblog/article/unlock.do";
	public final static String	RM_ARTICLE_REMOVE		= "xblog/article/remove.do";
	public final static String	RM_ARTICLE_DELETE		= "xblog/article/delete.do";
	
	public final static String	RM_ARTICLE_EXISTS		= "xblog/article/exists.do";
	public final static String	RM_ARTICLE_UPDATEABLE	= "xblog/article/updateable.do";
	public final static String	RM_ARTICLE_LOCKED		= "xblog/article/locked.do";
	public final static String	RM_ARTICLE_REMOVEABLE	= "xblog/article/removeable.do";
	public final static String	RM_ARTICLE_DELETEABLE	= "xblog/article/deleteable.do";
	public final static String	RM_ARTICLE_GET			= "xblog/article/get.do";
	public final static String	RM_ARTICLE_QUERY		= "xblog/article/query.do";
	public final static String	RM_ARTICLE_COUNT		= "xblog/article/count.do";

	public final static String	RM_ARTICLE_TAG_ADD			= "xblog/article/tag/add.do";
	public final static String	RM_ARTICLE_TAG_LOCK			= "xblog/article/tag/lock.do";
	public final static String	RM_ARTICLE_TAG_UNLOCK		= "xblog/article/tag/unlock.do";
	public final static String	RM_ARTICLE_TAG_REMOVE		= "xblog/article/tag/remove.do";
	public final static String	RM_ARTICLE_TAG_DELETE		= "xblog/article/tag/delete.do";
	
	public final static String	RM_ARTICLE_TAG_EXISTS		= "xblog/article/tag/exists.do";
	public final static String	RM_ARTICLE_TAG_UPDATEABLE	= "xblog/article/tag/updateable.do";
	public final static String	RM_ARTICLE_TAG_LOCKED		= "xblog/article/tag/locked.do";
	public final static String	RM_ARTICLE_TAG_REMOVEABLE	= "xblog/article/tag/removeable.do";
	public final static String	RM_ARTICLE_TAG_DELETEABLE	= "xblog/article/tag/deleteable.do";
	
	@InitBinder
	public void initBinder(HttpServletRequest request, WebDataBinder binder) {
		String uri = request.getRequestURI();
		commonValidator.setValidators(validatorMapping.getValidatorByUri(uri));
		binder.setValidator(commonValidator);
		if (logger.isDebugEnabled()) {
			logger.debug("init binder for {}", uri);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_ADD, method = RequestMethod.POST)
	public Object addArticle(@Validated @RequestAttribute("article") Article article, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setOwner(request, article);
			AuthUtils.setModifier(request, article);
			status = (short) articleService.addArticle(user, article);
			if (status == Status.SUCCESS) {
				msg = "添加文章" + article.getTitle() + "成功";
				r = new Result(status, msg, article);
			} else {
				msg = "添加文章" + article.getTitle() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_UPDATE, method = RequestMethod.POST)
	public Object updateArticle(@Validated Article article, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, article);
			status = (short) articleService.updateArticle(user, article);
			if (status == Status.SUCCESS) {
				msg = "更新文章[" + article.getId() + "]" + article.getTitle() + "成功";
				r = new Result(status, msg, article);
			} else {
				msg = "更新文章[" + article.getId() + "]" + article.getTitle() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_LOCK, method = RequestMethod.POST)
	public Object lockArticle(@Validated Article article, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, article);
			status = (short) articleService.lockArticle(user, article);
			if (status == Status.SUCCESS) {
				msg = "锁定文章[" + article.getId() + "]" + article.getTitle() + "成功";
				r = new Result(status, msg, article);
			} else {
				msg = "锁定文章[" + article.getId() + "]" + article.getTitle() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_UNLOCK, method = RequestMethod.POST)
	public Object unlockArticle(@Validated Article article, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, article);
			status = (short) articleService.unlockArticle(user, article);
			if (status == Status.SUCCESS) {
				msg = "解锁文章[" + article.getId() + "]" + article.getTitle() + "成功";
				r = new Result(status, msg, article);
			} else {
				msg = "解锁文章[" + article.getId() + "]" + article.getTitle() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_REMOVE, method = RequestMethod.POST)
	public Object removeArticle(@Validated Article article, BindingResult result, HttpServletRequest request) {
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
			AuthUtils.setModifier(request, article);
			status = (short) articleService.removeArticle(user, article);
			if (status == Status.SUCCESS) {
				msg = "移除文章[" + article.getId() + "]" + article.getTitle() + "成功";
				r = new Result(status, msg, article);
			} else {
				msg = "移除文章[" + article.getId() + "]" + article.getTitle() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_DELETE, method = RequestMethod.POST)
	public Object deleteArticle(@Validated Article article, BindingResult result, HttpServletRequest request) {
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
			status = (short) articleService.deleteArticle(user, article);
			if (status == Status.SUCCESS) {
				msg = "删除文章[" + article.getId() + "]" + article.getTitle() + "成功";
				r = new Result(status, msg, article);
			} else {
				msg = "删除文章[" + article.getId() + "]" + article.getTitle() + "失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_UPDATEABLE, method = RequestMethod.GET)
	public Object isArticleUpdateable(@Validated Article article, BindingResult result, HttpServletRequest request) {
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
			boolean is = articleService.isArticleUpdateable(user, article);
			if (is) {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "不可以更新";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_LOCKED, method = RequestMethod.GET)
	public Object isArticleLocked(@Validated Article article, BindingResult result, HttpServletRequest request) {
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
			boolean is = articleService.isArticleLocked(user, article);
			if (is) {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "未被锁定";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_REMOVEABLE, method = RequestMethod.GET)
	public Object isArticleRemoveable(@Validated Article article, BindingResult result, HttpServletRequest request) {
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
			boolean is = articleService.isArticleRemoveable(user, article);
			if (is) {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "不可以移除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_DELETEABLE, method = RequestMethod.GET)
	public Object isArticleDeleteable(@Validated Article article, BindingResult result, HttpServletRequest request) {
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
			boolean is = articleService.isArticleDeleteable(user, article);
			if (is) {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "不可以删除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_GET, method = RequestMethod.GET)
	public Object getArticle(@RequestParam(value = "id") Long id, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		Article article = articleService.getArticle(user, id);
		
		String msg = null;
		short status = Status.SUCCESS;
		
		if (article != null) {
			msg = "文章[" + id + "]" + article.getTitle() + "查询成功";
		} else {
			status = Status.ERROR;
			msg = "文章查询失败";
		}
		Result r = new Result(status, msg, article);
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		Record record = new Record(article, user,
				RequestUtils.getRequestAddress(request),
				RequestUtils.getRequestAgent(request),
				request.getHeader("User-Agent"));
		recordService.logRecord(record);
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_QUERY, method = RequestMethod.GET)
	public Object getArticles(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("文章{}按条件{}查询文章信息", uname, query.getParameters());
			} else {
				query = new QueryBase();
				logger.info("文章{}查询文章信息", uname);
			}
		}
		articleService.getArticles(user, query);
		
		String msg = "条件查询文章信息";
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, query);

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_COUNT, method = RequestMethod.GET)
	public Object countArticles(QueryBase query, HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		String uname = user.getName();
		
		if (logger.isInfoEnabled()) {
			if (query != null) {
				logger.info("文章{}按条件{}统计文章信息", uname, query.getParameters());
			} else {
				logger.info("文章{}统计文章信息", uname);
			}
		}
		long count = articleService.countArticles(user, query);
		
		String msg = "条件统计文章信息，共" + count;
		short status = Status.SUCCESS;
		
		Result r = new Result(status, msg, count);

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_ADD, method = RequestMethod.POST)
	public Object addArticleTag(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setOwner(request, article);
			AuthUtils.setModifier(request, article);
			try {
				status = (short) articleService.addArticleTag(cuser, article, article.getTags());
				if (status == Status.SUCCESS) {
					msg = "添加文章" + article.getTitle() + "标签成功";
				} else {
					msg = "添加文章" + article.getTitle() + "标签失败";
				}
			} catch (BlogException e) {
				status = e.getStatus();
				msg = e.getMessage();
			}
			r = new Result(status, msg, article);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_LOCK, method = RequestMethod.POST)
	public Object lockArticleTag(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, article);
			status = (short) articleService.lockArticleTag(cuser, article, article.getTags());
			if (status == Status.SUCCESS) {
				msg = "锁定文章[" + article.getId() + "]" + article.getTitle() + "标签成功";
				r = new Result(status, msg, article);
			} else {
				msg = "锁定文章[" + article.getId() + "]" + article.getTitle() + "标签失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_UNLOCK, method = RequestMethod.POST)
	public Object unlockArticleTag(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, article);
			status = (short) articleService.unlockArticleTag(cuser, article, article.getTags());
			if (status == Status.SUCCESS) {
				msg = "解锁文章[" + article.getId() + "]" + article.getTitle() + "标签成功";
				r = new Result(status, msg, article);
			} else {
				msg = "解锁文章[" + article.getId() + "]" + article.getTitle() + "标签失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_REMOVE, method = RequestMethod.POST)
	public Object removeArticleTag(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			AuthUtils.setModifier(request, article);
			status = (short) articleService.removeArticleTag(cuser, article, article.getTags());
			if (status == Status.SUCCESS) {
				msg = "移除文章[" + article.getId() + "]" + article.getTitle() + "标签成功";
				r = new Result(status, msg, article);
			} else {
				msg = "移除文章[" + article.getId() + "]" + article.getTitle() + "标签失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_DELETE, method = RequestMethod.POST)
	public Object deleteArticleTag(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = 0;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			status = (short) articleService.deleteArticleTag(cuser, article, article.getTags());
			if (status == Status.SUCCESS) {
				msg = "删除文章[" + article.getId() + "]" + article.getTitle() + "标签成功";
				r = new Result(status, msg, article);
			} else {
				msg = "删除文章[" + article.getId() + "]" + article.getTitle() + "标签失败";
				r = new Result(status, msg);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_EXISTS, method = RequestMethod.GET)
	public Object isArticleTagExists(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Tag> tags = article.getTags();
			Tag tag = tags.get(0);
			boolean is = articleService.hasArticleTag(cuser, article, tag);
			if (is) {
				msg = "查询文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "存在";
				r = new Result(status, msg, true);
			} else {
				msg = "查询文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "不存在";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_UPDATEABLE, method = RequestMethod.GET)
	public Object isArticleTagUpdateable(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Tag> tags = article.getTags();
			Tag tag = tags.get(0);
			boolean is = articleService.isArticleTagUpdateable(cuser, article, tag);
			if (is) {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "可以更新";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "不可以更新";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_LOCKED, method = RequestMethod.GET)
	public Object isArticleTagLocked(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Tag> tags = article.getTags();
			Tag tag = tags.get(0);
			boolean is = articleService.isArticleTagLocked(cuser, article, tag);
			if (is) {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "已被锁定";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "未被锁定";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_REMOVEABLE, method = RequestMethod.GET)
	public Object isArticleTagRemoveable(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Tag> tags = article.getTags();
			Tag tag = tags.get(0);
			boolean is = articleService.isArticleTagRemoveable(cuser, article, tag);
			if (is) {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "可以移除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "不可以移除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}
	
	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_TAG_DELETEABLE, method = RequestMethod.GET)
	public Object isArticleTagDeleteable(@Validated Article article, BindingResult result, HttpServletRequest request) {
		Object r = null;
		short status = Status.SUCCESS;
		String msg = null;
		
		User cuser = AuthUtils.getCurrentUser(request);
		if (result.hasErrors()) {
			Result re = ValidatorUtils.errorResult(result);
			status = re.getStatus();
			msg = re.getMessage();
			r = re;
		} else {
			List<Tag> tags = article.getTags();
			Tag tag = tags.get(0);
			boolean is = articleService.isArticleTagDeleteable(cuser, article, tag);
			if (is) {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "可以删除";
				r = new Result(status, msg, true);
			} else {
				msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签[" + tag.getId() + "]" + tag.getName() + "不可以删除";
				r = new Result(status, msg, false);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}
		
		return r;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public ArticleService getArticleService() {
		return articleService;
	}
	
}
