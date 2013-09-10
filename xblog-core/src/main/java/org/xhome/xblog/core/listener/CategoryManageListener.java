package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Category;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:05 PM
 * @description 分类管理监听器接口
 */
public interface CategoryManageListener {
	
	/**
	 * 分类管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param category 待管理的分类信息
	 * @param args 除category之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeCategoryManage(User oper, short action, Category category, Object ...args);
	
	/**
	 * 分类管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param category 待管理的分类信息
	 * @param args 除category之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterCategoryManage(User oper, short action, short result, Category category, Object ...args);

}
