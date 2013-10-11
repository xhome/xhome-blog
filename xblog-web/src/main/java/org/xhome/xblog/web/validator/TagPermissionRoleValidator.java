package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xauth.Role;
import org.xhome.xauth.web.validator.RoleNameValidator;
import org.xhome.xblog.TagRolePermission;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 
 */
public class TagPermissionRoleValidator extends Validator {
	
	private Validator	idValidator;
	private Validator	roleNameValidator;
	
	public final static String	FIELD_ROLE			= "role";
	
	public final static String	CODE_ROLE_EMPTY		= "role.empty";
	
	public TagPermissionRoleValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class.getName());
		roleNameValidator = validatorMapping.getValidatorByName(RoleNameValidator.class.getName());
	}
	
	@Override
	public boolean validate(Object target, Errors errors) {
		TagRolePermission permission = (TagRolePermission) target;
		Role role = permission == null ? null : permission.getRole();
		if (role == null) {
			errors.rejectValue(FIELD_ROLE, CODE_ROLE_EMPTY, validationConfig.getConfig(BlogValidatorConfig.TAG_PERMISSION_ROLE_EMPTY_MESSAGE));
			return false;
		}
		return idValidator.validate(role, errors) && roleNameValidator.validate(role, errors);
	}

	
}
