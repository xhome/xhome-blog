package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.TagRolePermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:20:06 PM
 * @describe
 */
@Service
public interface TagRolePermissionService {

	public int addTagRolePermission(User oper,
			TagRolePermission tagRolePermission);

	public int updateTagRolePermission(User oper,
			TagRolePermission tagRolePermission);

	public int lockTagRolePermission(User oper,
			TagRolePermission tagRolePermission);

	public int unlockTagRolePermission(User oper,
			TagRolePermission tagRolePermission);

	public int removeTagRolePermission(User oper,
			TagRolePermission tagRolePermission);

	public int removeTagRolePermissions(User oper,
			List<TagRolePermission> tagRolePermissions);

	public int deleteTagRolePermission(User oper,
			TagRolePermission tagRolePermission);

	public int deleteTagRolePermissions(User oper,
			List<TagRolePermission> tagRolePermissions);

	public boolean isTagRolePermissionExists(User oper,
			TagRolePermission tagRolePermission);

	public boolean isTagRolePermissionUpdateable(User oper,
			TagRolePermission tagRolePermission);

	public boolean isTagRolePermissionLocked(User oper,
			TagRolePermission tagRolePermission);

	public boolean isTagRolePermissionRemoveable(User oper,
			TagRolePermission tagRolePermission);

	public boolean isTagRolePermissionDeleteable(User oper,
			TagRolePermission tagRolePermission);

	public TagRolePermission getTagRolePermission(User oper, long id);

	public List<TagRolePermission> getTagRolePermissions(User oper,
			QueryBase query);

	public long countTagRolePermissions(User oper, QueryBase query);

}
