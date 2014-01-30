package org.xhome.validator.mapping;

import java.util.HashMap;
import java.util.Map;

import org.xhome.validator.IdValidator;
import org.xhome.validator.StatusValidator;
import org.xhome.validator.VersionValidator;
import org.xhome.xblog.web.action.ArticleAction;
import org.xhome.xblog.web.action.ArticleRolePermissionAction;
import org.xhome.xblog.web.action.ArticleUserPermissionAction;
import org.xhome.xblog.web.action.CategoryAction;
import org.xhome.xblog.web.action.CategoryRolePermissionAction;
import org.xhome.xblog.web.action.CategoryUserPermissionAction;
import org.xhome.xblog.web.action.CommentAction;
import org.xhome.xblog.web.action.TagAction;
import org.xhome.xblog.web.action.TagRolePermissionAction;
import org.xhome.xblog.web.action.TagUserPermissionAction;
import org.xhome.xblog.web.validator.ArticleCategoryValidator;
import org.xhome.xblog.web.validator.ArticleContentValidator;
import org.xhome.xblog.web.validator.ArticlePermissionArticleValidator;
import org.xhome.xblog.web.validator.ArticlePermissionRoleValidator;
import org.xhome.xblog.web.validator.ArticlePermissionUserValidator;
import org.xhome.xblog.web.validator.ArticleTagsValidator;
import org.xhome.xblog.web.validator.ArticleTitleValidator;
import org.xhome.xblog.web.validator.CategoryNameValidator;
import org.xhome.xblog.web.validator.CategoryPermissionCategoryValidator;
import org.xhome.xblog.web.validator.CategoryPermissionRoleValidator;
import org.xhome.xblog.web.validator.CategoryPermissionUserValidator;
import org.xhome.xblog.web.validator.CommentArticleValidator;
import org.xhome.xblog.web.validator.CommentContentValidator;
import org.xhome.xblog.web.validator.CommentTargetValidator;
import org.xhome.xblog.web.validator.TagNameValidator;
import org.xhome.xblog.web.validator.TagPermissionRoleValidator;
import org.xhome.xblog.web.validator.TagPermissionTagValidator;
import org.xhome.xblog.web.validator.TagPermissionUserValidator;

/**
 * @project xblog-web
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 28, 20139:16:59 PM
 * @describe
 */
public class BlogValidatorMapping implements Mapping {

