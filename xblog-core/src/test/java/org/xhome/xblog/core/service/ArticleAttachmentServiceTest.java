package org.xhome.xblog.core.service;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleAttachment;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xfileupload.FileContent;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class ArticleAttachmentServiceTest extends AbstractTest {

    private ArticleAttachmentService articleAttachmentService;

    public ArticleAttachmentServiceTest() {
        articleAttachmentService = context
                        .getBean(ArticleAttachmentServiceImpl.class);
        oper.setId(101L);
    }

    @Test
    public void testAddArticleAttachment() {
        Article article = new Article();
        article.setId(5L);
        FileContent fileContent = new FileContent();
        fileContent.setId(1L);
        ArticleAttachment articleAttachment = new ArticleAttachment(article,
                        fileContent);
        articleAttachment.setOwner(1L);
        articleAttachment.setModifier(1L);

        Timestamp time = new Timestamp(System.currentTimeMillis());
        articleAttachment.setCreated(time);
        articleAttachment.setModified(time);

        articleAttachmentService.addArticleAttachment(oper, articleAttachment);
    }

    @Test
    public void testGetArticleAttachment() {
        ArticleAttachment articleAttachment = articleAttachmentService
                        .getArticleAttachment(oper, 1L);
        printArticleAttachment(articleAttachment);
    }

    @Test
    public void testGetArticleAttachmentes() {
        QueryBase query = new QueryBase();
        List<ArticleAttachment> articleAttachments = articleAttachmentService
                        .getArticleAttachmentes(oper, query);
        printArticleAttachment(articleAttachments);

        Article article = new Article();
        article.setId(5L);
        articleAttachments = articleAttachmentService.getArticleAttachmentes(
                        oper, article);
        printArticleAttachment(articleAttachments);

        FileContent fileContent = new FileContent();
        fileContent.setId(1L);
        articleAttachments = articleAttachmentService.getArticleAttachmentes(
                        oper, fileContent);
        printArticleAttachment(articleAttachments);
    }

    @Test
    public void testDeleteArticleAttachment() {
        ArticleAttachment articleAttachment = articleAttachmentService
                        .getArticleAttachment(oper, 1L);
        articleAttachmentService.deleteArticleAttachment(oper,
                        articleAttachment);
    }

}
