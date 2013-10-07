package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.util.StringUtils;
import org.xhome.validator.Validator;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Article;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Oct 7, 201310:52:15 PM
 * @describe 
 */
public class ArticleContentValidator extends Validator {

	public final static String	FIELD_CONTENT			= "content";
	
	public final static String	CODE_CONTENT_EMPTY		= "content.empty";
	public final static String	CODE_CONTENT_SIZE		= "content.size";
	
	@Override
	public boolean validate(Object target, Errors errors) {
		Article article = (Article) target;
		String content = article == null ? null : article.getTitle();
		if (StringUtils.isEmpty(content)) {
			errors.rejectValue(FIELD_CONTENT, CODE_CONTENT_EMPTY, validationConfig.getConfig(BlogValidatorConfig.ARTICLE_CONTENT_EMPTY_MESSAGE));
		} else {
			int size = content.length();
			int min = Integer.parseInt(validationConfig.getConfig(BlogValidatorConfig.ARTICLE_CONTENT_SIZE_MIN)),
				max = Integer.parseInt(validationConfig.getConfig(BlogValidatorConfig.ARTICLE_CONTENT_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(FIELD_CONTENT, CODE_CONTENT_SIZE, validationConfig.getConfig(BlogValidatorConfig.ARTICLE_CONTENT_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

	
}
