package org.xhome.xblog.web.util;

import org.xhome.xblog.web.validator.ArticleCategoryValidator;
import org.xhome.xblog.web.validator.ArticleContentValidator;
import org.xhome.xblog.web.validator.ArticleTitleValidator;
import org.xhome.xblog.web.validator.CategoryNameValidator;
import org.xhome.xblog.web.validator.TagNameValidator;

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
	}
	
}
