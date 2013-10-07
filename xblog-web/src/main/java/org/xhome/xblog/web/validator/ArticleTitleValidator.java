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
public class ArticleTitleValidator extends Validator {

	public final static String	FIELD_TITLE			= "title";
	
	public final static String	CODE_TITLE_EMPTY		= "title.empty";
	public final static String	CODE_TITLE_SIZE		= "title.size";
	
	@Override
	public boolean validate(Object target, Errors errors) {
		Article article = (Article) target;
		String title = article == null ? null : article.getTitle();
		if (StringUtils.isEmpty(title)) {
			errors.rejectValue(FIELD_TITLE, CODE_TITLE_EMPTY, validationConfig.getConfig(BlogValidatorConfig.ARTICLE_TITLE_EMPTY_MESSAGE));
		} else {
			int size = title.length();
			int min = Integer.parseInt(validationConfig.getConfig(BlogValidatorConfig.ARTICLE_TITLE_SIZE_MIN)),
				max = Integer.parseInt(validationConfig.getConfig(BlogValidatorConfig.ARTICLE_TITLE_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(FIELD_TITLE, CODE_TITLE_SIZE, validationConfig.getConfig(BlogValidatorConfig.ARTICLE_TITLE_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

	
}
