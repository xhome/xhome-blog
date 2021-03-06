package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.common.util.StringUtils;
import org.xhome.validator.Validator;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Category;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 
 */
public class CategoryNameValidator extends Validator {

	public final static String	FIELD_NAME			= "name";
	
	public final static String	CODE_NAME_EMPTY		= "name.empty";
	public final static String	CODE_NAME_SIZE		= "name.size";
	
	@Override
	public boolean validate(Object target, Errors errors) {
		Category category = (Category) target;
		String name = category == null ? null : category.getName();
		if (StringUtils.isEmpty(name)) {
			errors.rejectValue(FIELD_NAME, CODE_NAME_EMPTY, validationConfig.getConfig(BlogValidatorConfig.CATEGORY_NAME_EMPTY_MESSAGE));
		} else {
			int size = name.length();
			int min = Integer.parseInt(validationConfig.getConfig(BlogValidatorConfig.CATEGORY_NAME_SIZE_MIN)),
				max = Integer.parseInt(validationConfig.getConfig(BlogValidatorConfig.CATEGORY_NAME_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(FIELD_NAME, CODE_NAME_SIZE, validationConfig.getConfig(BlogValidatorConfig.CATEGORY_NAME_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

	
}
