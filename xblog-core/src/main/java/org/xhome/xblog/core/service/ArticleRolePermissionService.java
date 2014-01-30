package org.xhome.xblog.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.ArticleRolePermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 10, 201311:20:06 PM
 * @describe
 */
@Service
public interface ArticleRolePermissionService {

	public int addArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission);

	public int updateArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission);

	public int lockArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission);

	public int unlockArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission);

	public int removeArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission);

	public int deleteArticleRolePermission(User oper,
			ArticleRolePermission articleRolePermission);

	public boolean isArticleRolePermissionExists(User oper,
			ArticleRolePermission articleRolePermission);

	public boolean isArticleRolePermissionUpdateable(User oper,
			ArticleRolePermission articleRolePermission);

	public boolean isArticleRolePermissionLocked(User oper,
			ArticleRolePermission articleRolePermission);

	public boolean isArticleRolePermissionRemoveable(User oper,
			ArticleRolePermission articleRolePermission);

	public boolean isArticleRolePermissionDeleteable(User oper,
			ArticleRolePermission articleRolePermission);

	public ArticleRolePermission getArticleRolePermission(User oper, long id);

	public List<ArticleRolePermission> getArticleRolePermissions(User oper,
			QueryBase query);

	public long countArticleRolePermissions(User oper, QueryBase query);

}
