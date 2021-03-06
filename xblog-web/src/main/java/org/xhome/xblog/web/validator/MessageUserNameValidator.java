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
public class MessageUserNameValidator extends Validator {

    public final static String FIELD_USER_NAME      = "userName";

    public final static String CODE_USER_NAME_EMPTY = "userName.empty";
    public final static String CODE_USER_NAME_SIZE  = "userName.size";

    @Override
    public boolean validate(Object target, Errors errors) {
        Message message = (Message) target;
        String userName = message == null ? null : message.getUserName();
        if (StringUtils.isEmpty(userName)) {
            errors.rejectValue(
                            FIELD_USER_NAME,
                            CODE_USER_NAME_EMPTY,
                            validationConfig.getConfig(BlogValidatorConfig.MESSAGE_USER_NAME_EMPTY_MESSAGE));
        } else {
            int size = userName.length();
            int min = Integer
                            .parseInt(validationConfig
                                            .getConfig(BlogValidatorConfig.MESSAGE_USER_NAME_SIZE_MIN)), max = Integer
                            .parseInt(validationConfig
                                            .getConfig(BlogValidatorConfig.MESSAGE_USER_NAME_SIZE_MAX));
            if (size < min || size > max) {
                errors.rejectValue(
                                FIELD_USER_NAME,
                                CODE_USER_NAME_SIZE,
                                validationConfig.getConfig(BlogValidatorConfig.MESSAGE_USER_NAME_SIZE_MESSAGE));
            } else {
                return true;
            }
        }
        return false;
    }

}
