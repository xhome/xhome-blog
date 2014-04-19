/**
 * Author:   jhat
 * Date:     2014-02-27
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 博客配置管理
 */

Ext.define('XHome.XBlog.Manage.BlogConfig', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }

        var switch_configs = {'xblog_allow_article_comment': 1, 'xblog_allow_leave_message': 1},
            number_configs = {'xblog_article_content_length': 1};

        // 搜索面板
        var spanel = Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                name: 'parameters["display"]',
                fieldLabel: '配置项',
                labelWidth: 40,
                maxLength: 50,
                maxLengthText: '配置项不能超过50个字符',
            }, {
                name: 'parameters["category"]',
                hidden: true,
                value: 2,
            }],
        });

        // 数据显示表格
        var grid = Ext.create('XHome.Dashboard.EditorGridPanel', {
            autoSelModel: false,
            autoPagingBar: false,
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 10,
                hidden: true,
            }, {
                text: '配置项',
                dataIndex: 'item',
                hidden: true,
            }, {
                text: '配置项',
                dataIndex: 'display',
            }, {
                text: '配置值',
                dataIndex: 'value',
                renderer: function(value, meta, record) {
                    var config = record.getData();
                    if (config.item in switch_configs) {
                        return {0: '关闭', 1: '开启'}[value]; 
                    }
                    return value; 
                },
                editor: {
                    allowBlank: false,
                }, 
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'category', 'item', 'display', 'value',
                    'createdStr', 'modifiedStr',
                    'owner', 'modifier', 'version', 'status'],
                url: 'xauth/config/query.json',
            }),

            /**
             * 修改配置项
             */
            editConfig: function() {
                var selection = grid.getSelectionModel().getSelection()[0],
                    config = selection.getData(),
                    formConfig = XHome.utils.formEncode(config, 'config'),
                    input;
                
                if (config.item in switch_configs) {
                    input = Ext.create('XHome.Combo.Switch', {
                        fieldLabel: '配置值',
                        name: 'config.value',
                        itemId: 'config.value',
                        value: config.value,
                    });
                } else {
                    input = {
                        fieldLabel: '配置值',
                        name: 'config.value',
                        itemId: 'config.value',
                        emptyText: '请输入配置值',
                        allowBlank: false,
                        blankText: '配置值不能为空',
                        maxLength: 1000,
                        maxLengthText: '配置值不能超过1000个字符',
                        value: config.value,
                    };
                    
                    if (config.item in number_configs) {
                        input['xtype'] = 'numberfield';
                    }
                }

                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '修改配置项',
                    height: 150,
                    width: 400,
                    url: 'xauth/config/update.json',
                    success: function(result) {
                        selection.data = result.data;
                        grid.getView().refresh();
                        XHome.Msg.info(result.message);
                    },
                    hiddenParams: formConfig,
                    items: [{
                        fieldLabel: '配置项',
                        value: config.display,
                        disabled: true,
                    }, input ],
                }).show();
            },
        });

        // 右键菜单
        var rightMenu = Ext.create('Ext.menu.Menu', {
            items: [{
                text: '修改配置项',
                iconAlign: 'left',
                iconCls: 'icon_edit',
                handler: function(button, e) {
                    grid.editConfig();
                },
            }],
        });

        XHome.utils.bindGridClick(grid, rightMenu, grid.editConfig);

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
