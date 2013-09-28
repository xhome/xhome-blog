package org.xhome.validator.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 28, 20139:17:04 PM
 * @describe 
 */
public class BlogValidatorConfig implements Config {
	
	public final static String		TAG_NAME_EMPTY_MESSAGE			= "xblog_tag_name_empty_message";
	public final static String		TAG_NAME_SIZE_MIN				= "xblog_tag_name_size_min";
	public final static String		TAG_NAME_SIZE_MAX				= "xblog_tag_name_size_max";
	public final static String		TAG_NAME_SIZE_MESSAGE			= "xblog_tag_name_size_message";
	
	public final static String		CATEGORY_NAME_EMPTY_MESSAGE		= "xblog_category_name_empty_message";
	public final static String		CATEGORY_NAME_SIZE_MIN			= "xblog_category_name_size_min";
	public final static String		CATEGORY_NAME_SIZE_MAX			= "xblog_category_name_size_max";
	public final static String		CATEGORY_NAME_SIZE_MESSAGE		= "xblog_category_name_size_message";
	
	public Map<String, String> validatorConfigs() {
		Map<String, String> configs = new HashMap<String, String>();
		
		configs.put(TAG_NAME_EMPTY_MESSAGE, "标签名不能为空");
		configs.put(TAG_NAME_SIZE_MIN, "0");
		configs.put(TAG_NAME_SIZE_MAX, "30");
		configs.put(TAG_NAME_SIZE_MESSAGE, "标签名不能超过30个字符");
		
		configs.put(CATEGORY_NAME_EMPTY_MESSAGE, "栏目名不能为空");
		configs.put(CATEGORY_NAME_SIZE_MIN, "0");
		configs.put(CATEGORY_NAME_SIZE_MAX, "30");
		configs.put(CATEGORY_NAME_SIZE_MESSAGE, "栏目名不能超过30个字符");
		
		return configs;
	}

}
