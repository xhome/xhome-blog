package org.xhome.xblog;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:24:37 PM
 * @describe 分类访问权限
 */
public abstract class CategoryPermission extends Permission {
	
	private static final long serialVersionUID = 4791465519402636642L;
	
	protected Category category; // 指定分类

	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
}
