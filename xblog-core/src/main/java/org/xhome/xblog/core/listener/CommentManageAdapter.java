package org.xhome.xblog.core.listener;

import org.xhome.xauth.User;
import org.xhome.xblog.Comment;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:52:54 PM
 * @describe 
 */
public class CommentManageAdapter implements CommentManageListener {

	public boolean beforeCommentManage(User oper, short action, Comment comment, Object ...args) {
		return true;
	}
	
	public void afterCommentManage(User oper, short action, short result, Comment comment, Object ...args) {}
	
}
