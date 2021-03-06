package org.xhome.xblog;

import java.util.List;

import org.xhome.common.Base;

/**
 * @project xblog-bean
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 31, 20135:47:05 PM
 * @describe 文章
 */
public class Article extends Base {

	private static final long serialVersionUID = -4955452875474082771L;

	private String title; // 文章标题
	private int attribute = PERMISSION_OPEN; // 文章属性
	private String content; // 文章概要内容
	private String detail; // 文章详细内容
	private Category category; // 所属分类
	private List<Tag> tags; // 文章标签
	private List<Comment> comments; // 文章评论
	private long readCount; // 文章阅读总数
	private long commentCount; // 文章评论总数

	/* 文章权限属性 */
	public final static int PERMISSION_OPEN = 1 << 0; // 完全公开权限
	public final static int PERMISSION_PRIVATE = 1 << 1; // 仅作者可见
	public final static int PERMISSION_READ_ONLY = 1 << 2; // 仅可阅读权限（包含评论）
	public final static int PERMISSION_READ_WITHOUT_COMMENT = 1 << 3; // 不可见评论（仅可阅读文章内容）
	public final static int PERMISSION_ASSIGN = 1 << 4; // 指定权限

	public Article() {
	}

	public Article(String title) {
		this.setTitle(title);
	}

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
		if (this.detail == null) {
			this.setDetail(content);
		}
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail
	 *            the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	/**
	 * @return the readCount
	 */
	public long getReadCount() {
		return readCount;
	}

	/**
	 * @param readCount
	 *            the readCount to set
	 */
	public void setReadCount(long readCount) {
		this.readCount = readCount;
	}

	/**
	 * @return the commentCount
	 */
	public long getCommentCount() {
		return commentCount;
	}

	/**
	 * @param commentCount
	 *            the commentCount to set
	 */
	public void setCommentCount(long commentCount) {
		this.commentCount = commentCount;
	}

}
