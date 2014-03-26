package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleAttachment;
import org.xhome.xfileupload.FileContent;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:20:06 PM
 * @describe
 */
@Service
public interface ArticleAttachmentService {

    public int addArticleAttachment(User oper,
                    ArticleAttachment articleAttachment);

    public int deleteArticleAttachment(User oper,
                    ArticleAttachment articleAttachment);

    public int deleteArticleAttachmentes(User oper,
                    List<ArticleAttachment> articleAttachments);

    public ArticleAttachment getArticleAttachment(User oper, long id);

    public List<ArticleAttachment> getArticleAttachmentes(User oper,
                    QueryBase query);

    public List<ArticleAttachment> getArticleAttachmentes(User oper,
                    Article article);

    public List<ArticleAttachment> getArticleAttachmentes(User oper,
                    FileContent fileContent);

    public long countArticleAttachmentes(User oper, QueryBase query);

}
