package org.xhome.xblog.web.util;

import org.xhome.xblog.web.validator.CategoryNameValidator;
import org.xhome.xblog.web.validator.TagNameValidator;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 28, 201310:46:52 PM
 * @describe 
 */
public class ValidatorUtils extends org.xhome.xauth.web.util.ValidatorUtils {

	static {
		codes.put(TagNameValidator.CODE_NAME_EMPTY, (short) 100);
		codes.put(TagNameValidator.CODE_NAME_SIZE, (short) 101);
		
		codes.put(CategoryNameValidator.CODE_NAME_EMPTY, (short) 102);
		codes.put(CategoryNameValidator.CODE_NAME_SIZE, (short) 103);
	}
	
}
