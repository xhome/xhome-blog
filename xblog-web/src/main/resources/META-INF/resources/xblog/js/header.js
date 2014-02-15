/**
 * Author:   jhat
 * Date:     2014-02-15
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 导航工具类用到的JS
 */

$(document).ready(function() {
    // 校验修改用户密码表单	
	validateUserChangePasswordForm(function(form) {
		$.post(form.action, $(form).serialize(), function(data) {
			if (data.status == 0) {
				// 密码修改成功
				form.reset();	
				$('#change_password_modal').modal('hide');
			} else {
				// 密码修改失败
				var message;
				if (data.status == 9) {
					message = '旧密码不正确';	
				} else {
					message = '密码修改失败，请重试';	
				}
				$('#user_change_password_error_msg').html(message).show();
			}	
		});
	});	
});