	@Override
	public Map<String, String> validatorMappings() {
		Map<String, String> mappings = new HashMap<String, String>();
		String idValidator = IdValidator.class.getName(), versionValidator = VersionValidator.class
				.getName(), baseValidator = idValidator + ","
				+ versionValidator + "," + StatusValidator.class.getName();

		String tagNameValidator = TagNameValidator.class.getName(), tagIdNameValidator = idValidator
				+ "," + tagNameValidator, tagVersionValidator = tagIdNameValidator
				+ "," + versionValidator;
		mappings.put(TagAction.RM_TAG_ADD, tagNameValidator);
		mappings.put(TagAction.RM_TAG_UPDATE, baseValidator + ","
				+ tagNameValidator);
		mappings.put(TagAction.RM_TAG_LOCK, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_UNLOCK, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_REMOVE, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_DELETE, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_EXISTS, tagNameValidator);
		mappings.put(TagAction.RM_TAG_UPDATEABLE, tagIdNameValidator);
		mappings.put(TagAction.RM_TAG_LOCKED, tagIdNameValidator);
		mappings.put(TagAction.RM_TAG_REMOVEABLE, tagIdNameValidator);
		mappings.put(TagAction.RM_TAG_DELETEABLE, tagIdNameValidator);

		String categoryNameValidator = CategoryNameValidator.class.getName(), categoryIdNameValidator = idValidator
				+ "," + categoryNameValidator, categoryVersionValidator = categoryIdNameValidator
				+ "," + versionValidator;
		mappings.put(CategoryAction.RM_CATEGORY_ADD, categoryNameValidator);
		mappings.put(CategoryAction.RM_CATEGORY_UPDATE, baseValidator + ","
				+ categoryNameValidator);
		mappings.put(CategoryAction.RM_CATEGORY_LOCK, categoryVersionValidator);
		mappings.put(CategoryAction.RM_CATEGORY_UNLOCK,
				categoryVersionValidator);
		mappings.put(CategoryAction.RM_CATEGORY_REMOVE,
				categoryVersionValidator);
		mappings.put(CategoryAction.RM_CATEGORY_DELETE,
				categoryVersionValidator);
		mappings.put(CategoryAction.RM_CATEGORY_EXISTS, categoryNameValidator);
		mappings.put(CategoryAction.RM_CATEGORY_UPDATEABLE,
				categoryIdNameValidator);
		mappings.put(CategoryAction.RM_CATEGORY_LOCKED, categoryIdNameValidator);
		mappings.put(CategoryAction.RM_CATEGORY_REMOVEABLE,
				categoryIdNameValidator);
		mappings.put(CategoryAction.RM_CATEGORY_DELETEABLE,
				categoryIdNameValidator);

		String articleTitleValidator = ArticleTitleValidator.class.getName(), articleContentValidator = ArticleContentValidator.class
				.getName(), articleCategoryValidator = ArticleCategoryValidator.class
				.getName(), articleTagsValidator = ArticleTagsValidator.class
				.getName(), articleAUValidator = articleTitleValidator + ","
				+ articleContentValidator + "," + articleCategoryValidator
				+ "," + articleTagsValidator, articleIdTitleValidator = idValidator
				+ "," + articleTitleValidator, articleVersionValidator = articleIdTitleValidator
				+ "," + versionValidator, articleTagValidator = articleIdTitleValidator
				+ "," + tagIdNameValidator;

		mappings.put(ArticleAction.RM_ARTICLE_ADD, articleAUValidator);
		mappings.put(ArticleAction.RM_ARTICLE_UPDATE, baseValidator + ","
				+ articleAUValidator);
		mappings.put(ArticleAction.RM_ARTICLE_LOCK, articleVersionValidator);
		mappings.put(ArticleAction.RM_ARTICLE_UNLOCK, articleVersionValidator);
		mappings.put(ArticleAction.RM_ARTICLE_REMOVE, articleVersionValidator);
		mappings.put(ArticleAction.RM_ARTICLE_DELETE, articleVersionValidator);
		mappings.put(ArticleAction.RM_ARTICLE_EXISTS, articleTitleValidator);
		mappings.put(ArticleAction.RM_ARTICLE_UPDATEABLE,
				articleIdTitleValidator);
		mappings.put(ArticleAction.RM_ARTICLE_LOCKED, articleIdTitleValidator);
		mappings.put(ArticleAction.RM_ARTICLE_REMOVEABLE,
				articleIdTitleValidator);
		mappings.put(ArticleAction.RM_ARTICLE_DELETEABLE,
				articleIdTitleValidator);

		mappings.put(ArticleAction.RM_ARTICLE_TAG_ADD, articleTagValidator);
		mappings.put(ArticleAction.RM_ARTICLE_TAG_LOCK, articleTagValidator);
		mappings.put(ArticleAction.RM_ARTICLE_TAG_UNLOCK, articleTagValidator);
		mappings.put(ArticleAction.RM_ARTICLE_TAG_REMOVE, articleTagValidator);
		mappings.put(ArticleAction.RM_ARTICLE_TAG_DELETE, articleTagValidator);
		mappings.put(ArticleAction.RM_ARTICLE_TAG_EXISTS, articleTagValidator);
		mappings.put(ArticleAction.RM_ARTICLE_TAG_UPDATEABLE,
				articleTagValidator);
		mappings.put(ArticleAction.RM_ARTICLE_TAG_LOCKED, articleTagValidator);
		mappings.put(ArticleAction.RM_ARTICLE_TAG_REMOVEABLE,
				articleTagValidator);
		mappings.put(ArticleAction.RM_ARTICLE_TAG_DELETEABLE,
				articleTagValidator);

		String commentContentValidator = CommentContentValidator.class
				.getName(), commentArticleValidator = CommentArticleValidator.class
				.getName(), commentTargetValidator = CommentTargetValidator.class
				.getName(), commentACTValidator = commentArticleValidator + ","
				+ commentContentValidator + "," + commentTargetValidator, commentAVersionValidator = idValidator
				+ "," + commentArticleValidator + "," + versionValidator;
		mappings.put(CommentAction.RM_COMMENT_ADD, commentACTValidator);
		mappings.put(CommentAction.RM_COMMENT_UPDATE, baseValidator + ","
				+ commentACTValidator);
		mappings.put(CommentAction.RM_COMMENT_LOCK, commentAVersionValidator);
		mappings.put(CommentAction.RM_COMMENT_UNLOCK, commentAVersionValidator);
		mappings.put(CommentAction.RM_COMMENT_REMOVE, commentAVersionValidator);
		mappings.put(CommentAction.RM_COMMENT_DELETE, commentAVersionValidator);
		mappings.put(CommentAction.RM_COMMENT_UPDATEABLE,
				commentAVersionValidator);
		mappings.put(CommentAction.RM_COMMENT_LOCKED, commentAVersionValidator);
		mappings.put(CommentAction.RM_COMMENT_REMOVEABLE,
				commentAVersionValidator);
		mappings.put(CommentAction.RM_COMMENT_DELETEABLE,
				commentAVersionValidator);

		String tagPTagValidator = TagPermissionTagValidator.class.getName();
		String tagPRoleValidator = TagPermissionRoleValidator.class.getName(), tagPTRValidator = tagPTagValidator
				+ "," + tagPRoleValidator, tagPTRVersionValidator = tagPTRValidator
				+ "," + versionValidator;
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_ADD,
				tagPTRValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_UPDATE,
				baseValidator + "," + tagPTRValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_LOCK,
				tagPTRVersionValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_UNLOCK,
				tagPTRVersionValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_REMOVE,
				tagPTRVersionValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_DELETE,
				tagPTRVersionValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_EXISTS,
				tagPTRValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_UPDATEABLE,
				tagPTRValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_LOCKED,
				tagPTRValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_REMOVEABLE,
				tagPTRValidator);
		mappings.put(TagRolePermissionAction.RM_TAG_ROLE_PERMISSION_DELETEABLE,
				tagPTRValidator);

