package org.xhome.xblog;

/**
 * @project xblog-bean
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Aug 31, 20136:39:16 PM
 * @describe 标签访问权限
 */
public class TagPermission extends Permission {

	private static final long serialVersionUID = 1295108418293360961L;
	
	protected Tag tag; // 指定标签

	public Tag getTag() {
		return tag;
	}
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
}
