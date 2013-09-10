package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Comment;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 201310:24:24 PM
 * @describe 
 */
public interface CommentDAO {

	public int addComment(Comment comment);
	
	public int updateComment(Comment comment);
	
	public int lockComment(Comment comment);
	
	public int unlockComment(Comment comment);
	
	public int removeComment(Comment comment);
	
	public int deleteComment(Comment comment);

	public boolean isCommentUpdateable(Comment comment);
	
	public boolean isCommentLocked(Comment comment);
	
	public boolean isCommentRemoveable(Comment comment);
	
	public boolean isCommentDeleteable(Comment comment);
	
	public Comment queryComment(Long id);
	
	public List<Comment> queryComments(QueryBase query);
	
	public long countComments(QueryBase query);
	
}
