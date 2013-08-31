package org.xhome.xblog;

import org.xhome.xauth.Role;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:26:30 PM
 * @describe 分类角色访问权限
 */
public class CatagoryRolePermission extends CatagoryPermission {

	private static final long serialVersionUID = -4199191249201399127L;

	private Role role; // 指定角色
	
	public CatagoryRolePermission() {}
	
	public CatagoryRolePermission(Catagory catagory, Role role, int permission) {
		this.setCatagory(catagory);
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
