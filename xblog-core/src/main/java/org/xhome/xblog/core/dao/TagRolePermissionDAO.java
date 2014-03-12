package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.TagRolePermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 11, 201310:32:06 PM
 * @describe
 */
public interface TagRolePermissionDAO {

	public int addTagRolePermission(TagRolePermission permission);

	public int updateTagRolePermission(TagRolePermission permission);

	public int lockTagRolePermission(TagRolePermission permission);

	public int unlockTagRolePermission(TagRolePermission permission);

	public int deleteTagRolePermission(TagRolePermission permission);

	public boolean isTagRolePermissionExists(TagRolePermission permission);

	public boolean isTagRolePermissionUpdateable(TagRolePermission permission);

	public boolean isTagRolePermissionLocked(TagRolePermission permission);

	public boolean isTagRolePermissionDeleteable(TagRolePermission permission);

	public TagRolePermission queryTagRolePermission(Long id);

	public List<TagRolePermission> queryTagRolePermissions(QueryBase query);

	public long countTagRolePermissions(QueryBase query);

}
