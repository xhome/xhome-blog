package org.xhome.xblog;

import org.xhome.xauth.User;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:26:38 PM
 * @describe 分类用户访问权限 
 */
public class CategoryUserPermission extends CategoryPermission {

	private static final long serialVersionUID = -6041892211617473781L;

	private User user; // 指定用户
	
	public CategoryUserPermission() {}
	
	public CategoryUserPermission(Category catagory, User user, int permission) {
		this.setCatagory(catagory);
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
