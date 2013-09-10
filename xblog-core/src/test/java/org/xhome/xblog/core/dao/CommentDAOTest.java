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
	private long id = 1L;
	
	public CommentDAOTest() {
		commentDAO = context.getBean(CommentDAO.class);
	}
	
	@Test
	public void testAddComment() {
		Article article = new Article();
		article.setId(1L);
		Comment comment = new Comment("TTTT", article);
		comment.setOwner(1L);
		comment.setModifier(1L);
		
//		comment.setType(Comment.TYPE_QUOTE);
//		Comment target = new Comment();
//		target.setId(id);
//		comment.setTarget(target);
		
		commentDAO.addComment(comment);
	}
	
	@Test
	public void testGetComment() {
		Comment comment = commentDAO.queryComment(id);
		printComment(comment);
	}
	
	@Test
	public void testQueryComment() {
		QueryBase query = new QueryBase();
		
		query.addParameter("article_id", 2);
		
		List<Comment> comments = commentDAO.queryComments(query);
		printComment(comments);
	}
	
	@Test
	public void testUpdateComment() {
		
		Comment comment = commentDAO.queryComment(id);
		printComment(comment);
		
		commentDAO.updateComment(comment);
		
		comment = commentDAO.queryComment(id);
		printComment(comment);
	}
	
	@Test
	public void testDeleteComment() {
		Comment comment = commentDAO.queryComment(id);
		commentDAO.deleteComment(comment);
	}
	
}
