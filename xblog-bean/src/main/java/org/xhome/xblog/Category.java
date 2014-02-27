package org.xhome.xblog;

import org.xhome.common.Base;

/**
 * @project blog-bean
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 31, 20135:20:04 PM
 * @describe 分类
 */
public class Category extends Base {

	private static final long serialVersionUID = -5455284914202137553L;

	private String name; // 分类名称
	private Long articleCount; // 分类文章总数
	private Category parent; // NULL表示顶级分类

	public Category() {
	}

	public Category(String name) {
		this.setName(name);
	}

	public Category(String name, Category parent) {
		this.setName(name);
		this.setParent(parent);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the articleCount
	 */
	public Long getArticleCount() {
		return articleCount;
	}

	/**
	 * @param articleCount
	 *            the articleCount to set
	 */
	public void setArticleCount(Long articleCount) {
		this.articleCount = articleCount;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

}
