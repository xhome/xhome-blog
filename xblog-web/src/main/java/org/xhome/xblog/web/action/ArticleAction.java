package org.xhome.xblog.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import org.xhome.util.StringUtils;
import org.xhome.web.action.AbstractAction;
import org.xhome.web.response.CommonResult;
import org.xhome.web.response.DataResult;
import org.xhome.web.util.RequestUtils;
import org.xhome.xauth.User;
import org.xhome.xauth.web.util.AuthUtils;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleAttachment;
import org.xhome.xblog.BlogException;
import org.xhome.xblog.Record;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.service.ArticleAttachmentService;
import org.xhome.xblog.core.service.ArticleService;
import org.xhome.xblog.core.service.BlogConfigService;
import org.xhome.xblog.core.service.CategoryService;
import org.xhome.xblog.core.service.RecordService;
import org.xhome.xblog.core.service.TagService;
import org.xhome.xfileupload.FileContent;
import org.xhome.xfileupload.core.service.FileContentService;
import org.xhome.xfileupload.web.util.FileUploadUtils;

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
    private ArticleService           articleService;
    @Autowired
    private RecordService            recordService;
    @Autowired
    private CategoryService          categoryService;
    @Autowired
    private TagService               tagService;
    @Autowired
    private BlogConfigService        blogConfigService;
    @Autowired
    private FileContentService       fileContentService;
    @Autowired
    private ArticleAttachmentService articleAttachmentService;

    public final static String       RM_ARTICLE_INDEX          = "xblog/article/index";
    public final static String       RM_ARTICLE_READ           = "xblog/article/read";
    public final static String       RM_ARTICLE_EDIT           = "xblog/article/edit";
    public final static String       RM_ARTICLE_ADD            = "xblog/article/add";
    public final static String       RM_ARTICLE_UPDATE         = "xblog/article/update";
    public final static String       RM_ARTICLE_LOCK           = "xblog/article/lock";
    public final static String       RM_ARTICLE_UNLOCK         = "xblog/article/unlock";
    public final static String       RM_ARTICLE_DELETE         = "xblog/article/delete";

    public final static String       RM_ARTICLE_EXISTS         = "xblog/article/exists";
    public final static String       RM_ARTICLE_UPDATEABLE     = "xblog/article/updateable";
    public final static String       RM_ARTICLE_LOCKED         = "xblog/article/locked";
    public final static String       RM_ARTICLE_DELETEABLE     = "xblog/article/deleteable";
    public final static String       RM_ARTICLE_GET            = "xblog/article/get";
    public final static String       RM_ARTICLE_QUERY          = "xblog/article/query";
    public final static String       RM_ARTICLE_COUNT          = "xblog/article/count";

    public final static String       RM_ARTICLE_TAG_ADD        = "xblog/article/tag/add";
    public final static String       RM_ARTICLE_TAG_LOCK       = "xblog/article/tag/lock";
    public final static String       RM_ARTICLE_TAG_UNLOCK     = "xblog/article/tag/unlock";
    public final static String       RM_ARTICLE_TAG_DELETE     = "xblog/article/tag/delete";

    public final static String       RM_ARTICLE_TAG_EXISTS     = "xblog/article/tag/exists";
    public final static String       RM_ARTICLE_TAG_UPDATEABLE = "xblog/article/tag/updateable";
    public final static String       RM_ARTICLE_TAG_LOCKED     = "xblog/article/tag/locked";
    public final static String       RM_ARTICLE_TAG_DELETEABLE = "xblog/article/tag/deleteable";

    /**
     * 博客首页
     * 
     * @param searchWord
     *            搜索关键字
     * @param cid
     *            分类ID
     * @param tid
     *            标签ID
     * @param page
     *            指定显示页
     * @param limit
     *            指定每页显示文章条数，默认为5
     * @return
     */
    @RequestMapping(value = RM_ARTICLE_INDEX, method = RequestMethod.GET)
    public Object indexArticle(
                    HttpServletRequest request,
                    @RequestParam(value = "sw", required = false) String searchWord,
                    @RequestParam(value = "cid", required = false) Long cid,
                    @RequestParam(value = "tid", required = false) Long tid,
                    @RequestParam(value = "page", required = false) Long page,
                    @RequestParam(value = "limit", required = false) Long limit) {
        User user = AuthUtils.getCurrentUser(request);
        Map<String, Object> data = new HashMap<String, Object>();

        QueryBase categories = new QueryBase();
        categories.setLimit(Long.MAX_VALUE);
        categoryService.getCategorys(user, categories);
        data.put("categories", categories);

        QueryBase tags = new QueryBase();
        tags.setLimit(Long.MAX_VALUE);
        tagService.getTags(user, tags);
        data.put("tags", tags);

        QueryBase articles = new QueryBase();
        if (StringUtils.isNotEmpty(searchWord)) {
            data.put("search_word", searchWord);
            articles.addParameter("search_word", searchWord);
        }
        articles.addParameter("user", user.getId());

        if (cid != null && cid > 0) {
            articles.addParameter("category_id", cid);
        }
        if (tid != null && tid > 0) {
            articles.addParameter("tag_id", tid);
        }
        if (page != null && page > 0) {
            articles.setPage(page);
        }
        if (limit == null || limit <= 0) {
            limit = 5L;
        }
        articles.setLimit(limit);

        articleService.getArticles(user, articles);
        data.put("articles", articles);

        short status = Status.SUCCESS;
        String uname = user.getName();
        String msg = "博客首页请求成功";
        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, uname, msg);
        }

        return new CommonResult(status, msg, data);
    }

    /**
     * 阅读指定ID的文章
     * 
     * @param id
     *            文章ID
     * @param request
     * @return
     */
    @RequestMapping(value = RM_ARTICLE_READ, method = RequestMethod.GET)
    public Object readArticle(HttpServletRequest request,
                    @RequestParam(value = "id") Long id) {
        User user = AuthUtils.getCurrentUser(request);
        Map<String, Object> data = new HashMap<String, Object>();

        QueryBase categories = new QueryBase();
        categories.setLimit(Long.MAX_VALUE);
        categoryService.getCategorys(user, categories);
        data.put("categories", categories);

        QueryBase tags = new QueryBase();
        tags.setLimit(Long.MAX_VALUE);
        tagService.getTags(user, tags);
        data.put("tags", tags);

        Article article = articleService.getArticle(user, id);

        String uname = user.getName();
        String msg = null;
        short status = Status.SUCCESS;
        if (article != null) {
            data.put("article", article);
            articleService.increaseRead(user, article);

            Record record = new Record(article, user,
                            RequestUtils.getRequestAddress(request),
                            RequestUtils.getRequestUserAgent(request),
                            RequestUtils.getRequestUserAgentName(request));
            recordService.logRecord(record);
            msg = "文章[" + id + "]" + article.getTitle() + "查询成功";
        } else {
            msg = "文章阅读页请求成功";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, uname, msg);
        }

        return new CommonResult(status, msg, data);
    }

    /**
     * 编辑文章页面请求
     * 
     * @param id
     *            文章ID，如果为空则为新建文章
     * @param request
     * @return
     */
    @RequestMapping(value = RM_ARTICLE_EDIT, method = RequestMethod.GET)
    public Object editArticle(
                    @RequestParam(value = "id", required = false) Long id,
                    HttpServletRequest request) {
        User user = AuthUtils.getCurrentUser(request);
        Map<String, Object> data = new HashMap<String, Object>();

        QueryBase categories = new QueryBase();
        categories.setLimit(Long.MAX_VALUE);
        categoryService.getCategorys(user, categories);
        data.put("categories", categories);

        QueryBase tags = new QueryBase();
        tags.setLimit(Long.MAX_VALUE);
        tagService.getTags(user, tags);
        data.put("tags", tags);

        Article article = null;
        if (id != null) {
            article = articleService.getArticle(user, id);
        }

        String uname = user.getName();
        String msg = null;
        short status = Status.SUCCESS;
        if (article != null) {
            data.put("article", article);
            msg = "文章[" + id + "]" + article.getTitle() + "查询成功";
        } else {
            msg = "文章编辑页请求成功";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, uname, msg);
        }

        return new CommonResult(status, msg, data);
    }

    /**
     * 截取文章概要内容
     * 
     * @param article
     */
    private void extractArticleContent(Article article) {
        Document doc = Jsoup.parse(article.getDetail());
        Element element = doc.body(), tmp;

        long maxLen = blogConfigService.getArticleContentLength();
        long textLen = 0, tmpLen = 0;
        StringBuffer content = new StringBuffer();

        while (element != null) {
            Iterator<Element> elements = element.children().iterator();
            while (elements.hasNext()) {
                tmp = elements.next();
                tmpLen += tmp.text().length();

                if (tmpLen < maxLen) {
                    content.append(tmp.outerHtml());
                    textLen = tmpLen;
                } else {
                    break;
                }

            }
            if (textLen == 0 && element.childNodeSize() > 0) {
                element = element.child(0);
                tmpLen = 0;
            } else {
                break;
            }
        }

        article.setContent(content.toString());
    }

    /**
     * 文章新增附件
     * 
     * @param user
     * @param article
     * @param request
     */
    private void addArticleAttachment(User user, Article article,
                    HttpServletRequest request) {
        List<FileContent> fileContents = FileUploadUtils
                        .getSessionFileContents(request);
        if (fileContents != null) {
            String detail = article.getDetail();
            List<FileContent> articleFileContents = new ArrayList<FileContent>();
            for (FileContent fileContent : fileContents) {
                if (detail.indexOf(fileContent.getFullURL()) >= 0) {
                    ArticleAttachment articleAttachment = new ArticleAttachment(
                                    article, fileContent);
                    AuthUtils.setOwner(request, articleAttachment);
                    articleAttachmentService.addArticleAttachment(user,
                                    articleAttachment);
                    articleFileContents.add(fileContent);
                }
            }
            FileUploadUtils.removeFileContent(request, articleFileContents);
            FileUploadUtils.cleanFileContent(request, fileContentService);
        }
    }

    /**
     * 添加新文章
     * 
     * @param article
     *            文章内容
     * @param request
     * @return
     */
    @RequestMapping(value = RM_ARTICLE_ADD, method = RequestMethod.POST)
    public Object addArticle(
                    @Validated @RequestAttribute("article") Article article,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        AuthUtils.setOwner(request, article);
        AuthUtils.setModifier(request, article);

        // 截取文章概要内容
        this.extractArticleContent(article);

        try {
            status = (short) articleService.addArticle(user, article);
            this.addArticleAttachment(user, article, request);
        } catch (BlogException e) {
            status = e.getStatus();
            msg = e.getMessage();
        }
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

    /**
     * 更新文章
     * 
     * @param article
     *            文章内容
     * @param request
     * @return
     */
    @RequestMapping(value = RM_ARTICLE_UPDATE, method = RequestMethod.POST)
    public Object updateArticle(
                    @Validated @RequestAttribute("article") Article article,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        AuthUtils.setModifier(request, article);

        // 截取文章概要内容
        this.extractArticleContent(article);

        try {
            status = (short) articleService.updateArticle(user, article);

            // 清理已上传的附件
            List<ArticleAttachment> articleAttachmentes = articleAttachmentService
                            .getArticleAttachmentes(user, article);
            if (articleAttachmentes != null) {
                String detail = article.getDetail();
                for (ArticleAttachment articleAttachment : articleAttachmentes) {
                    FileContent fileContent = articleAttachment
                                    .getFileContent();
                    if (detail.indexOf(fileContent.getFullURL()) < 0) {
                        fileContentService.deleteFileContent(user, fileContent);
                        AuthUtils.setModifier(request, user);
                        FileUploadUtils.deleteFileContent(fileContent, request);
                    }
                }
            }

            this.addArticleAttachment(user, article, request);
        } catch (BlogException e) {
            status = e.getStatus();
            msg = e.getMessage();
        }
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

    // @RequestMapping(value = RM_ARTICLE_LOCK, method = RequestMethod.POST)
    public Object lockArticle(
                    @Validated @RequestAttribute("article") Article article,
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

    // @RequestMapping(value = RM_ARTICLE_UNLOCK, method = RequestMethod.POST)
    public Object unlockArticle(
                    @Validated @RequestAttribute("article") Article article,
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

    @RequestMapping(value = RM_ARTICLE_DELETE, method = RequestMethod.POST)
    public Object deleteArticle(
                    @Validated @RequestAttribute("articles") List<Article> articles,
                    HttpServletRequest request) {
        short status = 0;
        String msg = null;

        User user = AuthUtils.getCurrentUser(request);
        for (Article article : articles) {
            AuthUtils.setModifier(request, article);
        }
        try {
            status = (short) articleService.deleteArticles(user, articles);
        } catch (RuntimeException e) {
            status = Status.ERROR;
        }

        if (status == Status.SUCCESS) {
            msg = "删除文章成功";
        } else {
            msg = "删除文章失败";
        }

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, user.getName(), msg);
        }

        return new CommonResult(status, msg, articles);
    }

    // @RequestMapping(value = RM_ARTICLE_UPDATEABLE, method =
    // RequestMethod.GET)
    public Object isArticleUpdateable(
                    @Validated @RequestAttribute("article") Article article,
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
    // @RequestMapping(value = RM_ARTICLE_LOCKED, method = RequestMethod.GET)
    public Object isArticleLocked(
                    @Validated @RequestAttribute("article") Article article,
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

    // @RequestMapping(value = RM_ARTICLE_DELETEABLE, method =
    // RequestMethod.GET)
    public Object isArticleDeleteable(
                    @Validated @RequestAttribute("article") Article article,
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

    // @RequestMapping(value = RM_ARTICLE_GET, method = RequestMethod.GET)
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

        if (article != null) {
            Record record = new Record(article, user,
                            RequestUtils.getRequestAddress(request),
                            RequestUtils.getRequestUserAgent(request),
                            RequestUtils.getRequestUserAgentName(request));
            recordService.logRecord(record);
        }

        return new CommonResult(status, msg, article);
    }

    @RequestMapping(value = RM_ARTICLE_QUERY, method = RequestMethod.GET)
    public Object getArticles(QueryBase query, HttpServletRequest request) {
        User user = AuthUtils.getCurrentUser(request);
        String uname = user.getName();

        if (query == null) {
            query = new QueryBase();
        }
        if (logger.isInfoEnabled()) {
            logger.info("文章{}按条件{}查询文章信息", uname, query.getParameters());
        }

        query.addParameter("user", user.getId());
        articleService.getArticles(user, query);

        String msg = "条件查询文章信息";
        short status = Status.SUCCESS;

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, uname, msg);
        }

        return new DataResult(status, msg, query);
    }

    // @RequestMapping(value = RM_ARTICLE_COUNT, method = RequestMethod.GET)
    public Object countArticles(QueryBase query, HttpServletRequest request) {
        User user = AuthUtils.getCurrentUser(request);
        String uname = user.getName();

        if (query == null) {
            query = new QueryBase();
        }
        if (logger.isInfoEnabled()) {
            logger.info("文章{}按条件{}统计文章信息", uname, query.getParameters());
        }

        query.addParameter("user", user.getId());
        long count = articleService.countArticles(user, query);

        String msg = "条件统计文章信息，共" + count;
        short status = Status.SUCCESS;

        if (logger.isInfoEnabled()) {
            logger.info("[{}] {} {}", status, uname, msg);
        }

        return new CommonResult(status, msg, count);
    }

    // @RequestMapping(value = RM_ARTICLE_TAG_ADD, method = RequestMethod.POST)
    public Object addArticleTag(
                    @Validated @RequestAttribute("article") Article article,
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

    // @RequestMapping(value = RM_ARTICLE_TAG_LOCK, method = RequestMethod.POST)
    public Object lockArticleTag(
                    @Validated @RequestAttribute("article") Article article,
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

    // @RequestMapping(value = RM_ARTICLE_TAG_UNLOCK, method =
    // RequestMethod.POST)
    public Object unlockArticleTag(
                    @Validated @RequestAttribute("article") Article article,
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

    @RequestMapping(value = RM_ARTICLE_TAG_DELETE, method = RequestMethod.POST)
    public Object deleteArticleTag(
                    @Validated @RequestAttribute("article") Article article,
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

    // @RequestMapping(value = RM_ARTICLE_TAG_EXISTS, method =
    // RequestMethod.GET)
    public Object isArticleTagExists(
                    @Validated @RequestAttribute("article") Article article,
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

    // @RequestMapping(value = RM_ARTICLE_TAG_UPDATEABLE, method =
    // RequestMethod.GET)
    public Object isArticleTagUpdateable(
                    @Validated @RequestAttribute("article") Article article,
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

    // @RequestMapping(value = RM_ARTICLE_TAG_LOCKED, method =
    // RequestMethod.GET)
    public Object isArticleTagLocked(
                    @Validated @RequestAttribute("article") Article article,
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

    // @RequestMapping(value = RM_ARTICLE_TAG_DELETEABLE, method =
    // RequestMethod.GET)
    public Object isArticleTagDeleteable(
                    @Validated @RequestAttribute("article") Article article,
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

    /**
     * @return the categoryService
     */
    public CategoryService getCategoryService() {
        return categoryService;
    }

    /**
     * @param categoryService
     *            the categoryService to set
     */
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * @return the tagService
     */
    public TagService getTagService() {
        return tagService;
    }

    /**
     * @param tagService
     *            the tagService to set
     */
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * @return the blogConfigService
     */
    public BlogConfigService getBlogConfigService() {
        return blogConfigService;
    }

    /**
     * @param blogConfigService
     *            the blogConfigService to set
     */
    public void setBlogConfigService(BlogConfigService blogConfigService) {
        this.blogConfigService = blogConfigService;
    }

    /**
     * @return the fileContentService
     */
    public FileContentService getFileContentService() {
        return fileContentService;
    }

    /**
     * @param fileContentService
     *            the fileContentService to set
     */
    public void setFileContentService(FileContentService fileContentService) {
        this.fileContentService = fileContentService;
    }

    /**
     * @return the articleAttachmentService
     */
    public ArticleAttachmentService getArticleAttachmentService() {
        return articleAttachmentService;
    }

    /**
     * @param articleAttachmentService
     *            the articleAttachmentService to set
     */
    public void setArticleAttachmentService(
                    ArticleAttachmentService articleAttachmentService) {
        this.articleAttachmentService = articleAttachmentService;
    }

}
