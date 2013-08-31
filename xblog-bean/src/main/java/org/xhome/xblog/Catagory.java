package org.xhome.xblog;

import org.xhome.common.Base;

/**
 * @project blog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20135:20:04 PM
 * @describe 分类
 */
public class Catagory extends Base {

	private static final long serialVersionUID = -5455284914202137553L;
	
	private String name; // 分类名称
	private Catagory parent; // NULL表示顶级分类
	
	public Catagory() {}
	
	public Catagory(String name) {
		this.setName(name);
	}
	
	public Catagory(String name, Catagory parent) {
		this.setName(name);
		this.setParent(parent);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Catagory getParent() {
		return parent;
	}
	public void setParent(Catagory parent) {
		this.parent = parent;
	}
	
}
