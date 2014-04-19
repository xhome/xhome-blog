/**
 * Author:   jhat
 * Date:     2014-02-15
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 文章阅读页面JS
 */

$(document).ready(function() {
    // 校验文章评论表单
    validateCommentForm(function(form) {
        $.post(form.action, $(form).serialize(), function(data) {
            if (data.status == 0) {
                // 评论成功
                var comment = data.data;
                $('#article_comments').prepend('<div id="comment_comment_' + comment.id + '" class="panel panel-default">'
                    + '<div class="panel-heading">'
                    + '<h1 class="panel-title">' + comment.userName + ' 发表于 ' + comment.createdStr + '</h1></div>'
                    + '<div class="panel-body">' + comment.content + '</div></div>');
                $('#article_comment_form_panel').hide();
            } else {
                // 评论出错 
                var message = data.status == 45 ? '该文章禁止评论' : data.message;
                $('#article_comment_error_msg').html(message).show(); 
            } 
        }); 
        return false; 
    });
});
