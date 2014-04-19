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
 * @describe 评论用户的个人主页校验
 */
public class MessageUserWebsiteValidator extends Validator {

    public final static String FIELD_USER_WEBSITE        = "userWebsite";

    public final static String CODE_USER_WEBSITE_EMPTY   = "userWebsite.empty";
    public final static String CODE_USER_WEBSITE_SIZE    = "userWebsite.size";
    public final static String CODE_USER_WEBSITE_PATTERN = "userEmail.pattern";

    @Override
    public boolean validate(Object target, Errors errors) {
        Message message = (Message) target;
        String userWebsite = message == null ? null : message.getUserWebsite();
        if (StringUtils.isEmpty(userWebsite)) {
            return true;
        } else {
            int size = userWebsite.length();
            int min = Integer
                            .parseInt(validationConfig
                                            .getConfig(BlogValidatorConfig.MESSAGE_USER_WEBSITE_SIZE_MIN)), max = Integer
                            .parseInt(validationConfig
                                            .getConfig(BlogValidatorConfig.MESSAGE_USER_WEBSITE_SIZE_MAX));
            if (size < min || size > max) {
                errors.rejectValue(
                                FIELD_USER_WEBSITE,
                                CODE_USER_WEBSITE_SIZE,
                                validationConfig.getConfig(BlogValidatorConfig.MESSAGE_USER_WEBSITE_SIZE_MESSAGE));
            } else {
                if (!userWebsite.matches(validationConfig
                                .getConfig(BlogValidatorConfig.MESSAGE_USER_WEBSITE_PATTERN_REGEXP))) {
                    errors.rejectValue(
                                    FIELD_USER_WEBSITE,
                                    CODE_USER_WEBSITE_PATTERN,
                                    validationConfig.getConfig(BlogValidatorConfig.MESSAGE_USER_WEBSITE_PATTERN_MESSAGE));
                } else {
                    return true;
                }
            }
        }
        return false;
    }

}
