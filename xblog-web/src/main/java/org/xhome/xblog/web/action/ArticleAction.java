package org.xhome.xblog.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.spring.mvc.extend.bind.annotation.RequestAttribute;
import org.xhome.web.action.AbstractAction;
import org.xhome.web.response.CommonResult;
import org.xhome.web.util.RequestUtils;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.Article;
import org.xhome.xblog.BlogException;
import org.xhome.xblog.Record;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.service.ArticleService;
import org.xhome.xblog.core.service.RecordService;

/**
 * @project xblog-web
 * @author jhat
 * @email cpf624@126.com
 * @date Oct 7, 201310:34:02 PM
 * @describe
 */
@Controller
public class ArticleAction extends AbstractAction {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private RecordService recordService;

	public final static String RM_ARTICLE_ADD = "xblog/article/add";
	public final static String RM_ARTICLE_UPDATE = "xblog/article/update";
	public final static String RM_ARTICLE_LOCK = "xblog/article/lock";
	public final static String RM_ARTICLE_UNLOCK = "xblog/article/unlock";
	public final static String RM_ARTICLE_REMOVE = "xblog/article/remove";
	public final static String RM_ARTICLE_DELETE = "xblog/article/delete";

	public final static String RM_ARTICLE_EXISTS = "xblog/article/exists";
	public final static String RM_ARTICLE_UPDATEABLE = "xblog/article/updateable";
	public final static String RM_ARTICLE_LOCKED = "xblog/article/locked";
	public final static String RM_ARTICLE_REMOVEABLE = "xblog/article/removeable";
	public final static String RM_ARTICLE_DELETEABLE = "xblog/article/deleteable";
	public final static String RM_ARTICLE_GET = "xblog/article/get";
	public final static String RM_ARTICLE_QUERY = "xblog/article/query";
	public final static String RM_ARTICLE_COUNT = "xblog/article/count";

	public final static String RM_ARTICLE_TAG_ADD = "xblog/article/tag/add";
	public final static String RM_ARTICLE_TAG_LOCK = "xblog/article/tag/lock";
	public final static String RM_ARTICLE_TAG_UNLOCK = "xblog/article/tag/unlock";
	public final static String RM_ARTICLE_TAG_REMOVE = "xblog/article/tag/remove";
	public final static String RM_ARTICLE_TAG_DELETE = "xblog/article/tag/delete";

	public final static String RM_ARTICLE_TAG_EXISTS = "xblog/article/tag/exists";
	public final static String RM_ARTICLE_TAG_UPDATEABLE = "xblog/article/tag/updateable";
	public final static String RM_ARTICLE_TAG_LOCKED = "xblog/article/tag/locked";
	public final static String RM_ARTICLE_TAG_REMOVEABLE = "xblog/article/tag/removeable";
	public final static String RM_ARTICLE_TAG_DELETEABLE = "xblog/article/tag/deleteable";

