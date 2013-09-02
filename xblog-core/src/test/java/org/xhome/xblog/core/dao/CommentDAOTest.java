package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Article;
import org.xhome.xblog.Comment;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:39 PM
 * @describe 
 */
public class CommentDAOTest extends AbstractTest {

	private CommentDAO commentDAO;
	
	public CommentDAOTest() {
		commentDAO = context.getBean(CommentDAO.class);
	}
	
	@Test
	public void testAddComment() {
		Article article = new Article();
		article.setId(1L);
		Comment comment = new Comment("TTTTT", article);
		comment.setOwner(1L);
		comment.setModifier(1L);
		commentDAO.addComment(comment);
	}
	
	@Test
	public void testGetComment() {
		Comment comment = commentDAO.queryComment(1L);
		printComment(comment);
	}
	
	@Test
	public void testQueryComment() {
		QueryBase query = new QueryBase();
		List<Comment> comments = commentDAO.queryComments(query);
		printComment(comments);
	}
	
	@Test
	public void testUpdateComment() {
		
		Comment comment = commentDAO.queryComment(1L);
		printComment(comment);
		
		commentDAO.updateComment(comment);
		
		comment = commentDAO.queryComment(1L);
		printComment(comment);
	}
	
	@Test
	public void testDeleteComment() {
		Comment comment = commentDAO.queryComment(1L);
		commentDAO.deleteComment(comment);
	}
	
}
