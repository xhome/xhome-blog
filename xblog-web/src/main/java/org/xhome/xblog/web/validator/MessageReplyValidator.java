package org.xhome.xblog.web.validator;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
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
 * @describe
 */
public class MessageReplyValidator extends Validator {

    public final static String     FIELD_REPLY      = "reply";

    public final static String     CODE_REPLY_EMPTY = "reply.empty";
    public final static String     CODE_REPLY_SIZE  = "reply.size";

    private final static Whitelist htmlWhitelist    = Whitelist.relaxed()
                                                                    .addTags("div",
                                                                                    "span")
                                                                    .addAttributes("div",
                                                                                    "style")
                                                                    .addAttributes("table",
                                                                                    "style",
                                                                                    "cellpadding",
                                                                                    "cellspacing",
                                                                                    "width")
                                                                    .addAttributes("tr",
                                                                                    "style")
                                                                    .addAttributes("td",
                                                                                    "style",
                                                                                    "height",
                                                                                    "nowrap",
                                                                                    "width")
                                                                    .addAttributes("p",
                                                                                    "style")
                                                                    .addAttributes("span",
                                                                                    "style")
                                                                    .addAttributes("img",
                                                                                    "style",
                                                                                    "width")
                                                                    .addAttributes("ul",
                                                                                    "style")
                                                                    .addAttributes("ol",
                                                                                    "style")
                                                                    .addAttributes("li",
                                                                                    "style")
                                                                    .addAttributes("a",
                                                                                    "href");

    @Override
    public boolean validate(Object target, Errors errors) {
        Message message = (Message) target;
        String reply = message == null ? null : message.getContent();
        if (StringUtils.isEmpty(reply)) {
            errors.rejectValue(
                            FIELD_REPLY,
                            CODE_REPLY_EMPTY,
                            validationConfig.getConfig(BlogValidatorConfig.MESSAGE_REPLY_EMPTY_MESSAGE));
        } else {
            // 去除评论内容中不安全的HTML标签，放置XSS攻击
            reply = Jsoup.clean(reply, htmlWhitelist);
            message.setContent(reply);
            int size = reply.length();
            int min = Integer
                            .parseInt(validationConfig
                                            .getConfig(BlogValidatorConfig.MESSAGE_REPLY_SIZE_MIN)), max = Integer
                            .parseInt(validationConfig
                                            .getConfig(BlogValidatorConfig.MESSAGE_REPLY_SIZE_MAX));
            if (size < min || size > max) {
                errors.rejectValue(
                                FIELD_REPLY,
                                CODE_REPLY_SIZE,
                                validationConfig.getConfig(BlogValidatorConfig.MESSAGE_REPLY_SIZE_MESSAGE));
            } else {
                return true;
            }
        }
        return false;
    }

}
