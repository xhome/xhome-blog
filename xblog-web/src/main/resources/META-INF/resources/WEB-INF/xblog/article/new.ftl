<!DOCTYPE html>
<#import "/xauth/xauth.ftl" as xauth />
<html lang="zh_CN">
<@xauth.head title="写文章" description="XAuth" keywords="XHome, XBlog, 博客">
<link href="xlibs/ext/resources/css/ext-all.css" rel="stylesheet" type="text/css"/>
</@xauth.head>
<body>
<script type="text/javascript" src="xlibs/ext/ext-all.js"></script>
<script type="text/javascript" src="xlibs/js/dashboard.js"></script>
<script type="text/javascript" src="xlibs/ext/lang/zh_CN.js"></script>
<script type="text/javascript">
Ext.onReady(function() {
    // 不支持IE 8及其以下版本
    if (Ext.isIE8m) {
        XHome.Msg.error('Unsupport IE 8 and lower version.'); 
    }

    // ExtJS初始化提示
    Ext.QuickTips.init();

    // 生成界面
    Ext.create('Ext.form.Panel', {
        url: '${xauth.base_url}xblog/article/add.json',
        renderTo: Ext.getBody(),
        layout: 'anchor',
        defaults: {
            anchor: '100%', 
        },
        bodyPadding: 10,
        border: false,
        items: [{
            xtype: 'textfield',
            name: 'article.title',
            itemId: 'article.title',
            fieldLabel: '标题',
            labelWidth: 30, 
            emptyText: '请输入文章标题',
            allowBlank: false,
            blankText: '文章标题不能为空',
            maxLength: 50,
            maxLengthText: '文章标题不能超过50个字符',
        }, {
            xtype: 'htmleditor',
            name: 'article.content',
            itemId: 'article.content',
            height: 400, 
        }, {
            xtype: 'hidden',
            name: 'article.category.name',
            itemId: 'article.category.name',
        }, {
            xtype: 'container',
            layout: {
                type: 'hbox', 
                align: 'middle', 
            },
            items: [{
                flex: 1,
                xtype: 'combo',
                name: 'article.category.id',
                itemId: 'article.category.id',
                fieldLabel: '分类',
                labelWidth: 30, 
                valueField: 'id',
                displayField: 'name',
                editable: false,
                store: Ext.create('XHome.data.JsonStore', {
                    wrapperBeforeload: false,
                    wrapperLoad: false,
                    url: '${xauth.base_url}xblog/category/query.json',
                    fields: ['id', 'name'],
                }),
                pageSize: 20,
                emptyText: '请选择文章分类',
                allowBlank: false,
                blankText: '文章分类不能为空',
                listeners: {
                    select: function(combo, records, eOpts) {
                        var form = combo.findParentByType('form'),
                            name = records[0].getData().name, 
                            categoryName = form.getComponent('article.category.name');
                        categoryName.setValue(name);
                    }
                },
            }, {
                flex: 1,
                xtype: 'combo',
                fieldLabel: '标签',
                labelWidth: 30, 
                valueField: 'id',
                displayField: 'name',
                editable: false,
                multiSelect: true,
                store: Ext.create('XHome.data.JsonStore', {
                    wrapperBeforeload: false,
                    wrapperLoad: false,
                    url: '${xauth.base_url}xblog/tag/query.json',
                    fields: ['id', 'name'],
                }),
                pageSize: 20,
                emptyText: '请选择文章标签',
                allowBlank: false,
                blankText: '文章标签不能为空',
                listeners: {
                    select: function(combo, records, eOpts) {
                        var tag, comp,
                            form = combo.findParentByType('form'),
                            values = form.getValues();

                        // 删除已有的标签
                        for (field in values) {
                            if (field.indexOf('article.tags') == 0) {
                                comp = form.getComponent(field);
                                form.remove(comp, true);
                            }
                        }

                        // 添加标签
                        for (var i = 0; i < records.length; i++) { 
                            tag = records[i].getData();
                            form.add({
                                xtype: 'hidden',
                                name: 'article.tags[' + i + '].id',
                                itemId: 'article.tags[' + i + '].id',
                                value: tag.id, 
                            });
                            form.add({
                                xtype: 'hidden',
                                name: 'article.tags[' + i + '].name',
                                itemId: 'article.tags[' + i + '].name',
                                value: tag.name, 
                            });
                        }
                    }
                },
            }], 
        }],
        buttons: [{
            text: '提交',
            handler: function() {
                var form = this.up('form').getForm();
                if (form.isValid()) {
                    form.submit({
                        success: function(form, action) {
                            XHome.Msg.info(action.result.message); 
                        },
                        failure: function(form, action) {
                            XHome.Msg.error(action.result.message);
                        },
                    }); 
                }
            },
        }],
    });

});
</script>
</body>
</html>
