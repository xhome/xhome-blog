package org.xhome.xblog.core.dao;

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
 * @date Sep 2, 201311:11:07 PM
 * @describe
 */
public class ArticleAttachmentDAOTest extends AbstractTest {

    private ArticleAttachmentDAO articleAttachmentDAO;
    private long                 id = 1L;

    public ArticleAttachmentDAOTest() {
        articleAttachmentDAO = context.getBean(ArticleAttachmentDAO.class);
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

        articleAttachmentDAO.addArticleAttachment(articleAttachment);
    }

    @Test
    public void testGetArticleAttachment() {
        ArticleAttachment articleAttachment = articleAttachmentDAO
                        .queryArticleAttachment(1L);
        printArticleAttachment(articleAttachment);
    }

    @Test
    public void testQueryArticleAttachment() {
        QueryBase query = new QueryBase();
        query.addParameter("article_id", "5");
        query.addParameter("fileContent_id", "1");
        List<ArticleAttachment> articleAttachments = articleAttachmentDAO
                        .queryArticleAttachmentes(query);
        printArticleAttachment(articleAttachments);
    }

    @Test
    public void testDeleteArticleAttachment() {
        ArticleAttachment articleAttachment = articleAttachmentDAO
                        .queryArticleAttachment(id);
        articleAttachmentDAO.deleteArticleAttachment(articleAttachment);
    }

}
