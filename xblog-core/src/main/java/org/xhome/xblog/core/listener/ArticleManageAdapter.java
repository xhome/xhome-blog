package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Article;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:52:54 PM
 * @describe 
 */
public class ArticleManageAdapter implements ArticleManageListener {

	public boolean beforeArticleManage(User oper, short action, Article tag, Object ...args) {
		return true;
	}
	
	public void afterArticleManage(User oper, short action, short result, Article tag, Object ...args) {}
	
}
