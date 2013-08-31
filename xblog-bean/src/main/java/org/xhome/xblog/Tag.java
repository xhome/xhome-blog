package org.xhome.xblog;

import org.xhome.common.Base;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:00:18 PM
 * @describe 标签
 */
public class Tag extends Base {
	
	private static final long serialVersionUID = -6819243943286518474L;
	
	private String name; // 标签名称
	
	public Tag() {}
	
	public Tag(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
