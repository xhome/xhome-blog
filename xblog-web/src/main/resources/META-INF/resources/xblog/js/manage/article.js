/**
 * Author:   jhat
 * Date:     2013-12-24
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 文章管理面板
 */

Ext.define('XHome.XBlog.Manage.Article', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }
        // 搜索面板
        var spanel = Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                name: 'parameters["title"]',
                fieldLabel: '标题',
                maxLength: 50,
                maxLengthText: '文章标题不能超过50个字符',
            }, {
                name: 'parameters["category_name"]',
                fieldLabel: '分类',
                maxLength: 30,
                maxLengthText: '分类名不能超过30个字符',
                margin: '0 0 0 10',
            }, {
                name: 'parameters["tag_name"]',
                fieldLabel: '标签',
                maxLength: 30,
                maxLengthText: '标签名不能超过30个字符',
                margin: '0 0 0 10',
            }],
        });

        // 数据显示表格
        var grid = Ext.create('XHome.Dashboard.EditorGridPanel', {
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 10,
                hidden: true,
            }, {
                text: '名称',
                dataIndex: 'title',
                width: 50, 
            }, {
                text: '属性',
                dataIndex: 'attribute',
                width: 20, 
                renderer: function(value, meta, record) {
                    if (value & (1 << 4)) {
                        return '指定权限'; 
                    } else if (value & (1 << 3)) {
                        return '不可见评论'; 
                    } else if (value & (1 << 2)) {
                        return '仅可阅读'; 
                    } else if (value & (1 << 1)) {
                        return '仅作者可见';
                    } 
                    return '完全公开'; 
                },
            }, {
                text: '分类',
                dataIndex: 'category',
                width: 40, 
                renderer: function(value, meta, record) {
                    return value.name; 
                },
            }, {
                text: '标签',
                dataIndex: 'tags',
                renderer: function(value, meta, record) {
                    if (!value) {
                        return ''; 
                    }
                    var tagNames = []; 
                    for (var i = 0; i < value.length; i++) {
                        tagNames.push(value[i].name);
                    } 
                    return tagNames.join(', ');
                },
            }, {
                text: '阅读数',
                dataIndex: 'readCount',
                width: 15, 
            }, {
                text: '评论数',
                dataIndex: 'commentCount',
                width: 15, 
            }, {
                text: '创建时间',
                dataIndex: 'createdStr',
                width: 30,
                hidden: true, 
            }, {
                text: '修改时间',
                dataIndex: 'modifiedStr',
                width: 30, 
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'title', 'attribute', 'category', 'tags',
                    'readCount', 'commentCount', 'createdStr', 'modifiedStr',
                    'owner', 'modifier', 'version', 'status'],
                url: 'xblog/article/query.json',
            }),

            /**
             * 新建文章
             */
            addArticle: function() {
                window.open('xblog/article/edit.htm', 'article_edit'); 
            },

            /**
             * 修改文章
             */
            editArticle: function() {
                var selection = grid.getSelectionModel().getSelection()[0],
                    article = selection.getData();
                window.open('xblog/article/edit.htm?id=' + article.id, 'article_edit'); 
            },

            /**
             * 删除文章
             */
            deleteArticle: function() {
                var store = grid.getStore(),
                    selections = grid.getSelectionModel().getSelection(),
                    record = undefined,
                    data = undefined,
                    articles = [],
                    articleTitles = [];
                if (selections.length == 0) {
                    return;
                }
                for (var i = 0; i < selections.length; i++) {
                    record = selections[i];
                    data = record.getData();
                    delete data.category; 
                    delete data.tags; 
                    articles.push(data);
                    articleTitles.push(record.getData().title);
                }
                var msg = '<font color="red">' + articleTitles.join(', ') + '</font>';
                XHome.utils.request({
                    confirmMsg: '确认删除文章: ' + msg + ' ?',
                    progressMsg: '正在删除文章: ' + msg + '......',
                    url: 'xblog/article/remove.json',
                    params: XHome.utils.formEncode(articles, 'articles'),
                    success: function(result) {
                        store.remove(selections);
                    }
                });
            },
        });

        // 右键菜单
        var rightMenu = Ext.create('Ext.menu.Menu', {
            items: [{
                text: '新建文章',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addArticle();
                },
            }, {
                text: '修改文章',
                iconAlign: 'left',
                iconCls: 'icon_edit',
                handler: function(button, e) {
                    grid.editArticle();
                },
            }, {
                text: '删除文章',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteArticle();
                },
            }],
        });

        // 表格工具条
        grid.addDocked({
            xtype: 'toolbar',
            dock: 'top',
            items: ['-', {
                xtype: 'button',
                text: '新建文章',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addArticle();
                },
            }, '-', {
                xtype: 'button',
                text: '删除文章',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteArticle();
                },
            }]
        });

        XHome.utils.bindGridClick(grid, rightMenu, grid.editArticle);

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
