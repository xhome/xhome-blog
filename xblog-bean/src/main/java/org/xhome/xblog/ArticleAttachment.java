package org.xhome.xblog;

import org.xhome.common.Base;
import org.xhome.xfileupload.FileContent;

/**
 * @project xblog-bean
 * @author jhat
 * @email cpf624@126.com
 * @homepage http://pfchen.org
 * @date Mar 26, 2014
 * @describe 文章附件
 */
public class ArticleAttachment extends Base {

    private static final long serialVersionUID = 1242890456836762712L;

    private Article           article;
    private FileContent       fileContent;

    public ArticleAttachment() {}

    public ArticleAttachment(Article article, FileContent fileContent) {
        this.setArticle(article);
        this.setFileContent(fileContent);
    }

    /**
     * @return the article
     */
    public Article getArticle() {
        return article;
    }

    /**
     * @param article
     *            the article to set
     */
    public void setArticle(Article article) {
        this.article = article;
    }

    /**
     * @return the fileContent
     */
    public FileContent getFileContent() {
        return fileContent;
    }

    /**
     * @param fileContent
     *            the fileContent to set
     */
    public void setFileContent(FileContent fileContent) {
        this.fileContent = fileContent;
    }

}
