/**
 * Author:   jhat
 * Date:     2014-04-16
 * Email:    cpf624@126.com
 * Home:     https://jhat.pw
 * Describe: 留言列表页面JS
 */

$(document).ready(function() {
    // 校验留言表单
    validateMessageForm(function(form) {
        $.post(form.action, $(form).serialize(), function(data) {
            if (data.status == 0) {
                // 留言成功
                var message = data.data;
                $('#leave_messages').prepend('<div id="leave_message_' + message.id + '" class="panel panel-default">'
                    + '<div class="panel-heading">'
                    + '<h1 class="panel-title">' + message.userName + ' 发表于 ' + message.createdStr + '</h1></div>'
                    + '<div class="panel-body">' + message.content + '</div></div>');
                $('#leave_message_form_panel').hide();
            } else {
                // 留言出错 
                var message = data.status == 45 ? '留言功能已经关闭' : data.message;
                $('#leave_message_error_msg').html(message).show(); 
            } 
        }); 
        return false; 
    });
});
