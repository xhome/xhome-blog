package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.common.util.StringUtils;
import org.xhome.validator.Validator;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Message;

/**
 * @project xblog-web
 * @author jhat
 * @email cpf624@126.com
 * @date Oct 7, 201310:52:15 PM
 * @describe 评论用户的邮箱校验
 */
public class MessageUserEmailValidator extends Validator {

    public final static String FIELD_USER_EMAIL        = "userEmail";

    public final static String CODE_USER_EMAIL_EMPTY   = "userEmail.empty";
    public final static String CODE_USER_EMAIL_SIZE    = "userEmail.size";
    public final static String CODE_USER_EMAIL_PATTERN = "userEmail.pattern";

    @Override
    public boolean validate(Object target, Errors errors) {
        Message message = (Message) target;
        String userEmail = message == null ? null : message.getUserEmail();
        if (StringUtils.isEmpty(userEmail)) {
            errors.rejectValue(
                            FIELD_USER_EMAIL,
                            CODE_USER_EMAIL_EMPTY,
                            validationConfig.getConfig(BlogValidatorConfig.MESSAGE_USER_EMAIL_EMPTY_MESSAGE));
        } else {
            int size = userEmail.length();
            int min = Integer
                            .parseInt(validationConfig
                                            .getConfig(BlogValidatorConfig.MESSAGE_USER_EMAIL_SIZE_MIN)), max = Integer
                            .parseInt(validationConfig
                                            .getConfig(BlogValidatorConfig.MESSAGE_USER_EMAIL_SIZE_MAX));
            if (size < min || size > max) {
                errors.rejectValue(
                                FIELD_USER_EMAIL,
                                CODE_USER_EMAIL_SIZE,
                                validationConfig.getConfig(BlogValidatorConfig.MESSAGE_USER_EMAIL_SIZE_MESSAGE));
            } else {
                if (!userEmail.matches(validationConfig
                                .getConfig(BlogValidatorConfig.MESSAGE_USER_EMAIL_PATTERN_REGEXP))) {
                    errors.rejectValue(
                                    FIELD_USER_EMAIL,
                                    CODE_USER_EMAIL_PATTERN,
                                    validationConfig.getConfig(BlogValidatorConfig.MESSAGE_USER_EMAIL_PATTERN_MESSAGE));
                } else {
                    return true;
                }
            }
        }
        return false;
    }

}
