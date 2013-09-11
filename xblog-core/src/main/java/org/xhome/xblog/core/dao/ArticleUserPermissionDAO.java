package org.xhome.xblog.core.dao;

import java.util.List;

import org.xhome.db.query.QueryBase;
import org.xhome.xblog.ArticleUserPermission;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 11, 201310:31:47 PM
 * @describe 
 */
public interface ArticleUserPermissionDAO {
	
	public int addArticleUserPermission(ArticleUserPermission permission);
	
	public int updateArticleUserPermission(ArticleUserPermission permission);
	
	public int lockArticleUserPermission(ArticleUserPermission permission);
	
	public int unlockArticleUserPermission(ArticleUserPermission permission);
	
	public int removeArticleUserPermission(ArticleUserPermission permission);
	
	public int deleteArticleUserPermission(ArticleUserPermission permission);
	
	public boolean isArticleUserPermissionExists(ArticleUserPermission permission);
	
	public boolean isArticleUserPermissionUpdateable(ArticleUserPermission permission);
	
	public boolean isArticleUserPermissionLocked(ArticleUserPermission permission);
	
	public boolean isArticleUserPermissionRemoveable(ArticleUserPermission permission);
	
	public boolean isArticleUserPermissionDeleteable(ArticleUserPermission permission);
	
	public ArticleUserPermission queryArticleUserPermission(Long id);
	
	public List<ArticleUserPermission> queryArticleUserPermissions(QueryBase query);
	
	public long countArticleUserPermissions(QueryBase query);

}
