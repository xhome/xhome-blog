/**
 * Author:   jhat
 * Date:     2013-12-24
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 标签管理面板
 */

Ext.define('XHome.XBlog.Manage.Tag', {
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
                maxLengthText: '标签名称不能超过30个字符',
                regex: /^[\w-_]+$/,
                regexText: '标签名称格式错误',
            }],
        });

        // 标签名称输入框
        Ext.define('XHome.XBlog.Manage.Tag.NameField', {
            extend: 'Ext.form.field.Text',
            fieldLabel: '名称',
            labelWidth: 30,
            name: 'tag.name',
            itemId: 'tag.name',
            emptyText: '请输入标签名称',
            allowBlank: false,
            blankText: '标签名称不能为空',
            minLength: 4,
            minLengthText: '标签名称不能少于4个字符',
            maxLength: 30,
            maxLengthText: '标签名称不能超过30个字符',
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
                url: 'xblog/tag/query.json',
            }),

            /**
             * 添加标签
             */
            addTag: function() {
                var store = grid.getStore();
                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '添加标签',
                    height: 130,
                    width: 320,
                    url: 'xblog/tag/add.json',
                    success: function(result) {
                        store.add(result.data);
                    },
                    items: [
                        Ext.create('XHome.XBlog.Manage.Tag.NameField'), 
                    ],
                }).show();
            },

            /**
             * 修改标签
             */
            editTag: function() {
                var selection = grid.getSelectionModel().getSelection()[0],
                    tag = selection.getData(),
                    formTag = XHome.utils.formEncode(tag, 'tag');
                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '修改标签',
                    height: 130,
                    width: 320,
                    url: 'xblog/tag/update.json',
                    success: function(result) {
                        selection.data = result.data;
                        grid.getView().refresh();
                        XHome.Msg.info(result.message);
                    },
                    hiddenParams: formTag,
                    items: [
                        Ext.create('XHome.XBlog.Manage.Tag.NameField', {
                            value: tag.name,
                        }), 
                    ],
                }).show();
            },

            /**
             * 删除标签
             */
            deleteTag: function() {
                var store = grid.getStore(),
                    selections = grid.getSelectionModel().getSelection(),
                    record = undefined,
                    data = undefined,
                    tags = [],
                    tagNames = [];
                if (selections.length == 0) {
                    return;
                }
                for (var i = 0; i < selections.length; i++) {
                    record = selections[i];
                    data = record.getData();
                    tags.push(data);
                    tagNames.push(record.getData().name);
                }
                var msg = '<font color="red">' + tagNames.join(', ') + '</font>';
                XHome.utils.request({
                    confirmMsg: '确认删除标签: ' + msg + ' ?',
                    progressMsg: '正在删除标签: ' + msg + '......',
                    url: 'xblog/tag/delete.json',
                    params: XHome.utils.formEncode(tags, 'tags'),
                    success: function(result) {
                        store.remove(selections);
                    }
                });
            },
        });

        // 右键菜单
        var rightMenu = Ext.create('Ext.menu.Menu', {
            items: [{
                text: '添加标签',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addTag();
                },
            }, {
                text: '修改标签',
                iconAlign: 'left',
                iconCls: 'icon_edit',
                handler: function(button, e) {
                    grid.editTag();
                },
            }, {
                text: '删除标签',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteTag();
                },
            }],
        });

        // 表格工具条
        grid.addDocked({
            xtype: 'toolbar',
            dock: 'top',
            items: ['-', {
                xtype: 'button',
                text: '添加标签',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addTag();
                },
            }, '-', {
                xtype: 'button',
                text: '删除标签',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteTag();
                },
            }]
        });

        XHome.utils.bindGridClick(grid, rightMenu, grid.editTag);

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
