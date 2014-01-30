package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Role;
import org.xhome.xblog.Permission;
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagRolePermission;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestTagRolePermissionManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class TagRolePermissionServiceTest extends AbstractTest {

	private TagRolePermissionService tagRolePermissionService;
	private Tag tag;
	private Role role;
	private int permission = Permission.COMMENT;
	private Long id = 1L;

	public TagRolePermissionServiceTest() {
		tagRolePermissionService = context
				.getBean(TagRolePermissionServiceImpl.class);
		oper.setId(101L);
		tag = new Tag("TTTT");
		tag.setId(1L);
		role = new Role("Admin");
		role.setId(1L);

		((TagRolePermissionServiceImpl) tagRolePermissionService)
				.registerTagRolePermissionManageListener(new TestTagRolePermissionManageListener());
	}

	@Test
	public void testAddTagRolePermission() {
		TagRolePermission tagRolePermission = new TagRolePermission(tag, role,
				permission);
		tagRolePermission.setOwner(1L);
		tagRolePermission.setModifier(1L);
		tagRolePermissionService.addTagRolePermission(oper, tagRolePermission);
	}

	@Test
	public void testGetTagRolePermission() {
		TagRolePermission tagRolePermission = tagRolePermissionService
				.getTagRolePermission(oper, id);
		printTagRolePermission(tagRolePermission);
	}

	@Test
	public void testCountTagRolePermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}",
				tagRolePermissionService.countTagRolePermissions(oper, query));
	}

	@Test
	public void testGetTagRolePermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("tag", "1");
		List<TagRolePermission> tagRolePermissions = tagRolePermissionService
				.getTagRolePermissions(oper, query);
		printTagRolePermission(tagRolePermissions);
	}

	@Test
	public void testIsTagRolePermissionUpdateable() {
		TagRolePermission tagRolePermission = tagRolePermissionService
				.getTagRolePermission(oper, id);
		logger.info("{}", tagRolePermissionService
				.isTagRolePermissionUpdateable(oper, tagRolePermission));
	}

	@Test
	public void testIsTagRolePermissionDeleteable() {
		TagRolePermission tagRolePermission = tagRolePermissionService
				.getTagRolePermission(oper, id);
		logger.info("{}", tagRolePermissionService
				.isTagRolePermissionDeleteable(oper, tagRolePermission));
	}

	@Test
	public void testIsTagRolePermissionLocked() {
		TagRolePermission tagRolePermission = tagRolePermissionService
				.getTagRolePermission(oper, id);
		logger.info("{}", tagRolePermissionService.isTagRolePermissionLocked(
				oper, tagRolePermission));
	}

	@Test
	public void testLockTagRolePermission() {
		TagRolePermission tagRolePermission = tagRolePermissionService
				.getTagRolePermission(oper, id);
		tagRolePermissionService.lockTagRolePermission(oper, tagRolePermission);
	}

	@Test
	public void testUnlockTagRolePermission() {
		TagRolePermission tagRolePermission = tagRolePermissionService
				.getTagRolePermission(oper, id);
		tagRolePermissionService.unlockTagRolePermission(oper,
				tagRolePermission);
	}

	@Test
	public void testUpdateTagRolePermission() {
		TagRolePermission tagRolePermission = tagRolePermissionService
				.getTagRolePermission(oper, id);
		tagRolePermission.setId(id);
		// tagRolePermission.setVersion(11);
		int r = tagRolePermissionService.updateTagRolePermission(oper,
				tagRolePermission);
		logger.info("result:" + r);
	}

	@Test
	public void testIsTagRolePermissionExists() {
		logger.info("{}", tagRolePermissionService.isTagRolePermissionExists(
				oper, new TagRolePermission(tag, role, permission)));
	}

	@Test
	public void testRemoveTagRolePermission() {
		TagRolePermission tagRolePermission = tagRolePermissionService
				.getTagRolePermission(oper, id);
		tagRolePermissionService.removeTagRolePermission(oper,
				tagRolePermission);
		tagRolePermissionService.deleteTagRolePermission(oper,
				tagRolePermission);
	}

}
