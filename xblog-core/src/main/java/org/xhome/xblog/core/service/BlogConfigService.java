package org.xhome.xblog.core.service;

import org.xhome.xauth.core.service.ConfigService;
import org.xhome.xblog.Article;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Feb 27, 2014
 * @describe 博客管理参数
 */
public interface BlogConfigService extends ConfigService {

    String ITEM_ALLOW_LEAVE_MESSAGE    = "xblog_allow_leave_message";   // 留言开关
    String ITEM_ALLOW_ARTICLE_COMMENT  = "xblog_allow_article_comment"; // 评论开关
    String ITEM_ARTICLE_CONTENT_LENGTH = "xblog_article_content_length"; // 文章概要内容长度

    String ITEM_TITLE_INDEX            = "xblog_title_index";           // 博客首页标题
    String ITEM_TITLE_READ             = "xblog_title_read";            // 博客阅读页标题
    String ITEM_TITLE_NEW              = "xblog_title_new";             // 博客编辑页标题（新建）
    String ITEM_TITLE_EDIT             = "xblog_title_edit";            // 博客编辑页标题（修改）

    /**
     * 查询是否允许留言
     * 
     * @return
     */
    public boolean allowLeaveMessage();

    /**
     * 查询是否允许文章评论
     * 
     * @return
     */
    public boolean allowArticleComment();

    /**
     * 获取文章概要内容长度
     * 
     * @return
     */
    public long getArticleContentLength();

    /**
     * 获取博客首页标题
     * 
     * @return
     */
    public String getTitleOfIndex();

    /**
     * 获取博客阅读页标题
     * 
     * @param article
     *            待阅读的文章
     * @return
     */
    public String getTitleOfRead(Article article);

    /**
     * 获取博客编辑页标题
     * 
     * @param article
     *            待编辑的文章，如果为NULL说明为新建文章
     * @return
     */
    public String getTitleOfEdit(Article article);

    /**
     * 获取博客编辑页标题（新建文章）
     * 
     * @return
     */
    public String getTitleOfEdit();

}
