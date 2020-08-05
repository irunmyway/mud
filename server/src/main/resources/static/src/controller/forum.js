/**

 @Name：layuiAdmin 社区系统
 @Author：star1029
 @Site：http://www.layui.com/admin/
 @License：LPPL

 */

layui.define(['table', 'form', 'util'], (exports) => {
  let $ = layui.$,
    setter = layui.setter,
    view = layui.view,
    admin = layui.admin,
    table = layui.table,
    form = layui.form,
    util = layui.util

  // 帖子列表
  table.render({
    elem: '#LAY-id-posts-list',
    url: setter.api + 'json/forum/posts.json',
    cols: [
      [
        { type: 'checkbox', fixed: 'left' },
        { field: 'id', title: 'ID', width: 100, sort: true },
        { field: 'poster', title: '发帖人' },
        {
          field: 'avatar',
          title: '头像',
          width: 100,
          templet: '#LAY-id-avatarTpl',
        },
        { field: 'content', title: '发帖内容' },
        {
          field: 'postTime',
          title: '发帖时间',
          sort: true,
          templet: (d) => {
            return util.toDateString(d.postTime * 1000, 'yyyy年MM月dd日')
          },
        },
        {
          field: 'top',
          title: '置顶',
          minWidth: 80,
          align: 'center',
          templet: '#LAY-id-topTpl',
        },
        {
          title: '操作',
          width: 150,
          align: 'center',
          fixed: 'right',
          toolbar: '#LAY-id-rowToolTpl',
        },
      ],
    ],
    page: true,
    limit: 10,
    limits: [10, 15, 20, 25, 30],
    text: '对不起，加载出现异常！',
  })

  // 监听帖子列表行工具事件
  table.on('tool(LAY-filter-posts-list)', (obj) => {
    let data = obj.data
    if (obj.event === 'edit') {
      layer.open({
        type: 1,
        title: '编辑帖子',
        id: 'LAY-popup-posts-edit',
        area: ['550px', '400px'],
        resize: false,
        success(layero, index) {
          view(this.id)
            .render('app/forum/posts-form', data)
            .done(() => {
              form.render(null, 'LAY-filter-postsList-form')
              admin.setInputFocusEnd(layero.find('[name=poster]'))
              form.on('submit(LAY-filter-posts-submit)', (data) => {
                let field = data.field
                //提交 Ajax 成功后，静态更新表格中的数据
                // $.ajax({})
                table.reload('LAY-id-posts-list') // 数据刷新
                layer.close(index) // 关闭弹层
              })
            })
        },
      })
    } else if (obj.event === 'del') {
      layer.confirm('确定删除此条帖子？', (index) => {
        obj.del()
        layer.close(index)
      })
    }
  })

  // 回帖列表
  table.render({
    elem: '#LAY-id-replys-list',
    url: setter.api + 'json/forum/replys.json',
    cols: [
      [
        { type: 'checkbox', fixed: 'left' },
        { field: 'id', title: 'ID', width: 100, sort: true },
        { field: 'replyer', title: '回帖人' },
        { field: 'cardid', title: '回帖ID', sort: true },
        { field: 'avatar', title: '头像', width: 100, templet: '#LAY-id-avatarTpl' },
        { field: 'content', title: '回帖内容', width: 200 },
        {
          field: 'replyTime',
          title: '回帖时间',
          sort: true,
          templet: (d) => {
            return util.toDateString(d.replyTime * 1000, 'yyyy年MM月dd日')
          },
        },
        {
          title: '操作',
          width: 150,
          align: 'center',
          fixed: 'right',
          toolbar: '#LAY-id-rowToolTpl',
        },
      ],
    ],
    page: true,
    limit: 10,
    limits: [10, 15, 20, 25, 30],
    text: '对不起，加载出现异常！',
  })

  // 监听回帖列表行工具事件
  table.on('tool(LAY-filter-replys-list)', (obj) => {
    let data = obj.data
    if (obj.event === 'edit') {
      layer.open({
        type: 1,
        title: '编辑回帖',
        id: 'LAY-popup-replys-edit',
        area: ['550px', '350px'],
        resize: false,
        success(layero, index) {
          view(this.id)
            .render('app/forum/replys-form', data)
            .done(() => {
              form.render(null, 'LAY-filter-replysList-form')
              admin.setInputFocusEnd(layero.find('[name=content]'))
              form.on('submit(LAY-filter-replys-submit)', (data) => {
                let field = data.field
                //提交 Ajax 成功后，静态更新表格中的数据
                // $.ajax({})
                table.reload('LAY-id-replys-list') // 数据刷新
                layer.close(index) // 关闭弹层
              })
            })
        },
      })
    } else if (obj.event === 'del') {
      layer.confirm('确定删除此条评论？', (index) => {
        obj.del()
        layer.close(index)
      })
    }
  })

  exports('forum', {})
})
