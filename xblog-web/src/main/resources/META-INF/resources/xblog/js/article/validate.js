/**
 * Author:   jhat
 * Date:     2014-02-15
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 文章相关表单校验
 */

/**
 * 评论表单校验
 *
 * @param submitHandler 表单提交处理函数
 * @param showErrors 校验错误提示处理函数
 * @param form 评论表单ID
 */
var validateCommentForm = function(submitHandler, showErrors, form) {
    if (!form) {
        form = '#article_comment_form'; 
    }
    
    if (!showErrors) {
        showErrors = function(map, list) {
            if (list.length > 0) {
                $('#article_comment_error_msg').html(list[0].message).show();  
            } else {
                $('#article_comment_error_msg').hide();
            }
        };
    }
    
    if (!submitHandler) {
        submitHandler = function(form) {
            $.post(form.action, $(form).serialize(), function(data) {
                if (data.status == 0) {
                    // 评论成功
                    var comment = data.data;
                    $('#article_comments').prepend('<div id="comment_comment_' + comment.id + '" class="panel panel-default">'
                        + '<div class="panel-heading">'
                        + '<h1 class="panel-title">' + comment.userName + ' 发表于 ' + comment.createdStr + '</h1></div>'
                        + '<div class="panel-body">' + comment.content + '</div></div>');
                    form.reset();
                } else {
                    // 评论出错 
                    var message = data.status == 45 ? '该文章禁止评论' : data.message;
                    $('#article_comment_error_msg').html(message).show(); 
                } 
            }); 
            return false; 
        };
    } 
    
    return $(form).validate({
        rules: {
            'comment.userName': {
                required: true,
                maxlength: 20,
            },
            'comment.userEmail': {
                required: true,
                maxlength: 50,
                regex: /\b(^['\w-_]+(\.['\w-_]+)*@([\w-])+(\.[\w-]+)*((\.[\w]{2,})|(\.[\w]{2,}\.[\w]{2,}))$)\b/,
            },
            'comment.userWebsite': {
                maxlength: 100,
                regex: /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/, 
            },
            'comment.content': {
                required: true,
                maxlength: 1000,
            },
        },
        messages: {
            'comment.userName': {
                required: "请输入您的称呼",
                maxlength: "称呼不能超过{0}个字符",
            }, 
            'comment.userEmail': {
                required: "请输入您的邮箱",
                maxlength: "邮箱不能超过{0}个字符",
                regex: "邮箱格式错误", 
                email: "邮箱格式错误", 
            }, 
            'comment.userWebsite': {
                maxlength: "个人主页不能超过{0}个字符",
                regex: "个人主页格式错误", 
            },
            'comment.content': {
                required: "评论内容不能为空",
                maxlength: "评论内容不能超过{0}个字符",
            },
        },
        showErrors: showErrors,
        submitHandler: submitHandler,
    });
};

/**
 * 文章编辑表单校验
 *
 * @param submitHandler 表单提交处理函数
 * @param showErrors 校验错误提示处理函数
 * @param form 评论表单ID
 */
var validateArticleForm = function(submitHandler, showErrors, form) {
    if (!form) {
        form = '#article_edit_form'; 
    }
    
    if (!showErrors) {
        showErrors = function(map, list) {
            if (list.length > 0) {
                alert(list[0].message); 
            }
        };
    }
    
    if (!submitHandler) {
        submitHandler = function(form) {
            var params = $('#article_params'),
                categoryId = $('#article_category').val(),
                categoryName = $('#article_category>option:selected').text(),
                tags = $('#article_tags input:checked'),
                tag, tagId, tagName, i, tlen = 'article_tags'.length + 1;

            if (tags.length == 0) {
                alert('请选择文章标签');
                return false;
            }
           
            params.html('<input type="hidden" id="article.category.id" name="article.category.id" value="' + categoryId + '" />'
                + '<input type="hidden" id="article.category.name" name="article.category.name" value="' + categoryName + '" />'); 
            
            for (i = 0; i < tags.length; i++) {
                tag = tags[i];
                tagId = tag.id.substr(tlen);
                tagName = tag.value;
                params.append('<input type="hidden" id="article.tags[' + i + '].id" name="article.tags[' + i + '].id" value="' + tagId + '" />'
                    + '<input type="hidden" id="article.tags[' + i + '].name" name="article.tags[' + i + '].name" value="' + tagName + '" />'
                ); 
            } 
            form.submit();
            return true; 
        };
    } 
    
    return $(form).validate({
        rules: {
            'article.title': {
                required: true,
                maxlength: 50,
            },
            'article.detail': {
                required: true,
                maxlength: 60000,
            },
        },
        messages: {
            'article.title': {
                required: "请输入文章标题",
                maxlength: "文章标题不能超过{0}个字符",
            }, 
            'article.detail': {
                required: "文章内容不能为空",
                maxlength: "文章内容不能超过{0}个字符",
            },
        },
        showErrors: showErrors,
        submitHandler: submitHandler,
    });
};
