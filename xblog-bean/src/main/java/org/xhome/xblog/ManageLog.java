package org.xhome.xblog;

import org.xhome.common.Base;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20137:05:06 PM
 * @describe 管理日志
 */
public class ManageLog extends Base {

	private static final long serialVersionUID = 4565215679462832967L;
	
	private String  content; // 内容描述
	private Short 	action;   // 动作 0:Add, 1:Update, 2: Remove, 3: Search...
	private Short 	type; 	  // 类型 1: 分类, 2: 文章, 3: 标签, 4: 文章标签, 5: 评论, 6: 文章访问记录..
	private Long 	obj; 	  // 操作对象
	
	public final static short TYPE_CATAGORY = 1; // 分类
	public final static short TYPE_ARTICLE = 2; // 文章
	public final static short TYPE_TAG = 3; // 表天
	public final static short TYPE_ARTICLE_TAG = 4; // 文章标签
	public final static short TYPE_COMMENT = 5; // 评论
	public final static short TYPE_RECORDS = 6; // 文章访问记录
	public final static short TYPE_CATAGORY_ROLE_PERMISSION = 7; // 分类角色访问权限
	public final static short TYPE_CATAGORY_USER_PERMISSION = 8; // 分类用户访问权限
	public final static short TYPE_ARTICLE_ROLE_PERMISSION = 9; // 文章角色访问权限
	public final static short TYPE_ARTICLE_USER_PERMISSION = 10; // 文章用户访问权限
	public final static short TYPE_TAG_ROLE_PERMISSION = 11; // 标签角色访问权限
	public final static short TYPE_TAG_USER_PERMISSION = 12; // 标签用户访问权限
	
	public ManageLog() {}
	
	public ManageLog (Short action, Short type, Long obj, Long user) {
		this.setAction(action);
		this.setType(type);
		this.setObj(obj);
		this.setOwner(user);
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Short getAction() {
		return action;
	}
	public void setAction(Short action) {
		this.action = action;
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public Long getObj() {
		return obj;
	}
	public void setObj(Long obj) {
		this.obj = obj;
	}
	
}
