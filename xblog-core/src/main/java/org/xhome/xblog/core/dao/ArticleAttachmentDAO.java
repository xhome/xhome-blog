package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.ArticleAttachment;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 2, 201310:55:20 PM
 * @describe
 */
public interface ArticleAttachmentDAO {

    public int addArticleAttachment(ArticleAttachment articleAttachment);

    public int deleteArticleAttachment(ArticleAttachment articleAttachment);

    public ArticleAttachment queryArticleAttachment(Long id);

    public List<ArticleAttachment> queryArticleAttachmentes(QueryBase query);

    public long countArticleAttachmentes(QueryBase query);

}
