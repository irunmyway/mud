/**

 @Name：layuiAdmin 公共业务
 @Author：贤心
 @Site：http://www.layui.com/admin/
 @License：LPPL

 */

layui.define((exports) => {
  let $ = layui.$,
    setter = layui.setter,
    admin = layui.admin

  admin.events.logout = () => {
    admin.req({
      url: setter.api + 'json/user/logout.json',
      done(res) {
        //这里要说明一下：done 是只有 response 的 code 正常才会执行。而 succese 则是只要 http 为 200 就会执行

        //清空本地记录的 token，并跳转到登入页
        admin.exit()
      },
    })
  }

  exports('common', {})
})
