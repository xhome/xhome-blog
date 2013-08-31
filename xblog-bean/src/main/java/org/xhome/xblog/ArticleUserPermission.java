package org.xhome.xblog;

import org.xhome.xauth.User;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:32:59 PM
 * @describe 文章用户访问权限
 */
public class ArticleUserPermission extends ArticlePermission {
	
	private static final long serialVersionUID = -2133206876614586446L;
	
	private User user; // 指定用户
	
	public ArticleUserPermission() {}
	
	public ArticleUserPermission(Article article, User user, int permission) {
		this.setArticle(article);
		this.setUser(user);
		this.setPermission(permission);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
