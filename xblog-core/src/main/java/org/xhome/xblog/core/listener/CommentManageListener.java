package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Comment;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:05 PM
 * @description 评论管理监听器接口
 */
public interface CommentManageListener {
	
	/**
	 * 评论管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param comment 待管理的评论信息
	 * @param args 除comment之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeCommentManage(User oper, short action, Comment comment, Object ...args);
	
	/**
	 * 评论管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param comment 待管理的评论信息
	 * @param args 除comment之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterCommentManage(User oper, short action, short result, Comment comment, Object ...args);

}
