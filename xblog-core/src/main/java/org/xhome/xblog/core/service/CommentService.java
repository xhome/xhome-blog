package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Comment;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:20:06 PM
 * @describe
 */
@Service
public interface CommentService {

	public int addComment(User oper, Comment comment);

	public int updateComment(User oper, Comment comment);

	public int lockComment(User oper, Comment comment);

	public int unlockComment(User oper, Comment comment);

	public int removeComment(User oper, Comment comment);

	public int removeComments(User oper, List<Comment> comments);

	public int deleteComment(User oper, Comment comment);

	public int deleteComments(User oper, List<Comment> comments);

	public boolean isCommentUpdateable(User oper, Comment comment);

	public boolean isCommentLocked(User oper, Comment comment);

	public boolean isCommentRemoveable(User oper, Comment comment);

	public boolean isCommentDeleteable(User oper, Comment comment);

	public Comment getComment(User oper, long id);

	public List<Comment> getComments(User oper, QueryBase query);

	public long countComments(User oper, QueryBase query);

}
