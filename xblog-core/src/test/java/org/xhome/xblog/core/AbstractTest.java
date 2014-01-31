package org.xhome.xblog.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xhome.xauth.Role;
import org.xhome.xauth.User;
import org.xhome.xblog.Article;
import org.xhome.xblog.ArticleRolePermission;
import org.xhome.xblog.ArticleUserPermission;
import org.xhome.xblog.Category;
import org.xhome.xblog.CategoryRolePermission;
import org.xhome.xblog.CategoryUserPermission;
import org.xhome.xblog.Comment;
import org.xhome.xblog.Record;
import org.xhome.xblog.Tag;
import org.xhome.xblog.TagRolePermission;
import org.xhome.xblog.TagUserPermission;

/**
 * @project xblog-core
 * @author jhat
 * @email cpf624@126.com
 * @date Sep 2, 20139:54:46 PM
 * @describe
 */
public abstract class AbstractTest {

	protected ApplicationContext context = null;
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected User oper;

	public AbstractTest() {
		context = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml");
		oper = new User("jhat");
	}

	protected void printTag(List<Tag> tags) {
		if (tags != null) {
			for (Tag tag : tags) {
				printTag(tag);
			}
		}
	}

	protected void printTag(Tag tag) {
		if (tag == null) {
			return;
		}
		logger.debug("Id:" + tag.getId() + "\tName:" + tag.getName()
				+ "\tStatus:" + tag.getStatus());
	}

	protected void printCategory(List<Category> categorys) {
		if (categorys != null) {
			for (Category category : categorys) {
				printCategory(category);
			}
		}
	}

	protected void printCategory(Category category) {
		if (category == null) {
			return;
		}
		logger.debug("Id:" + category.getId() + "\tName:" + category.getName()
				+ "\tStatus:" + category.getStatus());

		Category parent = category.getParent();
		if (parent != null) {
			logger.debug("\tParent:\tId:" + category.getId() + "\tName:"
					+ category.getName() + "\tStatus:" + category.getStatus());
		}
	}

	protected void printArticle(List<Article> articles) {
		if (articles != null) {
			for (Article article : articles) {
				printArticle(article);
			}
		}
	}

	protected void printArticle(Article article) {
		logger.debug("Id:" + article.getId() + "\tTitle:" + article.getTitle()
				+ "\tAttribute:" + article.getAttribute() + "\tContent:"
				+ article.getContent() + "\tVersion:" + article.getVersion()
				+ "\tStatus:" + article.getStatus());
		printCategory(article.getCategory());
		printTag(article.getTags());
	}

	protected void printComment(List<Comment> comments) {
		if (comments != null) {
			for (Comment comment : comments) {
				printComment(comment);
			}
		}
	}

	protected void printComment(Comment comment) {
		if (comment == null) {
			return;
		}
		Article article = comment.getArticle();
		logger.debug("Id:" + comment.getId() + "\tArticle:"
				+ (article != null ? article.getId() : null) + "\tType:"
				+ comment.getType() + "\tCotent:" + comment.getContent()
				+ "\tStatus:" + comment.getStatus());

		Comment target = comment.getTarget();
		if (target != null) {
			article = target.getArticle();
			logger.debug("\tTarget:\tId:" + target.getId() + "\tArticle:"
					+ (article != null ? article.getId() : null) + "\tType:"
					+ target.getType() + "\tCotent:" + target.getContent()
					+ "\tStatus:" + target.getStatus());
		}
	}

	protected void printRecord(List<Record> records) {
		if (records != null) {
			for (Record record : records) {
				printRecord(record);
			}
		}
	}

