package org.xhome.xblog;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:30:26 PM
 * @describe 文章访问权限
 */
public abstract class ArticlePermission extends Permission {

	private static final long serialVersionUID = -7655432863713969479L;
	
	protected Article article; // 指定文章
	
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	
}
