package org.xhome.xblog;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20137:05:06 PM
 * @describe 管理日志类型
 */
public final class ManageLogType {

	public final static short CATAGORY = 101; // 分类
	public final static short ARTICLE = 102; // 文章
	public final static short TAG = 103; // 表天
	public final static short ARTICLE_TAG = 104; // 文章标签
	public final static short COMMENT = 105; // 评论
	public final static short RECORD = 106; // 文章访问记录
	public final static short CATAGORY_ROLE_PERMISSION = 107; // 分类角色访问权限
	public final static short CATAGORY_USER_PERMISSION = 108; // 分类用户访问权限
	public final static short ARTICLE_ROLE_PERMISSION = 109; // 文章角色访问权限
	public final static short ARTICLE_USER_PERMISSION = 110; // 文章用户访问权限
	public final static short TAG_ROLE_PERMISSION = 111; // 标签角色访问权限
	public final static short TAG_USER_PERMISSION = 112; // 标签用户访问权限
	public final static short MANAGE_LOG = 113;
	
	private ManageLogType() {}
	
}
