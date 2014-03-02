package org.xhome.xblog.web.validator;

import org.springframework.validation.Errors;
import org.xhome.util.StringUtils;
import org.xhome.validator.Validator;
import org.xhome.validator.config.BlogValidatorConfig;
import org.xhome.xblog.Article;

/**
 * @project xblog-web
 * @author jhat
 * @email cpf624@126.com
 * @date Oct 7, 201310:52:15 PM
 * @describe
 */
public class ArticleDetailValidator extends Validator {

	public final static String FIELD_DETAIL = "detail";

	public final static String CODE_DETAIL_EMPTY = "detail.empty";
	public final static String CODE_DETAIL_SIZE = "detail.size";

	@Override
	public boolean validate(Object target, Errors errors) {
		Article article = (Article) target;
		String detail = article == null ? null : article.getDetail();
		if (StringUtils.isEmpty(detail)) {
			errors.rejectValue(
					FIELD_DETAIL,
					CODE_DETAIL_EMPTY,
					validationConfig
							.getConfig(BlogValidatorConfig.ARTICLE_DETAIL_EMPTY_MESSAGE));
		} else {
			int size = detail.length();
			int min = Integer.parseInt(validationConfig
					.getConfig(BlogValidatorConfig.ARTICLE_DETAIL_SIZE_MIN)), max = Integer
					.parseInt(validationConfig
							.getConfig(BlogValidatorConfig.ARTICLE_DETAIL_SIZE_MAX));
			if (size < min || size > max) {
				errors.rejectValue(
						FIELD_DETAIL,
						CODE_DETAIL_SIZE,
						validationConfig
								.getConfig(BlogValidatorConfig.ARTICLE_DETAIL_SIZE_MESSAGE));
			} else {
				return true;
			}
		}
		return false;
	}

}
