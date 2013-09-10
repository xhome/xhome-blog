package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Category;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:52:54 PM
 * @describe 
 */
public class CategoryManageAdapter implements CategoryManageListener {

	public boolean beforeCategoryManage(User oper, short action, Category category, Object ...args) {
		return true;
	}
	
	public void afterCategoryManage(User oper, short action, short result, Category category, Object ...args) {}
	
}
