/**

 @Name：layuiAdmin 设置
 @Author：贤心
 @Site：http://www.layui.com/admin/
 @License: LPPL

 */

layui.define(['form', 'upload'], (exports) => {
  let $ = layui.$,
    setter = layui.setter,
    admin = layui.admin,
    form = layui.form,
    upload = layui.upload,
    avatarSrc = $('#LAY-id-avatar-src')

  // 自定义验证
  form.verify({
    // value：表单的值、item：表单的DOM对象
    nickname(value, item) {
      if (!new RegExp('^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$').test(value)) return '用户名不能有特殊字符'
      if (/(^\_)|(\__)|(\_+$)/.test(value)) return "用户名首尾不能出现下划线'_'"
      if (/^\d+\d+\d$/.test(value)) return '用户名不能全为数字'
    },
    pass: [/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格'],
    // 确认密码
    repass(value) {
      if (value !== $('#LAY-id-new-password').val()) return '两次密码输入不一致'
    },
  })

  // 网站设置
  form.on('submit(LAY-filter-set-website)', (data) => {
    layer.msg(JSON.stringify(data.field))
    // admin.req({})
    return false
  })

  // 邮件服务
  form.on('submit(LAY-filter-set-email)', (data) => {
    layer.msg(JSON.stringify(data.field))
    // admin.req({})
    return false
  })

  // 设置我的资料
  form.on('submit(LAY-filter-set-info)', (data) => {
    layer.msg(JSON.stringify(data.field))
    // admin.req({})
    return false
  })

  // 设置密码
  form.on('submit(LAY-filter-set-password)', (data) => {
    layer.msg(JSON.stringify(data.field))
    // admin.req({})
    return false
  })

  // 上传头像
  upload.render({
    elem: '#LAY-id-avatar-upload',
    url: setter.api + 'json/upload/demo.json',
    method: 'get',
    done(res) {
      if (res.code === 0) {
        avatarSrc.val(res.data.src)
      } else {
        layer.msg(res.msg, { icon: 5 })
      }
    },
  })

  // 查看头像
  admin.events.avatarPreview = (othis) => {
    let src = avatarSrc.val()
    layer.photos({
      photos: {
        title: '查看头像',
        data: [{ src }],
      },
      shade: 0.01,
      closeBtn: 1,
      anim: 5,
    })
  }

  exports('set', {})
})
