package org.xhome.xblog.web.util;

import org.xhome.xblog.web.validator.ArticleCategoryValidator;
import org.xhome.xblog.web.validator.ArticleContentValidator;
import org.xhome.xblog.web.validator.ArticlePermissionArticleValidator;
import org.xhome.xblog.web.validator.ArticlePermissionRoleValidator;
import org.xhome.xblog.web.validator.ArticlePermissionUserValidator;
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
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 28, 201310:46:52 PM
 * @describe 
 */
public class ValidatorUtils extends org.xhome.xauth.web.util.ValidatorUtils {

	static {
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
		
		codes.put(CategoryPermissionCategoryValidator.CODE_CATEGORY_EMPTY, (short) 117);
		codes.put(CategoryPermissionRoleValidator.CODE_ROLE_EMPTY, (short) 118);
		codes.put(CategoryPermissionUserValidator.CODE_USER_EMPTY, (short) 119);
		
		codes.put(ArticlePermissionArticleValidator.CODE_ARTICLE_EMPTY, (short) 120);
		codes.put(ArticlePermissionRoleValidator.CODE_ROLE_EMPTY, (short) 121);
		codes.put(ArticlePermissionUserValidator.CODE_USER_EMPTY, (short) 122);
	}
	
}
