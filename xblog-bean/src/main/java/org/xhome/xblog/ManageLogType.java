package org.xhome.xblog;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20137:05:06 PM
 * @describe 管理日志类型
 */
public final class ManageLogType {

	public final static short CATAGORY = 10; // 分类
	public final static short ARTICLE = 11; // 文章
	public final static short TAG = 12; // 表天
	public final static short ARTICLE_TAG = 13; // 文章标签
	public final static short COMMENT = 14; // 评论
	public final static short RECORD = 15; // 文章访问记录
	public final static short CATAGORY_ROLE_PERMISSION = 16; // 分类角色访问权限
	public final static short CATAGORY_USER_PERMISSION = 17; // 分类用户访问权限
	public final static short ARTICLE_ROLE_PERMISSION = 18; // 文章角色访问权限
	public final static short ARTICLE_USER_PERMISSION = 19; // 文章用户访问权限
	public final static short TAG_ROLE_PERMISSION = 20; // 标签角色访问权限
	public final static short TAG_USER_PERMISSION = 21; // 标签用户访问权限
	public final static short MANAGE_LOG = 22;
	
	private ManageLogType() {}
	
}
