<!DOCTYPE html>
<#import "/xblog/xblog.ftl" as xblog />
<html lang="zh_CN">
<@xblog.head title="写文章" description="XBlog" keywords="XHome, XBlog, 博客">
<link href="${xblog.base_url}/xlibs/ext/resources/css/ext-all.css" rel="stylesheet" type="text/css"/>
</@xblog.head>
<body>
<script type="text/javascript" src="${xblog.base_url}/xlibs/ext/ext-all.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/js/dashboard.js"></script>
<script type="text/javascript" src="${xblog.base_url}/xlibs/ext/lang/zh_CN.js"></script>
<script type="text/javascript">
Ext.onReady(function() {
    // 不支持IE 8及其以下版本
    if (Ext.isIE8m) {
        XHome.Msg.error('Unsupport IE 8 and lower version.'); 
    }

    // ExtJS初始化提示
    Ext.QuickTips.init();

    // 生成界面
    var form = Ext.create('Ext.form.Panel', {
        id: 'form',
        url: '${xblog.base_url}/xblog/article/add.json',
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
            itemId: 'container',
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
                    url: '${xblog.base_url}/xblog/category/query.json',
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
                itemId: 'article.tags',
                fieldLabel: '分类',
                fieldLabel: '标签',
                labelWidth: 30, 
                valueField: 'id',
                displayField: 'name',
                editable: false,
                multiSelect: true,
                store: Ext.create('XHome.data.JsonStore', {
                    wrapperBeforeload: false,
                    wrapperLoad: false,
                    url: '${xblog.base_url}/xblog/tag/query.json',
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
                            if (field.indexOf('article.tags[') == 0) {
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
                var formPanel = this.up('form'),
                    form = formPanel.getForm();
                if (form.isValid()) {
                    form.submit({
                        success: function(form, action) {
                            var article = action.result.data;
                            var status = formPanel.getComponent('article.status'),
                                version = formPanel.getComponent('article.version');
                            if (status) {
                                status.setValue(article.status); 
                            }
                            if (version) {
                                version.setValue(article.version); 
                            }
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

    <#if commonResult?? && commonResult.status?? && commonResult.status == 0>
        form.add({
            xtype: 'hidden',
            name: 'article.id',
            itemId: 'article.id',
            value: ${commonResult.data.id},
        }); 
        form.add({
            xtype: 'hidden',
            name: 'article.status',
            itemId: 'article.status',
            value: ${commonResult.data.status},
        }); 
        form.add({
            xtype: 'hidden',
            name: 'article.version',
            itemId: 'article.version',
            value: ${commonResult.data.version},
        }); 
        form.getComponent('article.title').setValue('${commonResult.data.title}'); 
        form.getComponent('article.content').setValue('${commonResult.data.content}');
        var container = form.getComponent('container');
        container.getComponent('article.category.id').select(${commonResult.data.category.id}); 
        form.getComponent('article.category.name').setValue('${commonResult.data.category.name}'); 
        var tags = []; 
        <#list commonResult.data.tags as tag>
            tags.push(${tag.id});
            form.add({
                xtype: 'hidden',
                name: 'article.tags[${tag_index}].id',
                itemId: 'article.tags[${tag_index}].id',
                value: ${tag.id},
            }); 
            form.add({
                xtype: 'hidden',
                name: 'article.tags[${tag_index}].name',
                itemId: 'article.tags[${tag_index}].name',
                value: '${tag.name}',
            }); 
        </#list>
        container.getComponent('article.tags').select(tags); 
        form.getForm().url = '${xblog.base_url}/xblog/article/update.json';
    </#if>

});
</script>
</body>
</html>
