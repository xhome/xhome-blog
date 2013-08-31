package org.xhome.xblog;

import org.xhome.xauth.User;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:39:44 PM
 * @describe 标签用户访问权限
 */
public class TagUserPermission extends TagPermission {

	private static final long serialVersionUID = -7943663118902116733L;
	private User user; // 指定用户
	
	public TagUserPermission() {}
	
	public TagUserPermission(Tag tag, User user, int permission) {
		this.setTag(tag);
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
