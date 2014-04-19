/**
 * Author:   jhat
 * Date:     2014-04-19
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 留言相关表单校验
 */

/**
 * 留言表单校验
 *
 * @param submitHandler 表单提交处理函数
 * @param showErrors 校验错误提示处理函数
 * @param form 留言表单ID
 */
var validateMessageForm = function(submitHandler, showErrors, form) {
    if (!form) {
        form = '#leave_message_form'; 
    }
    
    if (!showErrors) {
        showErrors = function(map, list) {
            if (list.length > 0) {
                $('#leave_message_error_msg').html(list[0].message).show();  
            } else {
                $('#leave_message_error_msg').hide();
            }
        };
    }
    
    if (!submitHandler) {
        submitHandler = function(form) {
            $.post(form.action, $(form).serialize(), function(data) {
                if (data.status == 0) {
                    // 留言成功
                    var message = data.data;
                    $('#leave_messages').prepend('<div id="leave_message_' + message.id + '" class="panel panel-default">'
                        + '<div class="panel-heading">'
                        + '<h1 class="panel-title">' + message.userName + ' 发表于 ' + message.createdStr + '</h1></div>'
                        + '<div class="panel-body">' + message.content + '</div></div>');
                    form.reset();
                } else {
                    // 留言出错 
                    var message = data.status == 45 ? '留言功能已经关闭' : data.message;
                    $('#leave_message_error_msg').html(message).show(); 
                } 
            }); 
            return false; 
        };
    } 
    
    return $(form).validate({
        rules: {
            'message.userName': {
                required: true,
                maxlength: 20,
            },
            'message.userEmail': {
                required: true,
                maxlength: 50,
                regex: /\b(^['\w-_]+(\.['\w-_]+)*@([\w-])+(\.[\w-]+)*((\.[\w]{2,})|(\.[\w]{2,}\.[\w]{2,}))$)\b/,
            },
            'message.userWebsite': {
                maxlength: 100,
                regex: /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/, 
            },
            'message.content': {
                required: true,
                maxlength: 1000,
            },
        },
        messages: {
            'message.userName': {
                required: "请输入您的称呼",
                maxlength: "称呼不能超过{0}个字符",
            }, 
            'message.userEmail': {
                required: "请输入您的邮箱",
                maxlength: "邮箱不能超过{0}个字符",
                regex: "邮箱格式错误", 
                email: "邮箱格式错误", 
            }, 
            'message.userWebsite': {
                maxlength: "个人主页不能超过{0}个字符",
                regex: "个人主页格式错误", 
            },
            'message.content': {
                required: "留言内容不能为空",
                maxlength: "留言内容不能超过{0}个字符",
            },
        },
        showErrors: showErrors,
        submitHandler: submitHandler,
    });
};
