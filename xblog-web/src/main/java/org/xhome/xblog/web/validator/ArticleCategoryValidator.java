package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Article;
import org.xhome.xblog.Category;

/**
 * @project xblog-web
 * @author jhat
 * @email cpf624@126.com
 * @date Oct 7, 201311:18:29 PM
 * @describe
 */
public class ArticleCategoryValidator extends Validator {

	private Validator idValidator;
	private Validator categoryNameValidator;

	public final static String FIELD_CATEGORY = "category";

	public final static String CODE_CATEGORY_EMPTY = "category.empty";

	public ArticleCategoryValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class
				.getName());
		categoryNameValidator = validatorMapping
				.getValidatorByName(CategoryNameValidator.class.getName());
	}

	@Override
	public boolean validate(Object target, Errors errors) {
		Article article = (Article) target;
		Category category = article == null ? null : article.getCategory();
		if (category == null) {
			errors.rejectValue(
					FIELD_CATEGORY,
					CODE_CATEGORY_EMPTY,
					validationConfig
							.getConfig(BlogValidatorConfig.ARTICLE_CATEGORY_EMPTY_MESSAGE));
			return false;
		}
		return idValidator.validate(category, errors)
				&& categoryNameValidator.validate(category, errors);
	}

}
