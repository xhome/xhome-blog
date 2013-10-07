package org.xhome.validator.mapping;

import java.util.HashMap;
import java.util.Map;

import org.xhome.validator.IdValidator;
import org.xhome.validator.StatusValidator;
import org.xhome.validator.VersionValidator;
import org.xhome.xblog.web.action.ArticleAction;
import org.xhome.xblog.web.action.CategoryAction;
import org.xhome.xblog.web.action.TagAction;
import org.xhome.xblog.web.validator.ArticleCategoryValidator;
import org.xhome.xblog.web.validator.ArticleContentValidator;
import org.xhome.xblog.web.validator.ArticleTagsValidator;
import org.xhome.xblog.web.validator.ArticleTitleValidator;
import org.xhome.xblog.web.validator.CategoryNameValidator;
import org.xhome.xblog.web.validator.TagNameValidator;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 28, 20139:16:59 PM
 * @describe 
 */
public class BlogValidatorMapping implements Mapping {

	@Override
	public Map<String, String> validatorMappings() {
		Map<String, String> mappings = new HashMap<String, String>();
		String idValidator = IdValidator.class.getName(),
				versionValidator = VersionValidator.class.getName(),
				baseValidator = idValidator + "," + versionValidator + "," + StatusValidator.class.getName();
		
		String tagNameValidator = TagNameValidator.class.getName(),
			tagIdNameValidator = idValidator + "," + tagNameValidator,
			tagVersionValidator = tagIdNameValidator + "," + versionValidator;
		mappings.put(TagAction.RM_TAG_ADD, tagNameValidator);
		mappings.put(TagAction.RM_TAG_UPDATE, baseValidator + "," + tagNameValidator);
		mappings.put(TagAction.RM_TAG_LOCK, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_UNLOCK, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_REMOVE, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_DELETE, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_EXISTS, tagNameValidator);
		mappings.put(TagAction.RM_TAG_UPDATEABLE, tagIdNameValidator);
		mappings.put(TagAction.RM_TAG_LOCKED, tagIdNameValidator);
		mappings.put(TagAction.RM_TAG_REMOVEABLE, tagIdNameValidator);
		mappings.put(TagAction.RM_TAG_DELETEABLE, tagIdNameValidator);
		
		String categoryNameValidator = CategoryNameValidator.class.getName(),
				categoryIdNameValidator = idValidator + "," + categoryNameValidator,
				categoryVersionValidator = categoryIdNameValidator + "," + versionValidator;
			mappings.put(CategoryAction.RM_CATEGORY_ADD, categoryNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_UPDATE, baseValidator + "," + categoryNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_LOCK, categoryVersionValidator);
			mappings.put(CategoryAction.RM_CATEGORY_UNLOCK, categoryVersionValidator);
			mappings.put(CategoryAction.RM_CATEGORY_REMOVE, categoryVersionValidator);
			mappings.put(CategoryAction.RM_CATEGORY_DELETE, categoryVersionValidator);
			mappings.put(CategoryAction.RM_CATEGORY_EXISTS, categoryNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_UPDATEABLE, categoryIdNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_LOCKED, categoryIdNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_REMOVEABLE, categoryIdNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_DELETEABLE, categoryIdNameValidator);
			
		String articleTitleValidator = ArticleTitleValidator.class.getName(),
			   articleContentValidator = ArticleContentValidator.class.getName(),
			   articleCategoryValidator = ArticleCategoryValidator.class.getName(),
			   articleTagsValidator = ArticleTagsValidator.class.getName(),
			   articleAUValidator = articleTitleValidator + "," + articleContentValidator
			   		+ "," + articleCategoryValidator + "," + articleTagsValidator,
			   articleIdTitleValidator = idValidator + "," + articleTitleValidator,
			   articleVersionValidator = articleIdTitleValidator + "," + versionValidator,
			   articleTagValidator = articleIdTitleValidator + "," + tagIdNameValidator;
		
			mappings.put(ArticleAction.RM_ARTICLE_ADD, articleAUValidator);
			mappings.put(ArticleAction.RM_ARTICLE_UPDATE, baseValidator + "," + articleAUValidator);
			mappings.put(ArticleAction.RM_ARTICLE_LOCK, articleVersionValidator);
			mappings.put(ArticleAction.RM_ARTICLE_UNLOCK, articleVersionValidator);
			mappings.put(ArticleAction.RM_ARTICLE_REMOVE, articleVersionValidator);
			mappings.put(ArticleAction.RM_ARTICLE_DELETE, articleVersionValidator);
			mappings.put(ArticleAction.RM_ARTICLE_EXISTS, articleTitleValidator);
			mappings.put(ArticleAction.RM_ARTICLE_UPDATEABLE, articleIdTitleValidator);
			mappings.put(ArticleAction.RM_ARTICLE_LOCKED, articleIdTitleValidator);
			mappings.put(ArticleAction.RM_ARTICLE_REMOVEABLE, articleIdTitleValidator);
			mappings.put(ArticleAction.RM_ARTICLE_DELETEABLE, articleIdTitleValidator);
			
			mappings.put(ArticleAction.RM_ARTICLE_TAG_ADD, articleTagValidator);
			mappings.put(ArticleAction.RM_ARTICLE_TAG_LOCK, articleTagValidator);
			mappings.put(ArticleAction.RM_ARTICLE_TAG_UNLOCK, articleTagValidator);
			mappings.put(ArticleAction.RM_ARTICLE_TAG_REMOVE, articleTagValidator);
			mappings.put(ArticleAction.RM_ARTICLE_TAG_DELETE, articleTagValidator);
			mappings.put(ArticleAction.RM_ARTICLE_TAG_EXISTS, articleTagValidator);
			mappings.put(ArticleAction.RM_ARTICLE_TAG_UPDATEABLE, articleTagValidator);
			mappings.put(ArticleAction.RM_ARTICLE_TAG_LOCKED, articleTagValidator);
			mappings.put(ArticleAction.RM_ARTICLE_TAG_REMOVEABLE, articleTagValidator);
			mappings.put(ArticleAction.RM_ARTICLE_TAG_DELETEABLE, articleTagValidator);
		
		return mappings;
	}

}
