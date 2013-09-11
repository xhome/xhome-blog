package org.xhome.xblog.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xblog.Permission;
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagRolePermission;
import org.xhome.xblog.core.AbstractTest;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:54:39 PM
 * @describe 
 */
public class TagRolePermissionDAOTest extends AbstractTest {

	private TagRolePermissionDAO tagRolePermissionDAO;
	private Tag tag;
	private Role role;
	private int permission = Permission.COMMENT;
	private Long id = 1L;
	
	public TagRolePermissionDAOTest() {
		tagRolePermissionDAO = context.getBean(TagRolePermissionDAO.class);
		tag = new Tag("TTTT");
		tag.setId(1L);
		role = new Role("Admin");
		role.setId(1L);
	}
	
	@Test
	public void testAddTagRolePermission() {
		TagRolePermission tagRolePermission = new TagRolePermission(tag, role, permission);
		tagRolePermission.setOwner(1L);
		tagRolePermission.setModifier(1L);
		tagRolePermissionDAO.addTagRolePermission(tagRolePermission);
	}
	
	@Test
	public void testGetTagRolePermission() {
		TagRolePermission tagRolePermission = tagRolePermissionDAO.queryTagRolePermission(id);
		printTagRolePermission(tagRolePermission);
	}
	
	@Test
	public void testQueryTagRolePermission() {
		QueryBase query = new QueryBase();
		List<TagRolePermission> tagRolePermissions = tagRolePermissionDAO.queryTagRolePermissions(query);
		printTagRolePermission(tagRolePermissions);
	}
	
	@Test
	public void testUpdateTagRolePermission() {
		TagRolePermission tagRolePermission = tagRolePermissionDAO.queryTagRolePermission(id);
		printTagRolePermission(tagRolePermission);
		
		tagRolePermissionDAO.updateTagRolePermission(tagRolePermission);
		
		tagRolePermission = tagRolePermissionDAO.queryTagRolePermission(id);
		printTagRolePermission(tagRolePermission);
	}
	
	@Test
	public void testDeleteTagRolePermission() {
		TagRolePermission tagRolePermission = tagRolePermissionDAO.queryTagRolePermission(id);
		tagRolePermissionDAO.deleteTagRolePermission(tagRolePermission);
	}
	
}