		String tagPUserValidator = TagPermissionUserValidator.class.getName(), tagPTUValidator = tagPTagValidator
				+ "," + tagPUserValidator, tagPTUVersionValidator = tagPTUValidator
				+ "," + versionValidator;
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_ADD,
				tagPTUValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_UPDATE,
				baseValidator + "," + tagPTUValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_LOCK,
				tagPTUVersionValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_UNLOCK,
				tagPTUVersionValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_REMOVE,
				tagPTUVersionValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_DELETE,
				tagPTUVersionValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_EXISTS,
				tagPTUValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_UPDATEABLE,
				tagPTUValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_LOCKED,
				tagPTUValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_REMOVEABLE,
				tagPTUValidator);
		mappings.put(TagUserPermissionAction.RM_TAG_USER_PERMISSION_DELETEABLE,
				tagPTUValidator);

		String categoryPCategoryValidator = CategoryPermissionCategoryValidator.class
				.getName();
		String categoryPRoleValidator = CategoryPermissionRoleValidator.class
				.getName(), categoryPCRValidator = categoryPCategoryValidator
				+ "," + categoryPRoleValidator, categoryPCRVersionValidator = categoryPCRValidator
				+ "," + versionValidator;
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_ADD,
				categoryPCRValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_UPDATE,
				baseValidator + "," + categoryPCRValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_LOCK,
				categoryPCRVersionValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_UNLOCK,
				categoryPCRVersionValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_REMOVE,
				categoryPCRVersionValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_DELETE,
				categoryPCRVersionValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_EXISTS,
				categoryPCRValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_UPDATEABLE,
				categoryPCRValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_LOCKED,
				categoryPCRValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_REMOVEABLE,
				categoryPCRValidator);
		mappings.put(
				CategoryRolePermissionAction.RM_CATEGORY_ROLE_PERMISSION_DELETEABLE,
				categoryPCRValidator);

		String categoryPUserValidator = CategoryPermissionUserValidator.class
				.getName(), categoryPCUValidator = categoryPCategoryValidator
				+ "," + categoryPUserValidator, categoryPCUVersionValidator = categoryPCUValidator
				+ "," + versionValidator;
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_ADD,
				categoryPCUValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_UPDATE,
				baseValidator + "," + categoryPCUValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_LOCK,
				categoryPCUVersionValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_UNLOCK,
				categoryPCUVersionValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_REMOVE,
				categoryPCUVersionValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_DELETE,
				categoryPCUVersionValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_EXISTS,
				categoryPCUValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_UPDATEABLE,
				categoryPCUValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_LOCKED,
				categoryPCUValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_REMOVEABLE,
				categoryPCUValidator);
		mappings.put(
				CategoryUserPermissionAction.RM_CATEGORY_USER_PERMISSION_DELETEABLE,
				categoryPCUValidator);

		String articlePArticleValidator = ArticlePermissionArticleValidator.class
				.getName();
		String articlePRoleValidator = ArticlePermissionRoleValidator.class
				.getName(), articlePARValidator = articlePArticleValidator
				+ "," + articlePRoleValidator, articlePARVersionValidator = articlePARValidator
				+ "," + versionValidator;
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_ADD,
				articlePARValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_UPDATE,
				baseValidator + "," + articlePARValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_LOCK,
				articlePARVersionValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_UNLOCK,
				articlePARVersionValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_REMOVE,
				articlePARVersionValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_DELETE,
				articlePARVersionValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_EXISTS,
				articlePARValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_UPDATEABLE,
				articlePARValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_LOCKED,
				articlePARValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_REMOVEABLE,
				articlePARValidator);
		mappings.put(
				ArticleRolePermissionAction.RM_ARTICLE_ROLE_PERMISSION_DELETEABLE,
				articlePARValidator);

		String articlePUserValidator = ArticlePermissionUserValidator.class
				.getName(), articlePAUValidator = articlePArticleValidator
				+ "," + articlePUserValidator, articlePAUVersionValidator = articlePAUValidator
				+ "," + versionValidator;
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_ADD,
				articlePAUValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_UPDATE,
				baseValidator + "," + articlePAUValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_LOCK,
				articlePAUVersionValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_UNLOCK,
				articlePAUVersionValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_REMOVE,
				articlePAUVersionValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_DELETE,
				articlePAUVersionValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_EXISTS,
				articlePAUValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_UPDATEABLE,
				articlePAUValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_LOCKED,
				articlePAUValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_REMOVEABLE,
				articlePAUValidator);
		mappings.put(
				ArticleUserPermissionAction.RM_ARTICLE_USER_PERMISSION_DELETEABLE,
				articlePAUValidator);

		return mappings;
	}

	/**
	 * @see org.xhome.validator.mapping.Mapping#codeMappings()
	 */
	@Override
	public Map<String, Short> codeMappings() {
		Map<String, Short> codes = new HashMap<String, Short>();
		codes.put(TagNameValidator.CODE_NAME_EMPTY, (short) 101);
		codes.put(TagNameValidator.CODE_NAME_SIZE, (short) 102);

		codes.put(CategoryNameValidator.CODE_NAME_EMPTY, (short) 103);
		codes.put(CategoryNameValidator.CODE_NAME_SIZE, (short) 104);

		codes.put(ArticleTitleValidator.CODE_TITLE_EMPTY, (short) 105);
		codes.put(ArticleTitleValidator.CODE_TITLE_SIZE, (short) 106);
		codes.put(ArticleContentValidator.CODE_CONTENT_EMPTY, (short) 107);
		codes.put(ArticleContentValidator.CODE_CONTENT_SIZE, (short) 108);
		codes.put(ArticleCategoryValidator.CODE_CATEGORY_EMPTY, (short) 109);

		codes.put(CommentArticleValidator.CODE_ARTICLE_EMPTY, (short) 110);
		codes.put(CommentContentValidator.CODE_CONTENT_EMPTY, (short) 111);
		codes.put(CommentContentValidator.CODE_CONTENT_SIZE, (short) 112);
		codes.put(CommentTargetValidator.CODE_TARGET_EMPTY, (short) 113);

		codes.put(TagPermissionTagValidator.CODE_TAG_EMPTY, (short) 114);
		codes.put(TagPermissionRoleValidator.CODE_ROLE_EMPTY, (short) 115);
		codes.put(TagPermissionUserValidator.CODE_USER_EMPTY, (short) 116);

		codes.put(CategoryPermissionCategoryValidator.CODE_CATEGORY_EMPTY,
				(short) 117);
		codes.put(CategoryPermissionRoleValidator.CODE_ROLE_EMPTY, (short) 118);
		codes.put(CategoryPermissionUserValidator.CODE_USER_EMPTY, (short) 119);

		codes.put(ArticlePermissionArticleValidator.CODE_ARTICLE_EMPTY,
				(short) 120);
		codes.put(ArticlePermissionRoleValidator.CODE_ROLE_EMPTY, (short) 121);
		codes.put(ArticlePermissionUserValidator.CODE_USER_EMPTY, (short) 122);
		return codes;
	}

}
