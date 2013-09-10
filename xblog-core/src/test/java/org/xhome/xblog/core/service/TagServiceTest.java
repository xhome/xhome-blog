package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestTagManageListener;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:59:42 PM
 * @describe 
 */
public class TagServiceTest extends AbstractTest {
	
	private TagService	tagService;
	
	public TagServiceTest() {
		tagService = context.getBean(TagServiceImpl.class);
		oper.setId(101L);
		
		((TagServiceImpl)tagService).registerTagManageListener(new TestTagManageListener());
	}
	
	@Test
	public void testAddTag() {
		Tag tag = new Tag("TestTag");
		tag.setOwner(1L);
		tag.setModifier(1L);
		tagService.addTag(oper, tag);
	}
	
	@Test
	public void testGetTag() {
		Tag tag = tagService.getTag(oper, 1L);
		printTag(tag);
		
		tag = tagService.getTag(oper, "TestTag");
		printTag(tag);
	}
	
	@Test
	public void testCountTags() {
		logger.info("{}", tagService.countTags(oper));
		
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}", tagService.countTags(oper, query));
	}
	
	@Test
	public void testGetTags() {
		List<Tag> tags = tagService.getTags(oper);
		printTag(tags);
		
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		tags = tagService.getTags(oper, query);
		printTag(tags);
	}
	
	@Test
	public void testIsTagUpdateable(){
		Tag tag = tagService.getTag(oper, 1L);
		logger.info("{}", tagService.isTagUpdateable(oper, tag));
	}
	
	@Test
	public void testIsTagDeleteable(){
		Tag tag = tagService.getTag(oper, 1L);
		logger.info("{}", tagService.isTagDeleteable(oper, tag));
	}
	
	@Test
	public void testIsTagLocked(){
		Tag tag = tagService.getTag(oper, 1L);
		logger.info("{}", tagService.isTagLocked(oper, tag));
	}
	
	@Test
	public void testLockTag(){
		Tag tag = tagService.getTag(oper, 1L);
		tagService.lockTag(oper, tag);
	}
	
	@Test
	public void testUnlockTag(){
		Tag tag = tagService.getTag(oper, 1L);
		tagService.unlockTag(oper, tag);
	}
	
	@Test
	public void testUpdateTag() {
		Tag tag = tagService.getTag(oper, "TestTag");
		tag.setId(100L);
		// tag.setVersion(11);
		int r = tagService.updateTag(oper, tag);
		logger.info("result:" + r);
	}
	
	@Test
	public void testIsTagExists() {
		logger.info("{}", tagService.isTagExists(oper, new Tag("TestTag")));
	}
	
	@Test
	public void testRemoveTag() {
		Tag tag = tagService.getTag(oper, "TestTag");
		tagService.removeTag(oper, tag);
		tagService.deleteTag(oper, tag);
	}
	
}
