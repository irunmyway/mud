/**

 @Name：layuiAdmin 用户管理 管理员管理 角色管理
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

  // 用户列表
  table.render({
    elem: '#LAY-id-user-list',
    url: setter.api + 'json/user/userList.json',
    cols: [
      [
        { type: 'checkbox', fixed: 'left' },
        { field: 'id', title: 'ID', width: 80, sort: true },
        { field: 'username', title: '用户名', minWidth: 100 },
        { field: 'avatar', title: '头像', width: 100, templet: '#LAY-id-imgTpl' },
        { field: 'phone', title: '手机' },
        { field: 'email', title: '邮箱' },
        { field: 'sex', title: '性别', width: 80 },
        { field: 'ip', title: 'IP地址' },
        {
          field: 'joinTime',
          title: '加入时间',
          sort: true,
          templet: (d) => {
            return util.toDateString(d.joinTime * 1000, 'yyyy年MM月dd日')
          },
        },
        {
          title: '操作',
          width: 160,
          align: 'center',
          fixed: 'right',
          toolbar: '#LAY-id-rowToolTpl',
        },
      ],
    ],
    page: true,
    limit: 30,
    height: 'full-220',
    text: '加载出现异常!',
  })

  // 监听用户列表行工具事件
  table.on('tool(LAY-filter-user-list)', (obj) => {
    // 注：tool 是工具条事件名，LAY-id-user-list 是 table 原始容器的属性 lay-filter="对应的值"
    let data = obj.data, //获得当前行数据
      tr = $(obj.tr) // 获得当前行 tr 的 DOM 对象

    if (obj.event === 'del') {
      layer.confirm('确定删除该条数据吗?', { icon: 3, title: '提示' }, (index) => {
        obj.del()
        layer.close(index)
      })
    } else if (obj.event === 'edit') {
      layer.open({
        type: 1,
        title: '编辑用户',
        id: 'LAY-popup-user-edit',
        area: ['500px', '400px'],
        // 成功打开弹窗后的回调
        success(layero, index) {
          view(this.id)
            .render('user/list-form', data)
            .done(() => {
              form.render(null, 'LAY-filter-userList-form')
              admin.setInputFocusEnd(layero.find('[name=username]'))
              form.on('submit(LAY-filter-user-submit)', (data) => {
                let field = data.field
                //提交 Ajax 成功后，关闭当前弹层并重载表格
                //$.ajax({});
                table.reload('LAY-id-user-list')
                layer.close(index)
              })
            })
        },
      })
    }
  })

  // 管理员列表
  table.render({
    elem: '#LAY-id-admin-list',
    url: setter.api + 'json/admin/adminList.json',
    cols: [
      [
        { type: 'checkbox', fixed: 'left' },
        { field: 'id', title: 'ID', width: 80, sort: true },
        { field: 'loginName', title: '登入名', minWidth: 100 },
        { field: 'phone', title: '手机' },
        { field: 'email', title: '邮箱' },
        { field: 'role', title: '角色' },
        {
          field: 'joinTime',
          title: '加入时间',
          sort: true,
          templet: (d) => {
            return util.toDateString(d.joinTime * 1000, 'yyyy年MM月dd日')
          },
        },
        {
          field: 'check',
          title: '审核状态',
          templet: '#LAY-id-checkTpl',
          width: 100,
          align: 'center',
        },
        {
          title: '操作',
          width: 160,
          align: 'center',
          fixed: 'right',
          toolbar: '#LAY-id-rowToolTpl',
        },
      ],
    ],
    text: '加载出现异常',
  })

  // 监听管理员列表行工具事件
  table.on('tool(LAY-filter-admin-list)', (obj) => {
    let data = obj.data, //获得当前行数据
      tr = $(obj.tr), // 获得当前行 tr 的 DOM 对象
      roles = ['管理员', '超级管理员', '纠错员', '采购员', '推销员', '运营人员', '编辑']
    roles.forEach((v, index) => {
      if (v === data.role) data.role = index
    })

    if (obj.event === 'del') {
      layer.confirm('确定删除该条数据吗?', { icon: 3, title: '提示' }, (index) => {
        obj.del()
        layer.close(index)
      })
    } else if (obj.event === 'edit') {
      layer.open({
        type: 1,
        title: '编辑管理员',
        id: 'LAY-popup-admin-edit',
        area: ['420px', '450px'],
        // 成功打开弹窗后的回调
        success(layero, index) {
          view(this.id)
            .render('admin/list-form', data)
            .done(() => {
              form.render(null, 'LAY-filter-adminList-form')
              admin.setInputFocusEnd(layero.find('[name=loginName]'))
              form.on('submit(LAY-filter-admin-submit)', (data) => {
                let field = data.field
                //提交 Ajax 成功后，关闭当前弹层并重载表格
                //$.ajax({});
                table.reload('LAY-id-admin-list')
                layer.close(index)
              })
            })
        },
      })
    }
  })

  // 角色列表
  table.render({
    elem: '#LAY-id-role-list',
    url: setter.api + 'json/role/roleList.json',
    cols: [
      [
        { type: 'checkbox', fixed: 'left' },
        { field: 'id', title: 'ID', width: 80, sort: true },
        { field: 'role', title: '角色名' },
        { field: 'limits', title: '权限' },
        { field: 'descr', title: '具体描述' },
        {
          field: 'check',
          title: '审核状态',
          width: 100,
          templet: '#LAY-id-checkTpl',
          align: 'center',
        },
        {
          title: '操作',
          width: 160,
          align: 'center',
          fixed: 'right',
          toolbar: '#LAY-id-rowToolTpl',
        },
      ],
    ],
    text: '加载出现异常',
  })

  // 监听角色列表行工具事件
  table.on('tool(LAY-filter-role-list)', (obj) => {
    let data = obj.data, //获得当前行数据
      tr = $(obj.tr), // 获得当前行 tr 的 DOM 对象
      roles = ['管理员', '超级管理员', '纠错员', '采购员', '推销员', '运营人员', '编辑', '统计人员', '评估员']
    roles.forEach((v, index) => {
      if (v === data.role) data.role = index
    })

    if (obj.event === 'del') {
      layer.confirm('确定删除该条数据吗?', { icon: 3, title: '提示' }, (index) => {
        obj.del()
        layer.close(index)
      })
    } else if (obj.event === 'edit') {
      layer.open({
        type: 1,
        title: '编辑角色',
        id: 'LAY-popup-role-edit',
        area: ['500px', '400px'],
        // 成功打开弹窗后的回调
        success(layero, index) {
          view(this.id)
            .render('role/list-form', data)
            .done(() => {
              form.render(null, 'LAY-filter-roleList-form')
              admin.setInputFocusEnd(layero.find('[name=descr]'))
              form.on('submit(LAY-filter-role-submit)', (data) => {
                let field = data.field
                //提交 Ajax 成功后，关闭当前弹层并重载表格
                //$.ajax({});
                table.reload('LAY-id-role-list')
                layer.close(index)
              })
            })
        },
      })
    }
  })

  exports('useradmin', {})
})
