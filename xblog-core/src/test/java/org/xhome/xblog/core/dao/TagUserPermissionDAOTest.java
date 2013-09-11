package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Permission;
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagUserPermission;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:39 PM
 * @describe 
 */
public class TagUserPermissionDAOTest extends AbstractTest {

	private TagUserPermissionDAO tagUserPermissionDAO;
	private Tag tag;
	private User user;
	private int permission = Permission.COMMENT;
	private Long id = 1L;
	
	public TagUserPermissionDAOTest() {
		tagUserPermissionDAO = context.getBean(TagUserPermissionDAO.class);
		tag = new Tag("TTTT");
		tag.setId(1L);
		user = new User("Jhat");
		user.setId(1L);
	}
	
	@Test
	public void testAddTagUserPermission() {
		TagUserPermission tagUserPermission = new TagUserPermission(tag, user, permission);
		tagUserPermission.setOwner(1L);
		tagUserPermission.setModifier(1L);
		tagUserPermissionDAO.addTagUserPermission(tagUserPermission);
	}
	
	@Test
	public void testGetTagUserPermission() {
		TagUserPermission tagUserPermission = tagUserPermissionDAO.queryTagUserPermission(id);
		printTagUserPermission(tagUserPermission);
	}
	
	@Test
	public void testQueryTagUserPermission() {
		QueryBase query = new QueryBase();
		List<TagUserPermission> tagUserPermissions = tagUserPermissionDAO.queryTagUserPermissions(query);
		printTagUserPermission(tagUserPermissions);
	}
	
	@Test
	public void testUpdateTagUserPermission() {
		TagUserPermission tagUserPermission = tagUserPermissionDAO.queryTagUserPermission(id);
		printTagUserPermission(tagUserPermission);
		
		tagUserPermissionDAO.updateTagUserPermission(tagUserPermission);
		
		tagUserPermission = tagUserPermissionDAO.queryTagUserPermission(id);
		printTagUserPermission(tagUserPermission);
	}
	
	@Test
	public void testDeleteTagUserPermission() {
		TagUserPermission tagUserPermission = tagUserPermissionDAO.queryTagUserPermission(id);
		tagUserPermissionDAO.deleteTagUserPermission(tagUserPermission);
	}
	
}
