package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Tag;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:20:06 PM
 * @describe
 */
@Service
public interface TagService {

	public int addTag(User oper, Tag tag);

	public int updateTag(User oper, Tag tag);

	public int lockTag(User oper, Tag tag);

	public int unlockTag(User oper, Tag tag);

	public int deleteTag(User oper, Tag tag);

	public int deleteTags(User oper, List<Tag> tags);

	public boolean isTagExists(User oper, Tag tag);

	public boolean isTagUpdateable(User oper, Tag tag);

	public boolean isTagLocked(User oper, Tag tag);

	public boolean isTagDeleteable(User oper, Tag tag);

	public Tag getTag(User oper, long id);

	public Tag getTag(User oper, String name);

	public List<Tag> getTags(User oper, QueryBase query);

	public long countTags(User oper, QueryBase query);

}
