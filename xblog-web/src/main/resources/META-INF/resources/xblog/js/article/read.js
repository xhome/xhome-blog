/**
 * 评论表单校验 
 */
var validateCommentForm = function(form) {
    if (form == undefined) {
        form = '#comment_form'; 
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
                maxlength: "评论内容不能超过1000个字符",
            },
        },
        showErrors: function(map, list) {
            if (list.length > 0) {
                $('#comment_error_msg').html(list[0].message).show();  
            } else {
                $('#comment_error_msg').hide();
            }
        },
        submitHandler: function(form) {
            $.post('xblog/comment/add.json', $(form).serialize(), function(data) {
                if (data.status == 0) {
                    // 评论成功
                    var comment = data.data;
                    $('#article_comments').append('<div id="comment_comment_' + comment.id + '" class="panel panel-default">'
                        + '<div class="panel-heading">'
                        + '<h1 class="panel-title">' + comment.userName + ' 发表于 ' + comment.createdStr + '</h1></div>'
                        + '<div class="panel-body">' + comment.content + '</div></div>');
                    form.reset();
                } else {
                    // 评论出错 
                    $('#comment_error_msg').html(data.message).show(); 
                } 
            }); 
            return false; 
        },
    });
};
$(document).ready(function() {
    validateCommentForm();
});
