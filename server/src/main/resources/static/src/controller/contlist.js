/**

 @Name：layuiAdmin 内容系统
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

  // 文章列表
  table.render({
    elem: '#LAY-id-article-list',
    url: setter.api + 'json/content/article.json',
    cols: [
      [
        { type: 'checkbox', fixed: 'left' },
        { field: 'id', title: '文章ID', width: 100, sort: true },
        { field: 'label', title: '文章标签', minWidth: 100 },
        { field: 'title', title: '文章标题' },
        { field: 'author', title: '作者' },
        {
          field: 'uploadTime',
          title: '上传时间',
          sort: true,
          templet: (d) => {
            return util.toDateString(d.uploadTime * 1000, 'yyyy年MM月dd日')
          },
        },
        {
          field: 'status',
          title: '发布状态',
          minWidth: 80,
          align: 'center',
          templet: '#LAY-id-statusTpl',
        },
        {
          title: '操作',
          minWidth: 150,
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

  // 监听文章列表行工具事件
  table.on('tool(LAY-filter-article-list)', (obj) => {
    let data = obj.data,
      label = ['美食', '新闻', '八卦', '体育', '音乐', '历史']
    label.forEach((v, index) => {
      if (v === data.label) data.label = index
    })
    if (obj.event === 'edit') {
      layer.open({
        type: 1,
        title: '编辑文章',
        id: 'LAY-popup-article-edit',
        area: ['550px', '500px'],
        // 成功打开弹窗后的回调
        success(layero, index) {
          view(this.id)
            .render('app/content/article-form', data)
            .done(() => {
              form.render(null, 'LAY-filter-articleList-form')
              admin.setInputFocusEnd(layero.find('[name=title]'))
              form.on('submit(LAY-filter-article-submit)', (data) => {
                let field = data.field
                //提交 Ajax 成功后，静态更新表格中的数据
                // $.ajax({})
                table.reload('LAY-id-article-list') // 数据刷新
                layer.close(index) // 关闭弹层
              })
            })
        },
      })
    } else if (obj.event === 'del') {
      layer.confirm('确定删除此文章？', (index) => {
        obj.del()
        layer.close(index)
      })
    }
  })

  // 分类管理
  table.render({
    elem: '#LAY-id-tags-list',
    url: setter.api + 'json/content/tags.json',
    cols: [
      [
        { type: 'numbers', fixed: 'left' },
        { field: 'id', title: 'ID', width: 100, sort: true },
        { field: 'tags', title: '分类名', minWidth: 100 },
        {
          title: '操作',
          width: 150,
          align: 'center',
          fixed: 'right',
          templet: '#LAY-id-rowToolTpl',
        },
      ],
    ],
    text: '对不起，加载出现异常！',
  })

  // 监听分类管理行工具事件
  table.on('tool(LAY-filter-tags-list)', (obj) => {
    let data = obj.data
    if (obj.event === 'edit') {
      layer.open({
        type: 1,
        title: '编辑分类',
        id: 'LAY-popup-tags-edit',
        area: ['450px', '200px'],
        success(layero, index) {
          view(this.id)
            .render('app/content/tags-form', data)
            .done(() => {
              form.render(null, 'LAY-filter-tagsList-form')
              admin.setInputFocusEnd(layero.find('[name=tags]'))
              form.on('submit(LAY-filter-tags-submit)', (data) => {
                let field = data.field
                //提交 Ajax 成功后，静态更新表格中的数据
                // $.ajax({})
                obj.update(field) // 数据更新
                layer.close(index) // 关闭弹层
              })
            })
        },
      })
    } else if (obj.event === 'del') {
      layer.confirm('确定删除此分类？', (index) => {
        obj.del()
        layer.close(index)
      })
    }
  })

  // 评论管理
  table.render({
    elem: '#LAY-id-comment-list',
    url: setter.api + 'json/content/comment.json',
    cols: [
      [
        { type: 'checkbox', fixed: 'left' },
        { field: 'id', title: 'ID', width: 100, sort: true },
        { field: 'reviewers', title: '评论者', minWidth: 100 },
        { field: 'content', title: '评论内容', minWidth: 100 },
        {
          field: 'commentTime',
          title: '评论时间',
          minWidth: 100,
          sort: true,
          templet: (d) => {
            return util.toDateString(d.commentTime * 1000, 'yyyy年MM月dd日')
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

  // 监听评论管理行工具事件
  table.on('tool(LAY-filter-comment-list)', (obj) => {
    let data = obj.data
    if (obj.event === 'edit') {
      layer.open({
        type: 1,
        title: '编辑评论',
        id: 'LAY-popup-content-edit',
        area: ['450px', '240px'],
        success(layero, index) {
          view(this.id)
            .render('app/content/comment-form', data)
            .done(() => {
              form.render(null, 'LAY-filter-commentList-form')
              admin.setInputFocusEnd(layero.find('[name=content]'))
              form.on('submit(LAY-filter-comment-submit)', (data) => {
                let field = data.field
                //提交 Ajax 成功后，静态更新表格中的数据
                // $.ajax({})
                obj.update(field) // 数据更新
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

  exports('contlist', {})
})
