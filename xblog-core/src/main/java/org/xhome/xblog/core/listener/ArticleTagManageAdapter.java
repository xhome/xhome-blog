package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Article;
import org.xhome.xblog.Tag;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 11, 20131:10:54 AM
 * @describe 
 */
public class ArticleTagManageAdapter implements ArticleTagManageListener {

	public boolean beforeArticleTagManage(User oper, short action, Article article, Tag tag, Object ...args) {
		return true;
	}
	
	public void afterArticleTagManage(User oper, short action, short result, Article article, Tag tag, Object ...args) {}
	
}
