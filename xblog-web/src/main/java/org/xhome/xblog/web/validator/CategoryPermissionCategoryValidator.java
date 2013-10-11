package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryPermission;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 
 */
public class CategoryPermissionCategoryValidator extends Validator {
	
	private Validator	idValidator;
	private Validator	categoryNameValidator;
	
	public final static String	FIELD_CATEGORY			= "category";
	
	public final static String	CODE_CATEGORY_EMPTY		= "category.empty";
	
	public CategoryPermissionCategoryValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class.getName());
		categoryNameValidator = validatorMapping.getValidatorByName(CategoryNameValidator.class.getName());
	}
	
	@Override
	public boolean validate(Object target, Errors errors) {
		CategoryPermission permission = (CategoryPermission) target;
		Category category = permission == null ? null : permission.getCategory();
		if (category == null) {
			errors.rejectValue(FIELD_CATEGORY, CODE_CATEGORY_EMPTY, validationConfig.getConfig(BlogValidatorConfig.CATEGORY_PERMISSION_CATEGORY_EMPTY_MESSAGE));
			return false;
		}
		return idValidator.validate(category, errors) && categoryNameValidator.validate(category, errors);
	}

	
}
