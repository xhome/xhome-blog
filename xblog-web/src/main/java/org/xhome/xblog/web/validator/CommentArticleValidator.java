package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Article;
import org.xhome.xblog.Comment;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Oct 7, 201311:18:29 PM
 * @describe 
 */
public class CommentArticleValidator extends Validator {
	
	private Validator			idValidator;
	
	public final static String	FIELD_ARTICLE		= "article";
	
	public final static String	CODE_ARTICLE_EMPTY	= "article.empty";
	
	public CommentArticleValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class.getName());
	}
	
	@Override
	public boolean validate(Object target, Errors errors) {
		Comment comment = (Comment) target;
		Article article = comment == null ? null : comment.getArticle();
		if (article == null) {
			errors.rejectValue(FIELD_ARTICLE, CODE_ARTICLE_EMPTY, validationConfig.getConfig(BlogValidatorConfig.COMMENT_ARTICLE_EMPTY_MESSAGE));
			return false;
		}
		return idValidator.validate(article, errors);
	}
	
}
