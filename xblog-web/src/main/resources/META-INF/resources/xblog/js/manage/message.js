/**
 * Author:   jhat
 * Date:     2014-04-19
 * Email:    cpf624@126.com
 * Home:     https://jhat.pw
 * Describe: 留言管理面板
 */

Ext.define('XHome.XBlog.Manage.Message', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }
        
        // 搜索面板
        var spanel = Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                name: 'parameters["userName"]',
                fieldLabel: '称呼',
                maxLength: 20,
                maxLengthText: '留言称呼不能超过20个字符',
            }, {
                name: 'parameters["userEmail"]',
                fieldLabel: '邮箱',
                maxLength: 50,
                vtype: 'email',
                vtypeText: '留言邮箱格式错误',
                margin: '0 0 0 10',
            }],
        });

        // 数据显示表格
        var grid = Ext.create('XHome.Dashboard.EditorGridPanel', {
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 20,
            }, {
                text: '称呼',
                dataIndex: 'userName',
                width: 40,
            }, {
                text: '邮箱',
                dataIndex: 'userEmail',
                width: 60,
            }, {
                text: '网站',
                dataIndex: 'userWebsite',
                width: 70,
            }, {
                text: '内容',
                dataIndex: 'content',
                width: 100,
            }, {
                text: '答复',
                dataIndex: 'reply',
                width: 100,
            }, {
                text: '创建时间',
                dataIndex: 'createdStr',
                width: 65,
            }, {
                text: '修改时间',
                dataIndex: 'modifiedStr',
                width: 65,
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'userName', 'userEmail', 'userWebsite',
                    'content', 'reply', 'createdStr', 'modifiedStr',
                    'owner', 'modifier', 'version', 'status'],
                url: 'xblog/message/query.json',
            }),

            /**
             * 修改留言
             */
            editMessage: function() {
                var selection = grid.getSelectionModel().getSelection()[0],
                    message = selection.getData(),
                    formMessage = XHome.utils.formEncode(message, 'message');

                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '修改留言',
                    height: 300,
                    width: 400,
                    url: 'xblog/message/update.json',
                    success: function(result) {
                        selection.data = result.data;
                        grid.getView().refresh();
                        XHome.Msg.info(result.message);
                    },
                    hiddenParams: formMessage,
                    items: [{
                        fieldLabel: '称呼',
                        labelWidth: 30,
                        value: message.userName,
                        disabled: true,
                    }, {
                        fieldLabel: '邮箱',
                        labelWidth: 30,
                        value: message.userEmail,
                        disabled: true,
                    }, {
                        fieldLabel: '网站',
                        labelWidth: 30,
                        value: message.userWebsite,
                        disabled: true,
                    }, {
                        fieldLabel: '留言',
                        labelWidth: 30,
                        xtype: 'textarea',
                        value: message.content,
                        disabled: true,
                    }, {
                        fieldLabel: '答复',
                        labelWidth: 30,
                        xtype: 'textarea',
                        name: 'message.reply',
                        itemId: 'message.reply',
                        emptyText: '请输入留言答复',
                        allowBlank: false,
                        blankText: '留言答复不能为空',
                        minLength: 2,
                        minLengthText: '留言答复不能少于2个字符',
                        maxLength: 1000,
                        maxLengthText: '留言答复不能超过1000个字符',
                    }],
                }).show();
            },

            /**
             * 删除留言
             */
            deleteMessage: function() {
                var store = grid.getStore(),
                    selections = grid.getSelectionModel().getSelection(),
                    record = undefined,
                    data = undefined,
                    messages = [],
                    messageIds = [];
                if (selections.length == 0) {
                    return;
                }
                for (var i = 0; i < selections.length; i++) {
                    record = selections[i];
                    data = record.getData();
                    messages.push(data);
                    messageIds.push(record.getData().id);
                }
                var msg = '<font color="red">' + messageIds.join(', ') + '</font>';
                XHome.utils.request({
                    confirmMsg: '确认删除留言: ' + msg + ' ?',
                    progressMsg: '正在删除留言: ' + msg + '......',
                    url: 'xblog/message/delete.json',
                    params: XHome.utils.formEncode(messages, 'messages'),
                    success: function(result) {
                        store.remove(selections);
                    }
                });
            },
        });

        // 右键菜单
        var rightMenu = Ext.create('Ext.menu.Menu', {
            items: [{
                text: '修改留言',
                iconAlign: 'left',
                iconCls: 'icon_edit',
                handler: function(button, e) {
                    grid.editMessage();
                },
            }, {
                text: '删除留言',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteMessage();
                },
            }],
        });

        // 表格工具条
        grid.addDocked({
            xtype: 'toolbar',
            dock: 'top',
            items: ['-', {
                xtype: 'button',
                text: '删除留言',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteMessage();
                },
            }]
        });

        XHome.utils.bindGridClick(grid, rightMenu, grid.editMessage);

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
