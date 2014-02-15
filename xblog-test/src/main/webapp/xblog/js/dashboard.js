Ext.onReady(function() {
   // 不支持IE 8及其以下版本
   if (Ext.isIE8m) {
        Ext.MessageBox.alert('Info', 'Unsupport IE 8 and lower version.');
        return;
    }
    
    // ExtJS初始化提示
    Ext.QuickTips.init();
    
    // 生成界面
    new XHome.Dashboard({
        logoConfig: {
            // html: '<h1>XHome XBlog Dashboard</h1>',
        },
        navigationConfig: { // 导航菜单配置
            title: '导航菜单',
            root: {
                children: [{
                    id: 'xblog_manage',
                    text: '博客管理',
                    iconCls: 'icon-nav-manage',
                    children: [{
                        id: 'xblog_manage-category',
                        text: '分类管理',
                        leaf: true,
                        iconCls: 'icon-nav-manage-category',
                        showScript: 'xblog/js/manage/category.js',
                        showClass: 'XHome.XBlog.Manage.Category',
                    },{
                        id: 'xblog_manage-tag',
                        text: '标签管理',
                        leaf: true,
                        iconCls: 'icon-nav-manage-tag',
                        showScript: 'xblog/js/manage/tag.js',
                        showClass: 'XHome.XBlog.Manage.Tag',
                    }, {
                        id: 'xblog_manage-record',
                        text: '访问记录',
                        leaf: true,
                        iconCls: 'icon-nav-manage-record',
                        showScript: 'xblog/js/manage/record.js',
                        showClass: 'XHome.XBlog.Manage.Record',
                    }, {
                        id: 'xblog_manage-blog_config',
                        text: '博客配置',
                        leaf: true,
                        iconCls: 'icon-nav-manage-blog_config',
                        showScript: 'xblog/js/manage/blog_config.js',
                        showClass: 'XHome.XBlog.Manage.BlogConfig',
                    }]
                }, {
                    id: 'xauth_manage',
                    text: '认证管理',
                    iconCls: 'icon-nav-manage',
                    children: [{
                        id: 'xauth_manage-role',
                        text: '角色管理',
                        leaf: true,
                        iconCls: 'icon-nav-manage-role',
                        showScript: 'xauth/js/manage/role.js',
                        showClass: 'XHome.XAuth.Manage.Role',
                    },{
                        id: 'xauth_manage-user',
                        text: '用户管理',
                        leaf: true,
                        iconCls: 'icon-nav-manage-user',
                        showScript: 'xauth/js/manage/user.js',
                        showClass: 'XHome.XAuth.Manage.User',
                    }, {
                        id: 'xauth_manage-auth_log',
                        text: '认证日志',
                        leaf: true,
                        iconCls: 'icon-nav-manage-auth_log',
                        showScript: 'xauth/js/manage/auth_log.js',
                        showClass: 'XHome.XAuth.Manage.AuthLog',
                    }, {
                        id: 'xauth_manage-manage_log',
                        text: '管理日志',
                        leaf: true,
                        iconCls: 'icon-nav-manage-manage_log',
                        showScript: 'xauth/js/manage/manage_log.js',
                        showClass: 'XHome.XAuth.Manage.ManageLog',
                    }, {
                        id: 'xauth_manage-auth_config',
                        text: '认证配置',
                        leaf: true,
                        iconCls: 'icon-nav-manage-auth_config',
                        showScript: 'xauth/js/manage/auth_config.js',
                        showClass: 'XHome.XAuth.Manage.AuthConfig',
                    }]
                }, {
                    id: 'xauth_system',
                    text: '系统管理',
                    iconCls: 'icon-nav-system',
                    children: [{
                        id: 'xauth_system-config',
                        text: '系统配置',
                        leaf: true,
                        iconCls: 'icon-nav-system-config',
                        showScript: 'xauth/js/system/config.js',
                        showClass: 'XHome.XAuth.System.Config',
                    }]
                }]
            },
        },
    });
});
