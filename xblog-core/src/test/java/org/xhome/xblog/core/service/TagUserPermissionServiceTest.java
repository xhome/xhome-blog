package org.xhome.xblog.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Permission;
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagUserPermission;
import org.xhome.xblog.core.AbstractTest;
import org.xhome.xblog.core.listener.TestTagUserPermissionManageListener;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:59:42 PM
 * @describe
 */
public class TagUserPermissionServiceTest extends AbstractTest {

	private TagUserPermissionService tagUserPermissionService;
	private Tag tag;
	private User user;
	private int permission = Permission.COMMENT;
	private Long id = 1L;

	public TagUserPermissionServiceTest() {
		tagUserPermissionService = context
				.getBean(TagUserPermissionServiceImpl.class);
		oper.setId(101L);
		tag = new Tag("TTTT");
		tag.setId(1L);
		user = new User("Jhat");
		user.setId(1L);

		((TagUserPermissionServiceImpl) tagUserPermissionService)
				.registerTagUserPermissionManageListener(new TestTagUserPermissionManageListener());
	}

	@Test
	public void testAddTagUserPermission() {
		TagUserPermission tagUserPermission = new TagUserPermission(tag, user,
				permission);
		tagUserPermission.setOwner(1L);
		tagUserPermission.setModifier(1L);
		tagUserPermissionService.addTagUserPermission(oper, tagUserPermission);
	}

	@Test
	public void testGetTagUserPermission() {
		TagUserPermission tagUserPermission = tagUserPermissionService
				.getTagUserPermission(oper, id);
		printTagUserPermission(tagUserPermission);
	}

	@Test
	public void testCountTagUserPermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		logger.info("{}",
				tagUserPermissionService.countTagUserPermissions(oper, query));
	}

	@Test
	public void testGetTagUserPermissions() {
		QueryBase query = new QueryBase();
		query.addParameter("tag", "1");
		List<TagUserPermission> tagUserPermissions = tagUserPermissionService
				.getTagUserPermissions(oper, query);
		printTagUserPermission(tagUserPermissions);
	}

	@Test
	public void testIsTagUserPermissionUpdateable() {
		TagUserPermission tagUserPermission = tagUserPermissionService
				.getTagUserPermission(oper, id);
		logger.info("{}", tagUserPermissionService
				.isTagUserPermissionUpdateable(oper, tagUserPermission));
	}

	@Test
	public void testIsTagUserPermissionDeleteable() {
		TagUserPermission tagUserPermission = tagUserPermissionService
				.getTagUserPermission(oper, id);
		logger.info("{}", tagUserPermissionService
				.isTagUserPermissionDeleteable(oper, tagUserPermission));
	}

	@Test
	public void testIsTagUserPermissionLocked() {
		TagUserPermission tagUserPermission = tagUserPermissionService
				.getTagUserPermission(oper, id);
		logger.info("{}", tagUserPermissionService.isTagUserPermissionLocked(
				oper, tagUserPermission));
	}

	@Test
	public void testLockTagUserPermission() {
		TagUserPermission tagUserPermission = tagUserPermissionService
				.getTagUserPermission(oper, id);
		tagUserPermissionService.lockTagUserPermission(oper, tagUserPermission);
	}

	@Test
	public void testUnlockTagUserPermission() {
		TagUserPermission tagUserPermission = tagUserPermissionService
				.getTagUserPermission(oper, id);
		tagUserPermissionService.unlockTagUserPermission(oper,
				tagUserPermission);
	}

	@Test
	public void testUpdateTagUserPermission() {
		TagUserPermission tagUserPermission = tagUserPermissionService
				.getTagUserPermission(oper, id);
		tagUserPermission.setId(id);
		// tagUserPermission.setVersion(11);
		int r = tagUserPermissionService.updateTagUserPermission(oper,
				tagUserPermission);
		logger.info("result:" + r);
	}

	@Test
	public void testIsTagUserPermissionExists() {
		logger.info("{}", tagUserPermissionService.isTagUserPermissionExists(
				oper, new TagUserPermission(tag, user, permission)));
	}

	@Test
	public void testDeleteTagUserPermission() {
		TagUserPermission tagUserPermission = tagUserPermissionService
				.getTagUserPermission(oper, id);
		tagUserPermissionService.deleteTagUserPermission(oper,
				tagUserPermission);
	}

}
