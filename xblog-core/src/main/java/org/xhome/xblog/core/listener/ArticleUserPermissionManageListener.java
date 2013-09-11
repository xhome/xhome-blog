package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.ArticleUserPermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:05 PM
 * @description 文章用户访问权限管理监听器接口
 */
public interface ArticleUserPermissionManageListener {
	
	/**
	 * 文章用户访问权限管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param articleUserPermission 待管理的文章用户访问权限信息
	 * @param args 除articleUserPermission之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeArticleUserPermissionManage(User oper, short action, ArticleUserPermission articleUserPermission, Object ...args);
	
	/**
	 * 文章用户访问权限管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param articleUserPermission 待管理的文章用户访问权限信息
	 * @param args 除articleUserPermission之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterArticleUserPermissionManage(User oper, short action, short result, ArticleUserPermission articleUserPermission, Object ...args);

}
