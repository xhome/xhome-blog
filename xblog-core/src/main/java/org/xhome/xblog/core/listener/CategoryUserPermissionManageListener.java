package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.CategoryUserPermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:05 PM
 * @description 分类用户访问权限管理监听器接口
 */
public interface CategoryUserPermissionManageListener {
	
	/**
	 * 分类用户访问权限管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param categoryUserPermission 待管理的分类用户访问权限信息
	 * @param args 除categoryUserPermission之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeCategoryUserPermissionManage(User oper, short action, CategoryUserPermission categoryUserPermission, Object ...args);
	
	/**
	 * 分类用户访问权限管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param categoryUserPermission 待管理的分类用户访问权限信息
	 * @param args 除categoryUserPermission之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterCategoryUserPermissionManage(User oper, short action, short result, CategoryUserPermission categoryUserPermission, Object ...args);

}
