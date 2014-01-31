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
                }]
            },
        },
    });
});
