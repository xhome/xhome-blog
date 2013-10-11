package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xauth.User;
import org.xhome.xauth.web.validator.UserNameValidator;
import org.xhome.xblog.ArticleUserPermission;

/**
 * @project xauth-web
 * @author jhat
 * @email cpf624@126.com
 * @date Aug 14, 20139:37:50 PM
 * @description 
 */
public class ArticlePermissionUserValidator extends Validator {
	
	private Validator	idValidator;
	private Validator	userNameValidator;
	
	public final static String	FIELD_USER			= "user";
	
	public final static String	CODE_USER_EMPTY		= "user.empty";
	
	public ArticlePermissionUserValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class.getName());
		userNameValidator = validatorMapping.getValidatorByName(UserNameValidator.class.getName());
	}
	
	@Override
	public boolean validate(Object target, Errors errors) {
		ArticleUserPermission permission = (ArticleUserPermission) target;
		User user = permission == null ? null : permission.getUser();
		if (user == null) {
			errors.rejectValue(FIELD_USER, CODE_USER_EMPTY, validationConfig.getConfig(BlogValidatorConfig.ARTICLE_PERMISSION_USER_EMPTY_MESSAGE));
			return false;
		}
		return idValidator.validate(user, errors) && userNameValidator.validate(user, errors);
	}

	
}
