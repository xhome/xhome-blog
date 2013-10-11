package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticlePermission;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 
 */
public class ArticlePermissionArticleValidator extends Validator {
	
	private Validator	idValidator;
	private Validator	articleTitleValidator;
	
	public final static String	FIELD_ARTICLE			= "article";
	
	public final static String	CODE_ARTICLE_EMPTY		= "article.empty";
	
	public ArticlePermissionArticleValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class.getName());
		articleTitleValidator = validatorMapping.getValidatorByName(ArticleTitleValidator.class.getName());
	}
	
	@Override
	public boolean validate(Object target, Errors errors) {
		ArticlePermission permission = (ArticlePermission) target;
		Article article = permission == null ? null : permission.getArticle();
		if (article == null) {
			errors.rejectValue(FIELD_ARTICLE, CODE_ARTICLE_EMPTY, validationConfig.getConfig(BlogValidatorConfig.ARTICLE_PERMISSION_ARTICLE_EMPTY_MESSAGE));
			return false;
		}
		return idValidator.validate(article, errors) && articleTitleValidator.validate(article, errors);
	}

	
}
