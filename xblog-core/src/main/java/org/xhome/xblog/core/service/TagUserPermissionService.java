package org.xhome.xblog.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.TagUserPermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:20:06 PM
 * @describe 
 */
@Service
public interface TagUserPermissionService {
	
	public int addTagUserPermission(User oper, TagUserPermission tagUserPermission);
	
	public int updateTagUserPermission(User oper, TagUserPermission tagUserPermission);
	
	public int lockTagUserPermission(User oper, TagUserPermission tagUserPermission);
	
	public int unlockTagUserPermission(User oper, TagUserPermission tagUserPermission);

	public int removeTagUserPermission(User oper, TagUserPermission tagUserPermission);
	
	public int deleteTagUserPermission(User oper, TagUserPermission tagUserPermission);
	
	public boolean isTagUserPermissionExists(User oper, TagUserPermission tagUserPermission);
	
	public boolean isTagUserPermissionUpdateable(User oper, TagUserPermission tagUserPermission);

	public boolean isTagUserPermissionLocked(User oper, TagUserPermission tagUserPermission);
	
	public boolean isTagUserPermissionRemoveable(User oper, TagUserPermission tagUserPermission);
	
	public boolean isTagUserPermissionDeleteable(User oper, TagUserPermission tagUserPermission);
	
	public TagUserPermission getTagUserPermission(User oper, long id);
	
	public List<TagUserPermission> getTagUserPermissions(User oper);
	
	public List<TagUserPermission> getTagUserPermissions(User oper, QueryBase query);
	
	public long countTagUserPermissions(User oper);
	
	public long countTagUserPermissions(User oper, QueryBase query);

}
