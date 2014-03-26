package org.xhome.xblog.core.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.xhome.common.constant.Status;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleAttachment;
import org.xhome.xblog.core.dao.ArticleAttachmentDAO;
import org.xhome.xfileupload.FileContent;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:33 PM
 * @describe
 */
@Service
public class ArticleAttachmentServiceImpl implements ArticleAttachmentService {

    @Autowired
    private ArticleAttachmentDAO articleAttachmentDAO;

    private Logger               logger;

    public ArticleAttachmentServiceImpl() {
        logger = LoggerFactory.getLogger(ArticleAttachmentService.class);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public int addArticleAttachment(User oper,
                    ArticleAttachment articleAttachment) {
        Article article = articleAttachment.getArticle();

        articleAttachment.setStatus(Status.OK);
        articleAttachment.setVersion((short) 0);
        Timestamp t = new Timestamp(System.currentTimeMillis());
        articleAttachment.setCreated(t);
        articleAttachment.setModified(t);

        short r = articleAttachmentDAO.addArticleAttachment(articleAttachment) == 1 ? Status.SUCCESS
                        : Status.ERROR;

        if (logger.isDebugEnabled()) {
            if (r == Status.SUCCESS) {
                logger.debug("success to add attachment for article [{}] {}",
                                article.getId(), article.getTitle());
            } else {
                logger.debug("fail to add attachment for article [{}] {}",
                                article.getId(), article.getTitle());
            }
        }

        return r;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public int deleteArticleAttachment(User oper,
                    ArticleAttachment articleAttachment) {
        Article article = articleAttachment.getArticle();

        short r = Status.SUCCESS;
        articleAttachmentDAO.deleteArticleAttachment(articleAttachment);
        if (logger.isDebugEnabled()) {
            logger.debug("delete attachment {} for article [{}] {}",
                            articleAttachment.getId(), article.getId(),
                            article.getTitle());
        }

        return r;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public int deleteArticleAttachmentes(User oper,
                    List<ArticleAttachment> articleAttachments) {
        int r = Status.SUCCESS;
        for (ArticleAttachment articleAttachment : articleAttachments) {
            r = this.deleteArticleAttachment(oper, articleAttachment);
            if (r != Status.SUCCESS) {
                Article article = articleAttachment.getArticle();
                throw new RuntimeException("fail to delete attachment ["
                                + articleAttachment.getId() + "] for article ["
                                + article.getId() + "] " + article.getTitle());
            }
        }
        return r;
    }

    @Override
    public ArticleAttachment getArticleAttachment(User oper, long id) {
        ArticleAttachment articleAttachment = articleAttachmentDAO
                        .queryArticleAttachment(id);

        if (logger.isDebugEnabled()) {
            if (articleAttachment != null) {
                Article article = articleAttachment.getArticle();
                logger.debug("get attachment {} for article [{}] {}", id,
                                article.getId(), article.getTitle());
            } else {
                logger.debug("articleAttachment of id {} is not exists", id);
            }
        }

        return articleAttachment;
    }

    @Override
    public List<ArticleAttachment> getArticleAttachmentes(User oper,
                    QueryBase query) {
        List<ArticleAttachment> results = articleAttachmentDAO
                        .queryArticleAttachmentes(query);
        if (query != null) {
            query.setResults(results);
            long total = articleAttachmentDAO.countArticleAttachmentes(query);
            query.setTotal(total);
        }

        if (logger.isDebugEnabled()) {
            if (query != null) {
                logger.debug("query articleAttachments with parameters {}",
                                query.getParameters());
            } else {
                logger.debug("query articleAttachments");
            }
        }
        return results;
    }

    /**
     * @see org.xhome.xblog.core.service.ArticleAttachmentService#getArticleAttachmentes(org.xhome.xauth.User,
     *      org.xhome.xblog.Article)
     */
    @Override
    public List<ArticleAttachment> getArticleAttachmentes(User oper,
                    Article article) {
        QueryBase query = new QueryBase();
        query.setLimit(Long.MAX_VALUE);
        query.addParameter("article_id", article.getId());

        return articleAttachmentDAO.queryArticleAttachmentes(query);
    }

    /**
     * @see org.xhome.xblog.core.service.ArticleAttachmentService#getArticleAttachmentes(org.xhome.xauth.User,
     *      org.xhome.xfileupload.FileContent)
     */
    @Override
    public List<ArticleAttachment> getArticleAttachmentes(User oper,
                    FileContent fileContent) {
        QueryBase query = new QueryBase();
        query.setLimit(Long.MAX_VALUE);
        query.addParameter("fileContent_id", fileContent.getId());

        return articleAttachmentDAO.queryArticleAttachmentes(query);
    }

    @Override
    public long countArticleAttachmentes(User oper, QueryBase query) {
        long c = articleAttachmentDAO.countArticleAttachmentes(query);
        if (logger.isDebugEnabled()) {
            if (query != null) {
                logger.debug("count articleAttachments with parameters {} of {}",
                                query.getParameters(), c);
            } else {
                logger.debug("count articleAttachments of {}", c);
            }
        }

        return c;
    }

    public void setArticleAttachmentDAO(
                    ArticleAttachmentDAO articleAttachmentDAO) {
        this.articleAttachmentDAO = articleAttachmentDAO;
    }

    public ArticleAttachmentDAO getArticleAttachmentDAO() {
        return this.articleAttachmentDAO;
    }

}
