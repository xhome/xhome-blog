package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.ArticleUserPermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:20:06 PM
 * @describe
 */
@Service
public interface ArticleUserPermissionService {

	public int addArticleUserPermission(User oper,
			ArticleUserPermission articleUserPermission);

	public int updateArticleUserPermission(User oper,
			ArticleUserPermission articleUserPermission);

	public int lockArticleUserPermission(User oper,
			ArticleUserPermission articleUserPermission);

	public int unlockArticleUserPermission(User oper,
			ArticleUserPermission articleUserPermission);

	public int deleteArticleUserPermission(User oper,
			ArticleUserPermission articleUserPermission);

	public int deleteArticleUserPermissions(User oper,
			List<ArticleUserPermission> articleUserPermissions);

	public boolean isArticleUserPermissionExists(User oper,
			ArticleUserPermission articleUserPermission);

	public boolean isArticleUserPermissionUpdateable(User oper,
			ArticleUserPermission articleUserPermission);

	public boolean isArticleUserPermissionLocked(User oper,
			ArticleUserPermission articleUserPermission);

	public boolean isArticleUserPermissionDeleteable(User oper,
			ArticleUserPermission articleUserPermission);

	public ArticleUserPermission getArticleUserPermission(User oper, long id);

	public List<ArticleUserPermission> getArticleUserPermissions(User oper,
			QueryBase query);

	public long countArticleUserPermissions(User oper, QueryBase query);

}
