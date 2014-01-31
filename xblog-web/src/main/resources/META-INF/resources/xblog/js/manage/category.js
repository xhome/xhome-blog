/**
 * Author:   jhat
 * Date:     2013-12-24
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 分类管理面板
 */

Ext.define('XHome.XBlog.Manage.Category', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }
        // 搜索面板
        var spanel = Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                name: 'parameters["name"]',
                fieldLabel: '名称',
                maxLength: 30,
                maxLengthText: '分类名称不能超过30个字符',
                regex: /^[\w-_]+$/,
                regexText: '分类名称格式错误',
            }],
        });

        // 分类名称输入框
        Ext.define('XHome.XBlog.Manage.Category.NameField', {
            extend: 'Ext.form.field.Text',
            fieldLabel: '名称',
            labelWidth: 30,
            name: 'category.name',
            itemId: 'category.name',
            emptyText: '请输入分类名称',
            allowBlank: false,
            blankText: '分类名称不能为空',
            minLength: 4,
            minLengthText: '分类名称不能少于4个字符',
            maxLength: 30,
            maxLengthText: '分类名称不能超过30个字符',
        });

        // 数据显示表格
        var grid = Ext.create('XHome.Dashboard.EditorGridPanel', {
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 10,
            }, {
                text: '名称',
                dataIndex: 'name',
            }, {
                text: '创建时间',
                dataIndex: 'createdStr',
            }, {
                text: '修改时间',
                dataIndex: 'modifiedStr',
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'name', 'createdStr', 'modifiedStr',
                    'owner', 'modifier', 'version', 'status'],
                url: 'xblog/category/query.json',
            }),

            /**
             * 添加分类
             */
            addCategory: function() {
                var store = grid.getStore();
                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '添加分类',
                    height: 130,
                    width: 320,
                    url: 'xblog/category/add.json',
                    success: function(result) {
                        store.add(result.data);
                    },
                    items: [
                        Ext.create('XHome.XBlog.Manage.Category.NameField'), 
                    ],
                }).show();
            },

            /**
             * 修改分类
             */
            editCategory: function() {
                var selection = grid.getSelectionModel().getSelection()[0],
                    category = selection.getData(),
                    formCategory = XHome.utils.formEncode(category, 'category');
                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '修改分类',
                    height: 130,
                    width: 320,
                    url: 'xblog/category/update.json',
                    success: function(result) {
                        selection.data = result.data;
                        grid.getView().refresh();
                        XHome.Msg.info(result.message);
                    },
                    hiddenParams: formCategory,
                    items: [
                        Ext.create('XHome.XBlog.Manage.Category.NameField', {
                            value: category.name,
                        }), 
                    ],
                }).show();
            },

            /**
             * 删除分类
             */
            deleteCategory: function() {
                var store = grid.getStore(),
                    selections = grid.getSelectionModel().getSelection(),
                    record = undefined,
                    data = undefined,
                    categories = [],
                    categoryNames = [];
                if (selections.length == 0) {
                    return;
                }
                for (var i = 0; i < selections.length; i++) {
                    record = selections[i];
                    data = record.getData();
                    categories.push(data);
                    categoryNames.push(record.getData().name);
                }
                var msg = '<font color="red">' + categoryNames.join(', ') + '</font>';
                XHome.utils.request({
                    confirmMsg: '确认删除分类: ' + msg + ' ?',
                    progressMsg: '正在删除分类: ' + msg + '......',
                    url: 'xblog/category/remove.json',
                    params: XHome.utils.formEncode(categories, 'categories'),
                    success: function(result) {
                        store.remove(selections);
                    }
                });
            },
        });

        // 右键菜单
        var rightMenu = Ext.create('Ext.menu.Menu', {
            items: [{
                text: '添加分类',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addCategory();
                },
            }, {
                text: '修改分类',
                iconAlign: 'left',
                iconCls: 'icon_edit',
                handler: function(button, e) {
                    grid.editCategory();
                },
            }, {
                text: '删除分类',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteCategory();
                },
            }],
        });

        // 表格工具条
        grid.addDocked({
            xtype: 'toolbar',
            dock: 'top',
            items: ['-', {
                xtype: 'button',
                text: '添加分类',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addCategory();
                },
            }, '-', {
                xtype: 'button',
                text: '删除分类',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteCategory();
                },
            }]
        });

        XHome.utils.bindGridClick(grid, rightMenu, grid.editCategory);

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
