package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Tag;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:05 PM
 * @description 标签管理监听器接口
 */
public interface TagManageListener {
	
	/**
	 * 标签管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param tag 待管理的标签信息
	 * @param args 除tag之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeTagManage(User oper, short action, Tag tag, Object ...args);
	
	/**
	 * 标签管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param tag 待管理的标签信息
	 * @param args 除tag之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterTagManage(User oper, short action, short result, Tag tag, Object ...args);

}
