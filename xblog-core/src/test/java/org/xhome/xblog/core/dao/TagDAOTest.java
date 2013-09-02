package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Tag;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:39 PM
 * @describe 
 */
public class TagDAOTest extends AbstractTest {

	private TagDAO tagDAO;
	
	public TagDAOTest() {
		tagDAO = context.getBean(TagDAO.class);
	}
	
	@Test
	public void testAddTag() {
		Tag tag = new Tag("TestTag");
		tag.setOwner(1L);
		tag.setModifier(1L);
		tagDAO.addTag(tag);
	}
	
	@Test
	public void testGetTag() {
		Tag tag = tagDAO.queryTagById(1L);
		printTag(tag);
	}
	
	@Test
	public void testQueryTag() {
		QueryBase query = new QueryBase();
		List<Tag> tags = tagDAO.queryTags(query);
		printTag(tags);
	}
	
	@Test
	public void testUpdateTag() {
		Tag tag = tagDAO.queryTagByName("TestTag");
		printTag(tag);
		
		tagDAO.updateTag(tag);
		
		tag = tagDAO.queryTagByName("TestTag");
		printTag(tag);
	}
	
	@Test
	public void testDeleteTag() {
		Tag tag = tagDAO.queryTagByName("TestTag");
		tagDAO.deleteTag(tag);
	}
	
}
