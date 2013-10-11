package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.validator.IdValidator;
import org.xhome.validator.Validator;
import org.xhome.validator.ValidatorMapping;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Comment;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Oct 7, 201311:18:29 PM
 * @describe 
 */
public class CommentTargetValidator extends Validator {
	
	private Validator			idValidator;
	
	public final static String	FIELD_TARGET		= "target";
	
	public final static String	CODE_TARGET_EMPTY	= "target.empty";
	
	public CommentTargetValidator() {
		ValidatorMapping validatorMapping = ValidatorMapping.getInstance();
		idValidator = validatorMapping.getValidatorByName(IdValidator.class.getName());
	}
	
	@Override
	public boolean validate(Object target, Errors errors) {
		Comment comment = (Comment) target;
		Comment ta = comment == null ? null : comment.getTarget();
		if (ta == null) {
			errors.rejectValue(FIELD_TARGET, CODE_TARGET_EMPTY, validationConfig.getConfig(BlogValidatorConfig.COMMENT_TARGET_EMPTY_MESSAGE));
			return false;
		}
		return idValidator.validate(ta, errors);
	}
	
}
