package org.xhome.xblog.core.service;

import org.xhome.xauth.Config;
import org.xhome.xauth.core.service.ConfigServiceImpl;
import org.xhome.xblog.Article;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Feb 27, 2014
 * @describe
 */
public class BlogConfigServiceImpl extends ConfigServiceImpl implements
                BlogConfigService {

    /**
     * @see org.xhome.xblog.core.service.BlogConfigService#allowLeaveMessage()
     */
    @Override
    public boolean allowLeaveMessage() {
        Config config = configDAO.queryConfigByItem(ITEM_ALLOW_LEAVE_MESSAGE);
        return config != null ? !"0".equals(config.getValue()) : false;
    }

    /**
     * @see org.xhome.xblog.core.service.BlogConfigService#allowArticleComment()
     */
    @Override
    public boolean allowArticleComment() {
        Config config = configDAO.queryConfigByItem(ITEM_ALLOW_ARTICLE_COMMENT);
        return config != null ? !"0".equals(config.getValue()) : false;
    }

    /**
     * @see org.xhome.xblog.core.service.BlogConfigService#getArticleContentLength()
     */
    @Override
    public long getArticleContentLength() {
        Config config = configDAO
                        .queryConfigByItem(ITEM_ARTICLE_CONTENT_LENGTH);
        return Long.parseLong(config.getValue());
    }

    /**
     * @see org.xhome.xblog.core.service.BlogConfigService#getTitleOfIndex()
     */
    @Override
    public String getTitleOfIndex() {
        Config config = configDAO.queryConfigByItem(ITEM_TITLE_INDEX);
        return config.getValue();
    }

    /**
     * @see org.xhome.xblog.core.service.BlogConfigService#getTitleOfRead(org.xhome.xblog.Article)
     */
    @Override
    public String getTitleOfRead(Article article) {
        Config config = configDAO.queryConfigByItem(ITEM_TITLE_READ);
        String value = config.getValue();
        String title = value.replaceAll("${article.title}", article.getTitle());
        return title;
    }

    /**
     * @see org.xhome.xblog.core.service.BlogConfigService#getTitleOfEdit(org.xhome.xblog.Article)
     */
    @Override
    public String getTitleOfEdit(Article article) {
        if (article == null) {
            Config config = configDAO.queryConfigByItem(ITEM_TITLE_NEW);
            return config.getValue();
        } else {
            Config config = configDAO.queryConfigByItem(ITEM_TITLE_EDIT);
            String value = config.getValue();
            String title = value.replaceAll("${article.title}",
                            article.getTitle());
            return title;
        }

    }

    /**
     * @see org.xhome.xblog.core.service.BlogConfigService#getTitleOfEdit()
     */
    @Override
    public String getTitleOfEdit() {
        return getTitleOfEdit(null);
    }

}
