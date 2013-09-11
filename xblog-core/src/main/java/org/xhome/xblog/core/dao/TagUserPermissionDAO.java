package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.TagUserPermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 11, 201310:31:47 PM
 * @describe 
 */
public interface TagUserPermissionDAO {
	
	public int addTagUserPermission(TagUserPermission permission);
	
	public int updateTagUserPermission(TagUserPermission permission);
	
	public int lockTagUserPermission(TagUserPermission permission);
	
	public int unlockTagUserPermission(TagUserPermission permission);
	
	public int removeTagUserPermission(TagUserPermission permission);
	
	public int deleteTagUserPermission(TagUserPermission permission);
	
	public boolean isTagUserPermissionExists(TagUserPermission permission);
	
	public boolean isTagUserPermissionUpdateable(TagUserPermission permission);
	
	public boolean isTagUserPermissionLocked(TagUserPermission permission);
	
	public boolean isTagUserPermissionRemoveable(TagUserPermission permission);
	
	public boolean isTagUserPermissionDeleteable(TagUserPermission permission);
	
	public TagUserPermission queryTagUserPermission(Long id);
	
	public List<TagUserPermission> queryTagUserPermissions(QueryBase query);
	
	public long countTagUserPermissions(QueryBase query);

}
