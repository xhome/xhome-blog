package org.xhome.xblog;

import org.xhome.xauth.Role;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:39:29 PM
 * @describe 标签角色访问权限
 */
public class TagRolePermission extends TagPermission {

	private static final long serialVersionUID = 5346873962626324634L;
	
	private Role role; // 指定角色
	
	public TagRolePermission() {}
	
	public TagRolePermission(Tag tag, Role role, int permission) {
		this.setTag(tag);
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
