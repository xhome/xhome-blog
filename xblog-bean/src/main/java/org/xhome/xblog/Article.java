package org.xhome.xblog;

import java.util.List;

import org.xhome.common.Base;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20135:47:05 PM
 * @describe 文章
 */
public class Article extends Base {

	private static final long serialVersionUID = -4955452875474082771L;
	
	private String title; // 文章标题
	private int attribute = PERMISSION_OPEN; // 文章属性
	private String content; // 文章内容
	private Catagory catagory; // 所属分类
	private List<Tag> tags; // 文章标签
	private List<Comment> comments; // 文章评论
	
	/* 文章权限属性 */
	public final static int PERMISSION_OPEN = 1 << 0; // 完全公开权限
	public final static int PERMISSION_PRIVATE = 1 << 1; // 仅作者可见
	public final static int PERMISSION_READ_ONLY = 1 << 2; // 仅可阅读权限（包含评论）
	public final static int PERMISSION_READ_WITHOUT_COMMENT = 1 << 3; // 不可见评论（仅可阅读文章内容）
	public final static int PERMISSION_ASSIGN = 1 << 4; // 指定权限
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getAttribute() {
		return attribute;
	}
	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Catagory getCatagory() {
		return catagory;
	}
	public void setCatagory(Catagory catagory) {
		this.catagory = catagory;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
