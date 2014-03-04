package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Article;
import org.xhome.xblog.Tag;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 11, 20131:11:08 AM
 * @describe 文章标签管理监听器接口
 */
public interface ArticleTagManageListener {

	/**
	 * 文章标签管理前监听器接口
	 * 
	 * @param oper
	 *            执行该操作的用户
	 * @param action
	 *            操作类型
	 * @param article
	 *            待管理的文章
	 * @param tag
	 *            待管理的文章标签(删除操作是tag可能为null，表示删除文章所有标签)
	 * @param args
	 *            除article之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public boolean beforeArticleTagManage(User oper, short action,
			Article article, Tag tag, Object... args);

	/**
	 * 文章标签管理后监听器接口
	 * 
	 * @param oper
	 *            执行该操作的用户
	 * @param action
	 *            操作类型
	 * @param result
	 *            操作结果
	 * @param article
	 *            待管理的文章
	 * @param tag
	 *            待管理的文章标签(删除操作是tag可能为null，表示删除文章所有标签)
	 * @param args
	 *            除user之外的方法调用参数
	 * @return 是否允许执行该操作：true/false（允许/不允许）
	 */
	public void afterArticleTagManage(User oper, short action, short result,
			Article article, Tag tag, Object... args);

}
