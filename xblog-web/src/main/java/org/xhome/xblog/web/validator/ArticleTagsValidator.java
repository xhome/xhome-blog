package org.xhome.xblog.web.validator;

import java.util.List;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.xblog.Article;
import org.xhome.xblog.Tag;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Oct 7, 201311:18:29 PM
 * @describe 
 */
public class ArticleTagsValidator extends Validator {
	
	private Validator			idValidator;
	private Validator			tagNameValidator;
	
	public ArticleTagsValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class.getName());
		tagNameValidator = validatorMapping.getValidatorByName(TagNameValidator.class.getName());
	}
	
	@Override
	public boolean validate(Object target, Errors errors) {
		Article article = (Article) target;
		List<Tag> tags = article == null ? null : article.getTags();
		if (tags != null && (!tags.isEmpty())) {
			for (Tag tag : tags) {
				if (idValidator.validate(tag, errors) && tagNameValidator.validate(tag, errors)) {
					continue;
				}
				return false;
			}
		}
		return true;
	}
	
}
