package org.xhome.xblog;

import org.xhome.common.Base;

/**
 * @project xblog-bean
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 31, 20136:00:18 PM
 * @describe 标签
 */
public class Tag extends Base {

	private static final long serialVersionUID = -6819243943286518474L;

	private String name; // 标签名称
	private Long articleCount; // 标签文章总数

	public Tag() {
	}

	public Tag(String name) {
		this.setName(name);
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

}
