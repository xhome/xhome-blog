package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Article;
import org.xhome.xblog.Comment;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestCommentManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class CommentServiceTest extends AbstractTest {

	private CommentService commentService;
	private long id = 1L;

	public CommentServiceTest() {
		commentService = context.getBean(CommentServiceImpl.class);
		oper.setId(101L);

		((CommentServiceImpl) commentService)
				.registerCommentManageListener(new TestCommentManageListener());
	}

	@Test
	public void testAddComment() {
		Article article = new Article();
		article.setId(3L);
		Comment comment = new Comment("TTTT", article);
		comment.setOwner(1L);
		comment.setModifier(1L);

		// comment.setType(Comment.TYPE_QUOTE);
		// Comment target = new Comment();
		// target.setId(id);
		// comment.setTarget(target);
		commentService.addComment(oper, comment);
	}

	@Test
	public void testGetComment() {
		Comment comment = commentService.getComment(oper, id);
		printComment(comment);
	}

	@Test
	public void testCountComments() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", commentService.countComments(oper, query));
	}

	@Test
	public void testGetComments() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		List<Comment> comments = commentService.getComments(oper, query);
		printComment(comments);
	}

	@Test
	public void testIsCommentUpdateable() {
		Comment comment = commentService.getComment(oper, 1L);
		logger.info("{}", commentService.isCommentUpdateable(oper, comment));
	}

	@Test
	public void testIsCommentDeleteable() {
		Comment comment = commentService.getComment(oper, 1L);
		logger.info("{}", commentService.isCommentDeleteable(oper, comment));
	}

	@Test
	public void testIsCommentLocked() {
		Comment comment = commentService.getComment(oper, 1L);
		logger.info("{}", commentService.isCommentLocked(oper, comment));
	}

	@Test
	public void testLockComment() {
		Comment comment = commentService.getComment(oper, 1L);
		commentService.lockComment(oper, comment);
	}

	@Test
	public void testUnlockComment() {
		Comment comment = commentService.getComment(oper, 1L);
		commentService.unlockComment(oper, comment);
	}

	@Test
	public void testUpdateComment() {
		Comment comment = commentService.getComment(oper, id);
		comment.setId(100L);
		// comment.setVersion(11);
		int r = commentService.updateComment(oper, comment);
		logger.info("result:" + r);
	}

	@Test
	public void testRemoveComment() {
		Comment comment = commentService.getComment(oper, id);
		commentService.removeComment(oper, comment);
		commentService.deleteComment(oper, comment);
	}

}
