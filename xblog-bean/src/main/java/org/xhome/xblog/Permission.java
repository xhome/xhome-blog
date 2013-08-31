package org.xhome.xblog;

import org.xhome.common.Base;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:23:09 PM
 * @describe 权限
 */
public abstract class Permission extends Base {
	
	private static final long serialVersionUID = -7058124926859980488L;
	
	protected int permission = COMMENT; // 权限设置
	
	/* 文章访问权限 */
	/* 高权限具备比其低的所有权限 */
	/* 如果未指定分类或标签，则权限适用于所有文章，否则只适用于指定分类或标签的文章 */
	/* 如果未指定角色或用户，则权限适用于所有角色或用户，否则只适用于指定的角色或用户 */
	public final static int DENY = 0; // 不允许访问
	public final static int READ = 1; // 仅允许查看文章内容（不允许查看评论）
	public final static int READ_COMMENT = 2; // 允许查看评论
	public final static int COMMENT = 3; // 允许评论
	public final static int ADD = 4; // 允许添加
	public final static int LOCK = 5; // 允许锁定
	public final static int UNLOCK = 6; // 允许解锁
	public final static int UPDATE = 7; // 允许修改
	public final static int REMOVE = 8; // 允许标记删除
	public final static int DELETE = 9; // 允许删除
	
	/* 分类、标签管理权限 */
	/* 具备管理权限的同时具备文章的所有访问权限 */
	/* 如果未指定分类或标签，则权限适用于所有分类或标签，否则只适用于指定分类或标签的子分类 */
	/* 如果未指定角色或用户，则权限适用于所有角色或用户，否则只适用于指定的角色或用户 */
	public final static int MANAGE_READ = 10; // 允许查看
	public final static int MANAGE_ADD = 11; // 允许添加
	public final static int MANAGE_LOCK = 12; // 允许锁定
	public final static int MANAGE_UNLOCK = 13; // 允许解锁
	public final static int MANAGE_UPDATE = 14; // 允许修改
	public final static int MANAGE_REMOVE = 15; // 允许标记删除
	public final static int MANAGE_DELETE = 16; // 允许删除
	
	public int getPermission() {
		return permission;
	}
	public void setPermission(int permission) {
		this.permission = permission;
	}

}
