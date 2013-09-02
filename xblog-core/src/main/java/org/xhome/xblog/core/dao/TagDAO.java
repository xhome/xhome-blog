package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.Tag;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 2, 20139:40:44 PM
 * @describe 
 */
public interface TagDAO {

	public int addTag(Tag tag);
	
	public int updateTag(Tag tag);
	
	public int lockTag(Tag tag);
	
	public int unlockTag(Tag tag);
	
	public int removeTag(Tag tag);
	
	public int deleteTag(Tag tag);
	
	public boolean isTagExists(Tag tag);
	
	public boolean isTagUpdateable(Tag tag);
	
	public boolean isTagLocked(Tag tag);
	
	public boolean isTagRemoveable(Tag tag);
	
	public boolean isTagDeleteable(Tag tag);
	
	public Tag queryTagById(Long id);
	
	public Tag queryTagByName(String name);
	
	public List<Tag> queryTags(QueryBase query);
	
	public long countTags(QueryBase query);
	
}
