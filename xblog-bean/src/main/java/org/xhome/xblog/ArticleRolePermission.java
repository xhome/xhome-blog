package org.xhome.xblog;

import org.xhome.xauth.Role;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:33:06 PM
 * @describe 文章角色访问权限
 */
public class ArticleRolePermission extends ArticlePermission {

	private static final long serialVersionUID = -3462881039424073937L;
	
	private Role role; // 指定角色
	
	public ArticleRolePermission() {}
	
	public ArticleRolePermission(Article article, Role role, int permission) {
		this.setArticle(article);
		this.setRole(role);
		this.setPermission(permission);
	}

	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

}