	protected void printRecord(Record record) {
		if (record == null) {
			return;
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.debug("Id:" + record.getId() + "\tUser:"
				+ record.getUser().getId() + "\tArticle:"
				+ record.getArticle().getId() + "\tTime:"
				+ format.format(record.getCreated()) + "\tAddress:"
				+ record.getAddress() + "\tAgent:" + record.getAgent()
				+ "\tNumber:" + record.getNumber() + "\tStatus:"
				+ record.getStatus());
	}

	protected void printTagUserPermission(
			List<TagUserPermission> tagUserPermissions) {
		if (tagUserPermissions != null) {
			for (TagUserPermission tagUserPermission : tagUserPermissions) {
				printTagUserPermission(tagUserPermission);
			}
		}
	}

	protected void printTagUserPermission(TagUserPermission tagUserPermission) {
		if (tagUserPermission == null) {
			return;
		}
		Tag tag = tagUserPermission.getTag();
		User user = tagUserPermission.getUser();
		logger.debug("Id:" + tagUserPermission.getId() + "\tTag:"
				+ (tag != null ? tag.getId() : null) + "\tUser:"
				+ (user != null ? user.getId() : null) + "\tPermission:"
				+ tagUserPermission.getPermission() + "\tStatus:"
				+ tagUserPermission.getStatus());
	}

	protected void printTagRolePermission(
			List<TagRolePermission> tagRolePermissions) {
		if (tagRolePermissions != null) {
			for (TagRolePermission tagRolePermission : tagRolePermissions) {
				printTagRolePermission(tagRolePermission);
			}
		}
	}

	protected void printTagRolePermission(TagRolePermission tagRolePermission) {
		if (tagRolePermission == null) {
			return;
		}
		Tag tag = tagRolePermission.getTag();
		Role role = tagRolePermission.getRole();
		logger.debug("Id:" + tagRolePermission.getId() + "\tTag:"
				+ (tag != null ? tag.getId() : null) + "\tRole:"
				+ (role != null ? role.getId() : null) + "\tPermission:"
				+ tagRolePermission.getPermission() + "\tStatus:"
				+ tagRolePermission.getStatus());
	}

	protected void printCategoryUserPermission(
			List<CategoryUserPermission> categoryUserPermissions) {
		if (categoryUserPermissions != null) {
			for (CategoryUserPermission categoryUserPermission : categoryUserPermissions) {
				printCategoryUserPermission(categoryUserPermission);
			}
		}
	}

	protected void printCategoryUserPermission(
			CategoryUserPermission categoryUserPermission) {
		if (categoryUserPermission == null) {
			return;
		}
		Category category = categoryUserPermission.getCategory();
		User user = categoryUserPermission.getUser();
		logger.debug("Id:" + categoryUserPermission.getId() + "\tCategory:"
				+ (category != null ? category.getId() : null) + "\tUser:"
				+ (user != null ? user.getId() : null) + "\tPermission:"
				+ categoryUserPermission.getPermission() + "\tStatus:"
				+ categoryUserPermission.getStatus());
	}

	protected void printCategoryRolePermission(
			List<CategoryRolePermission> categoryRolePermissions) {
		if (categoryRolePermissions != null) {
			for (CategoryRolePermission categoryRolePermission : categoryRolePermissions) {
				printCategoryRolePermission(categoryRolePermission);
			}
		}
	}

	protected void printCategoryRolePermission(
			CategoryRolePermission categoryRolePermission) {
		if (categoryRolePermission == null) {
			return;
		}
		Category category = categoryRolePermission.getCategory();
		Role role = categoryRolePermission.getRole();
		logger.debug("Id:" + categoryRolePermission.getId() + "\tCategory:"
				+ (category != null ? category.getId() : null) + "\tRole:"
				+ (role != null ? role.getId() : null) + "\tPermission:"
				+ categoryRolePermission.getPermission() + "\tStatus:"
				+ categoryRolePermission.getStatus());
	}

	protected void printArticleUserPermission(
			List<ArticleUserPermission> articleUserPermissions) {
		if (articleUserPermissions != null) {
			for (ArticleUserPermission articleUserPermission : articleUserPermissions) {
				printArticleUserPermission(articleUserPermission);
			}
		}
	}

	protected void printArticleUserPermission(
			ArticleUserPermission articleUserPermission) {
		if (articleUserPermission == null) {
			return;
		}
		Article article = articleUserPermission.getArticle();
		User user = articleUserPermission.getUser();
		logger.debug("Id:" + articleUserPermission.getId() + "\tArticle:"
				+ (article != null ? article.getId() : null) + "\tUser:"
				+ (user != null ? user.getId() : null) + "\tPermission:"
				+ articleUserPermission.getPermission() + "\tStatus:"
				+ articleUserPermission.getStatus());
	}

	protected void printArticleRolePermission(
			List<ArticleRolePermission> articleRolePermissions) {
		if (articleRolePermissions != null) {
			for (ArticleRolePermission articleRolePermission : articleRolePermissions) {
				printArticleRolePermission(articleRolePermission);
			}
		}
	}

	protected void printArticleRolePermission(
			ArticleRolePermission articleRolePermission) {
		if (articleRolePermission == null) {
			return;
		}
		Article article = articleRolePermission.getArticle();
		Role role = articleRolePermission.getRole();
		logger.debug("Id:" + articleRolePermission.getId() + "\tArticle:"
				+ (article != null ? article.getId() : null) + "\tRole:"
				+ (role != null ? role.getId() : null) + "\tPermission:"
				+ articleRolePermission.getPermission() + "\tStatus:"
				+ articleRolePermission.getStatus());
	}

}
