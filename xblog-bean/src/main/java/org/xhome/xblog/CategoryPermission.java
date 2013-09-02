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
	
	protected Category catagory; // 指定分类

	public Category getCatagory() {
		return catagory;
	}
	public void setCatagory(Category catagory) {
		this.catagory = catagory;
	}
	
}
