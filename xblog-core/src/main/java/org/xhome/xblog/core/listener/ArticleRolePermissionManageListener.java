package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.ArticleRolePermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:05 PM
 * @description 文章角色访问权限管理监听器接口
 */
public interface ArticleRolePermissionManageListener {
	
	/**
	 * 文章角色访问权限管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param articleRolePermission 待管理的文章角色访问权限信息
	 * @param args 除articleRolePermission之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeArticleRolePermissionManage(User oper, short action, ArticleRolePermission articleRolePermission, Object ...args);
	
	/**
	 * 文章角色访问权限管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param articleRolePermission 待管理的文章角色访问权限信息
	 * @param args 除articleRolePermission之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterArticleRolePermissionManage(User oper, short action, short result, ArticleRolePermission articleRolePermission, Object ...args);

}