	@RequestMapping(value = RM_ARTICLE_ADD, method = RequestMethod.POST)
	public Object addArticle(
			@Validated @RequestAttribute("article") Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, article);
		AuthUtils.setModifier(request, article);
		status = (short) articleService.addArticle(user, article);
		if (status == Status.SUCCESS) {
			msg = "添加文章" + article.getTitle() + "成功";
		} else {
			msg = "添加文章" + article.getTitle() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_UPDATE, method = RequestMethod.POST)
	public Object updateArticle(@Validated Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, article);
		status = (short) articleService.updateArticle(user, article);
		if (status == Status.SUCCESS) {
			msg = "更新文章[" + article.getId() + "]" + article.getTitle() + "成功";
		} else {
			msg = "更新文章[" + article.getId() + "]" + article.getTitle() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_LOCK, method = RequestMethod.POST)
	public Object lockArticle(@Validated Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, article);
		status = (short) articleService.lockArticle(user, article);
		if (status == Status.SUCCESS) {
			msg = "锁定文章[" + article.getId() + "]" + article.getTitle() + "成功";
		} else {
			msg = "锁定文章[" + article.getId() + "]" + article.getTitle() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_UNLOCK, method = RequestMethod.POST)
	public Object unlockArticle(@Validated Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, article);
		status = (short) articleService.unlockArticle(user, article);
		if (status == Status.SUCCESS) {
			msg = "解锁文章[" + article.getId() + "]" + article.getTitle() + "成功";
		} else {
			msg = "解锁文章[" + article.getId() + "]" + article.getTitle() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_REMOVE, method = RequestMethod.POST)
	public Object removeArticle(@Validated Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, article);
		status = (short) articleService.removeArticle(user, article);
		if (status == Status.SUCCESS) {
			msg = "移除文章[" + article.getId() + "]" + article.getTitle() + "成功";
		} else {
			msg = "移除文章[" + article.getId() + "]" + article.getTitle() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_DELETE, method = RequestMethod.POST)
	public Object deleteArticle(@Validated Article article,
			HttpServletRequest request) {
		User user = AuthUtils.getCurrentUser(request);
		short status = (short) articleService.deleteArticle(user, article);
		String msg = null;
		if (status == Status.SUCCESS) {
			msg = "删除文章[" + article.getId() + "]" + article.getTitle() + "成功";
		} else {
			msg = "删除文章[" + article.getId() + "]" + article.getTitle() + "失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_UPDATEABLE, method = RequestMethod.GET)
	public Object isArticleUpdateable(@Validated Article article,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean updateable = articleService.isArticleUpdateable(user, article);
		if (updateable) {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle()
					+ "可以更新";
		} else {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle()
					+ "不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	@ResponseBody
	@RequestMapping(value = RM_ARTICLE_LOCKED, method = RequestMethod.GET)
	public Object isArticleLocked(@Validated Article article,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean locked = articleService.isArticleLocked(user, article);
		if (locked) {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle()
					+ "已被锁定";
		} else {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle()
					+ "未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	@RequestMapping(value = RM_ARTICLE_REMOVEABLE, method = RequestMethod.GET)
	public Object isArticleRemoveable(@Validated Article article,
			BindingResult result, HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean removeable = articleService.isArticleRemoveable(user, article);
		if (removeable) {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle()
					+ "可以移除";
		} else {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle()
					+ "不可以移除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, removeable);
	}

	@RequestMapping(value = RM_ARTICLE_DELETEABLE, method = RequestMethod.GET)
	public Object isArticleDeleteable(@Validated Article article,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User user = AuthUtils.getCurrentUser(request);
		boolean deleteable = articleService.isArticleDeleteable(user, article);
		if (deleteable) {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle()
					+ "可以删除";
		} else {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle()
					+ "不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, user.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	@RequestMapping(value = RM_ARTICLE_GET, method = RequestMethod.GET)
	public Object getArticle(@RequestParam(value = "id") Long id,
			HttpServletRequest request) {
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

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		Record record = new Record(article, user,
				RequestUtils.getRequestAddress(request),
				RequestUtils.getRequestAgent(request),
				request.getHeader("User-Agent"));
		recordService.logRecord(record);

		return new CommonResult(status, msg, article);
	}

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

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, query);
	}

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

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, uname, msg);
		}

		return new CommonResult(status, msg, count);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_ADD, method = RequestMethod.POST)
	public Object addArticleTag(@Validated Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		AuthUtils.setOwner(request, article);
		AuthUtils.setModifier(request, article);
		try {
			status = (short) articleService.addArticleTag(cuser, article,
					article.getTags());
			if (status == Status.SUCCESS) {
				msg = "添加文章" + article.getTitle() + "标签成功";
			} else {
				msg = "添加文章" + article.getTitle() + "标签失败";
			}
		} catch (BlogException e) {
			status = e.getStatus();
			msg = e.getMessage();
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_LOCK, method = RequestMethod.POST)
	public Object lockArticleTag(@Validated Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, article);
		status = (short) articleService.lockArticleTag(cuser, article,
				article.getTags());
		if (status == Status.SUCCESS) {
			msg = "锁定文章[" + article.getId() + "]" + article.getTitle() + "标签成功";
		} else {
			msg = "锁定文章[" + article.getId() + "]" + article.getTitle() + "标签失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_UNLOCK, method = RequestMethod.POST)
	public Object unlockArticleTag(@Validated Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, article);
		status = (short) articleService.unlockArticleTag(cuser, article,
				article.getTags());
		if (status == Status.SUCCESS) {
			msg = "解锁文章[" + article.getId() + "]" + article.getTitle() + "标签成功";
		} else {
			msg = "解锁文章[" + article.getId() + "]" + article.getTitle() + "标签失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_REMOVE, method = RequestMethod.POST)
	public Object removeArticleTag(@Validated Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		AuthUtils.setModifier(request, article);
		status = (short) articleService.removeArticleTag(cuser, article,
				article.getTags());
		if (status == Status.SUCCESS) {
			msg = "移除文章[" + article.getId() + "]" + article.getTitle() + "标签成功";
		} else {
			msg = "移除文章[" + article.getId() + "]" + article.getTitle() + "标签失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_DELETE, method = RequestMethod.POST)
	public Object deleteArticleTag(@Validated Article article,
			HttpServletRequest request) {
		short status = 0;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		status = (short) articleService.deleteArticleTag(cuser, article,
				article.getTags());
		if (status == Status.SUCCESS) {
			msg = "删除文章[" + article.getId() + "]" + article.getTitle() + "标签成功";
		} else {
			msg = "删除文章[" + article.getId() + "]" + article.getTitle() + "标签失败";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, article);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_EXISTS, method = RequestMethod.GET)
	public Object isArticleTagExists(@Validated Article article,
			BindingResult result, HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		List<Tag> tags = article.getTags();
		Tag tag = tags.get(0);
		boolean has = articleService.hasArticleTag(cuser, article, tag);
		if (has) {
			msg = "查询文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "存在";
		} else {
			msg = "查询文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "不存在";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, has);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_UPDATEABLE, method = RequestMethod.GET)
	public Object isArticleTagUpdateable(@Validated Article article,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		List<Tag> tags = article.getTags();
		Tag tag = tags.get(0);
		boolean updateable = articleService.isArticleTagUpdateable(cuser,
				article, tag);
		if (updateable) {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "可以更新";
		} else {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "不可以更新";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, updateable);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_LOCKED, method = RequestMethod.GET)
	public Object isArticleTagLocked(@Validated Article article,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		List<Tag> tags = article.getTags();
		Tag tag = tags.get(0);
		boolean locked = articleService.isArticleTagLocked(cuser, article, tag);
		if (locked) {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "已被锁定";
		} else {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "未被锁定";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, locked);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_REMOVEABLE, method = RequestMethod.GET)
	public Object isArticleTagRemoveable(@Validated Article article,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		List<Tag> tags = article.getTags();
		Tag tag = tags.get(0);
		boolean removeable = articleService.isArticleTagRemoveable(cuser,
				article, tag);
		if (removeable) {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "可以移除";
		} else {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "不可以移除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, removeable);
	}

	@RequestMapping(value = RM_ARTICLE_TAG_DELETEABLE, method = RequestMethod.GET)
	public Object isArticleTagDeleteable(@Validated Article article,
			HttpServletRequest request) {
		short status = Status.SUCCESS;
		String msg = null;

		User cuser = AuthUtils.getCurrentUser(request);
		List<Tag> tags = article.getTags();
		Tag tag = tags.get(0);
		boolean deleteable = articleService.isArticleTagDeleteable(cuser,
				article, tag);
		if (deleteable) {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "可以删除";
		} else {
			msg = "查询到文章[" + article.getId() + "]" + article.getTitle() + "标签["
					+ tag.getId() + "]" + tag.getName() + "不可以删除";
		}

		if (logger.isInfoEnabled()) {
			logger.info("[{}] {} {}", status, cuser.getName(), msg);
		}

		return new CommonResult(status, msg, deleteable);
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public ArticleService getArticleService() {
		return articleService;
	}

	public void setRecordService(RecordService recordService) {
		this.recordService = recordService;
	}

	public RecordService getRecordService() {
		return recordService;
	}

}
