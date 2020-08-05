/**

 @Name：layuiAdmin 用户登入和注册等
 @Author：贤心
 @Site：http://www.layui.com/admin/
 @License: LPPL

 */

layui.define('form', (exports) => {
  let $ = layui.$,
    setter = layui.setter,
    admin = layui.admin,
    form = layui.form,
    $body = $('body')

  // 自定义验证
  form.verify({
    // value：表单的值、item：表单的DOM对象
    nickname(value, item) {
      if (!value || value === '') {
        return '用户名不能为空'
      }
      if (!new RegExp('^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$').test(value)) {
        return '用户名不能有特殊字符'
      }
      if (/(^\_)|(\__)|(\_+$)/.test(value)) {
        return "用户名首尾不能出现下划线'_'"
      }
      if (/^\d+\d+\d$/.test(value)) {
        return '用户名不能全为数字'
      }
    },
    // 我们既支持上述函数式的方式，也支持下述数组的形式
    // 数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
    pass: [/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格'],
  })

  // 发送短信验证码
  admin.sendAuthCode({
    elem: '#LAY-id-get-smscode', // 获取短信验证码的按钮
    elemPhone: '#LAY-id-user-cellphone', // 手机输入框
    elemSMScode: '#LAY-id-user-smscode', // 短信验证码输入框
    ajax: {
      url: setter.api + 'json/user/sms.json',
    },
  })

  // 更换图形验证码
  $body.on('click', '#LAY-id-get-vercode', function () {
    this.src = 'https://www.oschina.net/action/user/captcha?t=' + new Date().getTime()
  })

  // 输出接口
  exports('user', {})
})
