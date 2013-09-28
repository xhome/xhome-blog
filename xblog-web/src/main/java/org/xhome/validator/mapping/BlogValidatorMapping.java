package org.xhome.validator.mapping;

import java.util.HashMap;
import java.util.Map;

import org.xhome.validator.IdValidator;
import org.xhome.validator.StatusValidator;
import org.xhome.validator.VersionValidator;
import org.xhome.xblog.web.action.CategoryAction;
import org.xhome.xblog.web.action.TagAction;
import org.xhome.xblog.web.validator.CategoryNameValidator;
import org.xhome.xblog.web.validator.TagNameValidator;

/**
 * @project xblog-web
 * @author 	jhat
 * @email 	cpf624@126.com
 * @date 	Sep 28, 20139:16:59 PM
 * @describe 
 */
public class BlogValidatorMapping implements Mapping {

	@Override
	public Map<String, String> validatorMappings() {
		Map<String, String> mappings = new HashMap<String, String>();
		String idValidator = IdValidator.class.getName(),
				versionValidator = VersionValidator.class.getName(),
				baseValidator = idValidator + "," + versionValidator + "," + StatusValidator.class.getName();
		
		String tagNameValidator = TagNameValidator.class.getName(),
			tagIdNameValidator = idValidator + "," + tagNameValidator,
			tagVersionValidator = tagIdNameValidator + "," + versionValidator;
		mappings.put(TagAction.RM_TAG_ADD, tagNameValidator);
		mappings.put(TagAction.RM_TAG_UPDATE, baseValidator + "," + tagNameValidator);
		mappings.put(TagAction.RM_TAG_LOCK, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_UNLOCK, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_REMOVE, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_DELETE, tagVersionValidator);
		mappings.put(TagAction.RM_TAG_EXISTS, tagNameValidator);
		mappings.put(TagAction.RM_TAG_UPDATEABLE, tagIdNameValidator);
		mappings.put(TagAction.RM_TAG_LOCKED, tagIdNameValidator);
		mappings.put(TagAction.RM_TAG_REMOVEABLE, tagIdNameValidator);
		mappings.put(TagAction.RM_TAG_DELETEABLE, tagIdNameValidator);
		
		String categoryNameValidator = CategoryNameValidator.class.getName(),
				categoryIdNameValidator = idValidator + "," + categoryNameValidator,
				categoryVersionValidator = categoryIdNameValidator + "," + versionValidator;
			mappings.put(CategoryAction.RM_CATEGORY_ADD, categoryNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_UPDATE, baseValidator + "," + categoryNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_LOCK, categoryVersionValidator);
			mappings.put(CategoryAction.RM_CATEGORY_UNLOCK, categoryVersionValidator);
			mappings.put(CategoryAction.RM_CATEGORY_REMOVE, categoryVersionValidator);
			mappings.put(CategoryAction.RM_CATEGORY_DELETE, categoryVersionValidator);
			mappings.put(CategoryAction.RM_CATEGORY_EXISTS, categoryNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_UPDATEABLE, categoryIdNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_LOCKED, categoryIdNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_REMOVEABLE, categoryIdNameValidator);
			mappings.put(CategoryAction.RM_CATEGORY_DELETEABLE, categoryIdNameValidator);
		
		return mappings;
	}

}
