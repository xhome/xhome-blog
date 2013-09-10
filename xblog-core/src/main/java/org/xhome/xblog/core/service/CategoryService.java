package org.xhome.xblog.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.User;
import org.xhome.xblog.Category;

/**
 * @project xblog-core
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 10, 201311:20:06 PM
 * @describe 
 */
@Service
public interface CategoryService {
	
	public int addCategory(User oper, Category category);
	
	public int updateCategory(User oper, Category category);
	
	public int lockCategory(User oper, Category category);
	
	public int unlockCategory(User oper, Category category);

	public int removeCategory(User oper, Category category);
	
	public int deleteCategory(User oper, Category category);
	
	public boolean isCategoryExists(User oper, Category category);
	
	public boolean isCategoryUpdateable(User oper, Category category);

	public boolean isCategoryLocked(User oper, Category category);
	
	public boolean isCategoryRemoveable(User oper, Category category);
	
	public boolean isCategoryDeleteable(User oper, Category category);
	
	public Category getCategory(User oper, long id);
	
	public Category getCategory(User oper, String name);
	
	public List<Category> getCategorys(User oper);
	
	public List<Category> getCategorys(User oper, QueryBase query);
	
	public long countCategorys(User oper);
	
	public long countCategorys(User oper, QueryBase query);

}
