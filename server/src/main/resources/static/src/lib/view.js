/**

 @Name：layuiAdmin 视图模块
 @Author：贤心
 @Site：http://www.layui.com/admin/
 @License：LPPL

 */

layui.define(['laytpl', 'layer'], (exports) => {
  let $ = layui.$,
    laytpl = layui.laytpl,
    layer = layui.layer,
    setter = layui.setter,
    device = layui.device(),
    hint = layui.hint(),
    SHOW = 'layui-show',
    APP_BODY = 'LAY_app_body', // 主体内容容器ID
    // 对外接口
    view = (id) => {
      return new Class(id)
    },
    // 构造器
    Class = function (id = APP_BODY) {
      this.id = id
      this.container = $(`#${id}`)
    }

  // 加载中
  view.loading = function (el) {
    this.loadElem = $(
      '<i class="layui-anim layui-anim-rotate layui-anim-loop layui-icon layui-icon-loading layadmin-loading"></i>',
    )
    el.append(this.loadElem)
  }

  // 移除加载
  view.removeLoad = function () {
    this.loadElem && this.loadElem.remove()
  }

  // 清除 token，并跳转到登入页
  view.exit = () => {
    // 清空本地记录的 token
    layui.data(setter.tableName, {
      key: setter.request.tokenName,
      remove: true,
    })
    // 跳转到登入页
    location.hash = '/admin/login'
  }

  // Ajax请求
  view.req = (options) => {
    let success = options.success,
      error = options.error,
      request = setter.request,
      response = setter.response,
      debug = () => {
        return setter.debug ? `<br><cite>URL：</cite>${options.url}` : ''
      }
    options.data = options.data || {}
    options.headers = options.headers || {}

    if (request.tokenName) {
      let sendData = typeof options.data === 'string' ? JSON.parse(options.data) : options.data

      // 自动给参数传入默认 token
      options.data[request.tokenName] =
        request.tokenName in sendData
          ? options.data[request.tokenName]
          : layui.data(setter.tableName)[request.tokenName] || ''

      // 自动给 Request Headers 传入 token
      options.headers[request.tokenName] =
        request.tokenName in options.headers
          ? options.headers[request.tokenName]
          : layui.data(setter.tableName)[request.tokenName] || ''
    }
    delete options.success
    delete options.error

    return $.ajax(
      $.extend(
        {
          type: 'get',
          dataType: 'json',
          success(res) {
            let statusCode = response.statusCode
            // 只有 response 的 code 一切正常才执行 done
            if (res[response.statusName] === statusCode.ok) {
              typeof options.done === 'function' && options.done(res)
            }
            // 登录状态失效，清除本地 access_token，并强制跳转到登入页
            else if (res[response.statusName] === statusCode.logout) {
              view.exit()
            }
            // 其它异常
            else {
              let errorMsg = ['<cite>Error：</cite> ' + (res[response.msgName] || '返回状态码异常'), debug()].join('')
              view.error(errorMsg)
            }
            // 只要 http 状态码正常，无论 response 的 code 是否正常都执行 success
            typeof success === 'function' && success(res)
          },
          error(e, code) {
            let errorMsg = [`请求异常，请重试<br><cite>错误信息：</cite>${code}`, debug()].join('')
            view.error(errorMsg)
            typeof error === 'function' && error(e)
          },
        },
        options,
      ),
    )
  }

  // 弹窗
  view.popup = (options) => {
    let success = options.success,
      skin = options.skin
    delete options.success
    delete options.skin

    return layer.open(
      $.extend(
        {
          type: 1,
          title: '提示',
          content: '',
          id: 'LAY-id-view-popup',
          skin: `layui-layer-admin ${skin ? skin : ''}`,
          shadeClose: true,
          closeBtn: false,
          success(layero, index) {
            let closeElem = $('<i class="layui-icon layui-icon-close" close></i>')
            layero.append(closeElem)
            closeElem.on('click', () => {
              layer.close(index)
            })
            typeof success === 'function' && success.apply(this, arguments)
          },
        },
        options,
      ),
    )
  }

  // 异常提示
  view.error = (content, options) => {
    return view.popup(
      $.extend(
        {
          content,
          maxWidth: 300,
          offset: 't',
          anim: 6,
          id: 'LAY-id-error', // 弹窗内容区的id
        },
        options,
      ),
    )
  }

  // 请求模板文件渲染
  Class.prototype.render = function (views, params = {}) {
    let othis = this,
      router = layui.router()
    views = setter.views + views + setter.engine
    $(`#${APP_BODY}`).children('.layadmin-loading').remove()
    view.loading(this.container)

    // 请求模板
    $.ajax({
      url: views,
      type: 'get',
      dataType: 'html',
      data: { v: layui.cache.version },
      success(html) {
        html = `<div>${html}</div>`
        let titleElem = $(html).find('title'),
          title = titleElem.text() || (html.match(/<title>([\s\S]*)<\/title>/) || [])[1],
          res = { title, body: html }

        titleElem.remove()
        othis.params = params // 获取参数

        if (othis.then) {
          othis.then(res)
          delete othis.then
        }

        othis.parse(html)
        view.removeLoad()

        if (othis.done) {
          othis.done(res)
          delete othis.done
        }
      },
      error(e) {
        view.removeLoad()
        if (othis.render.isError) {
          return view.error(`请求视图文件异常，状态：${e.status}`)
        }

        if (e.status === 404) {
          othis.render('template/tips/404')
        } else {
          othis.render('template/tips/error')
        }
        othis.render.isError = true
      },
    })
    return this
  }

  // 解析模板
  Class.prototype.parse = function (html, refresh, callback) {
    let isScriptTpl = typeof html === 'object', // 是否模板元素
      elem = isScriptTpl ? html : $(html),
      tempElem = isScriptTpl ? html : elem.find('*[template]'),
      router = layui.router(),
      fn = function (options) {
        let tpl = laytpl(options.dataElem.html()),
          res = $.extend({ params: router.params }, options.res)

        options.dataElem.after(tpl.render(res))
        typeof callback === 'function' && callback()

        try {
          options.done && new Function('d', options.done)(res)
        } catch (e) {
          console.error(options.dataElem[0], `\n存在错误回调脚本\n\n`, e)
        }
      }

    elem.find('title').remove()
    this.container[refresh ? 'after' : 'html'](elem.children())

    router.params = this.params || {}

    // 遍历模板区块
    for (let i = tempElem.length; i > 0; i--) {
      ;(function () {
        let dataElem = tempElem.eq(i - 1),
          // 获取回调
          layDone = dataElem.attr('lay-done') || dataElem.attr('lay-then'),
          // 接口 url
          url = laytpl(dataElem.attr('lay-url') || '').render(router),
          // 接口参数
          data = laytpl(dataElem.attr('lay-data') || '').render(router),
          // 接口请求的头信息
          headers = laytpl(dataElem.attr('lay-headers') || '').render(router)

        try {
          data = new Function(`return ${data};`)()
        } catch (e) {
          hint.error(`lay-data: ${e.message}`)
          data = {}
        }

        try {
          headers = new Function(`return ${headers};`)()
        } catch (e) {
          hint.error(`lay-headers: ${e.message}`)
          headers = headers || {}
        }

        if (url) {
          view.req({
            type: dataElem.attr('lay-type') || 'get',
            url,
            data,
            dataType: 'json',
            headers,
            success(res) {
              fn({
                dataElem,
                res,
                done: layDone,
              })
            },
          })
        } else {
          fn({
            dataElem,
            done: layDone,
          })
        }
      })()
    }
    return this
  }

  // 直接渲染字符
  Class.prototype.send = function (views, data = {}) {
    let tpl = laytpl(views || this.container.html()).render(data)
    this.container.html(tpl)
    return this
  }

  // 局部刷新模板
  Class.prototype.refresh = function (callback) {
    let next = this.container.next(),
      templateId = next.attr('lay-templateid')
    if (this.id !== templateId) return this

    this.parse(this.container, 'refresh', () => {
      this.container.siblings(`[lay-templateid="${this.id}"]:last`).remove()
      typeof callback === 'function' && callback()
    })
    return this
  }

  // 视图请求成功后的回调
  Class.prototype.then = function (callback) {
    this.then = callback
    return this
  }

  // 视图渲染完毕后的回调
  Class.prototype.done = function (callback) {
    this.done = callback
    return this
  }

  // 对外输出
  exports('view', view)
})
