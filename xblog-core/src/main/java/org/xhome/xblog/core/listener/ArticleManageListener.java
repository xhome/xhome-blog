package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Article;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:53:05 PM
 * @description 文章管理监听器接口
 */
public interface ArticleManageListener {
	
	/**
	 * 文章管理前监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param article 待管理的文章信息
	 * @param args 除article之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeArticleManage(User oper, short action, Article article, Object ...args);
	
	/**
	 * 文章管理后监听器接口
	 * 
	 * @param oper 执行该操作的用户
	 * @param action 操作类型
	 * @param result 操作结果
	 * @param article 待管理的文章信息
	 * @param args 除article之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterArticleManage(User oper, short action, short result, Article article, Object ...args);

}
