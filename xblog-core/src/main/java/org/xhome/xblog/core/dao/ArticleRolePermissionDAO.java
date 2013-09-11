package org.xhome.xblog.core.dao;

import java.util.List;
import org.xhome.db.query.QueryBase;
import org.xhome.xblog.ArticleRolePermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 11, 201310:32:06 PM
 * @describe 
 */
public interface ArticleRolePermissionDAO {
	
	public int addArticleRolePermission(ArticleRolePermission permission);
	
	public int updateArticleRolePermission(ArticleRolePermission permission);
	
	public int lockArticleRolePermission(ArticleRolePermission permission);
	
	public int unlockArticleRolePermission(ArticleRolePermission permission);
	
	public int removeArticleRolePermission(ArticleRolePermission permission);
	
	public int deleteArticleRolePermission(ArticleRolePermission permission);
	
	public boolean isArticleRolePermissionExists(ArticleRolePermission permission);
	
	public boolean isArticleRolePermissionUpdateable(ArticleRolePermission permission);
	
	public boolean isArticleRolePermissionLocked(ArticleRolePermission permission);
	
	public boolean isArticleRolePermissionRemoveable(ArticleRolePermission permission);
	
	public boolean isArticleRolePermissionDeleteable(ArticleRolePermission permission);
	
	public ArticleRolePermission queryArticleRolePermission(Long id);
	
	public List<ArticleRolePermission> queryArticleRolePermissions(QueryBase query);
	
	public long countArticleRolePermissions(QueryBase query);

}
