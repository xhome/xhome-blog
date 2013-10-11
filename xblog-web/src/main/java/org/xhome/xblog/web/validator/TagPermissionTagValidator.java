package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagPermission;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 
 */
public class TagPermissionTagValidator extends Validator {
	
	private Validator	idValidator;
	private Validator	tagNameValidator;
	
	public final static String	FIELD_TAG			= "tag";
	
	public final static String	CODE_TAG_EMPTY		= "tag.empty";
	
	public TagPermissionTagValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class.getName());
		tagNameValidator = validatorMapping.getValidatorByName(TagNameValidator.class.getName());
	}
	
	@Override
	public boolean validate(Object target, Errors errors) {
		TagPermission permission = (TagPermission) target;
		Tag tag = permission == null ? null : permission.getTag();
		if (tag == null) {
			errors.rejectValue(FIELD_TAG, CODE_TAG_EMPTY, validationConfig.getConfig(BlogValidatorConfig.TAG_PERMISSION_TAG_EMPTY_MESSAGE));
			return false;
		}
		return idValidator.validate(tag, errors) && tagNameValidator.validate(tag, errors);
	}

	
}
