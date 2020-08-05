/**

 @Name：layuiAdmin 主页示例
 @Author：star1029
 @Site：http://www.layui.com/admin/
 @License：GPL-2

 */

layui.define('echarts', (exports) => {
  let $ = layui.$,
    layer = layui.layer,
    admin = layui.admin,
    setter = layui.setter,
    element = layui.element,
    laytpl = layui.laytpl

  admin.req({
    url: setter.api + 'json/home/sample.json',
    success(res) {
      renderUpdateLogs(res.data.updateLogs)
      renderUserNews(res.data.userNews)
      renderComponents(res.data.components)
      renderCardList(res.data.cardList)
      renderUserNotes(res.data.userNotes)

      layui.use('carousel', () => {
        let carousel = layui.carousel,
          device = layui.device()

        element.render('progress')
        // 轮播切换
        $('.layadmin-carousel').each(function () {
          carousel.render({
            elem: this,
            width: '100%',
            arrow: 'none',
            autoplay: $(this).data('autoplay') === true,
            intervel: $(this).data('interval') || 3000,
            trigger: device.ios || device.android ? 'click' : 'hover',
            anim: $(this).data('anim') || 'default',
          })
        })

        // 八卦新闻
        let elemPlayers = $('#LAY-id-players').children('div'),
          // 访问量
          elemPageView = $('#LAY-id-pageView').children('div'),
          // 全国用户分布图
          elemUserMap = $('#LAY-id-userMap').children('div')

        elemPlayers[0] && renderDataView(elemPlayers, res.data.players)
        elemPageView[0] && renderDataView(elemPageView, res.data.pageView)
        elemUserMap[0] && renderDataView(elemUserMap, res.data.userMap)
      })
    },
  })

  // 本周活跃用户列表
  layui.use('table', () => {
    let table = layui.table
    table.render({
      elem: '#LAY-id-activeUser',
      url: setter.api + 'json/home/active-user.json',
      cols: [
        [
          {
            field: 'username',
            title: '用户名',
            templet: (d) => {
              if (d.username === '胡歌') {
                return `<span class="layui-text-red">${d.username}</span>`
              } else if (d.username === '彭于晏') {
                return `<span class="layui-text-orange">${d.username}</span>`
              } else if (d.username === '靳东') {
                return `<span class="layui-text-green">${d.username}</span>`
              } else {
                return d.username
              }
            },
          },
          {
            field: 'lastLogin',
            title: '最后登录时间',
            minWidth: 120,
            templet: `<div><i class="layui-icon layui-icon-log"> {{ d.lastLogin }}</i></div>`,
          },
          {
            field: 'status',
            title: '状态',
            templet: (d) => {
              if (d.status === '在线') {
                return `<span class="layui-text-cyan">${d.status}</span>`
              } else {
                return `<i>${d.status}</i>`
              }
            },
          },
          {
            field: 'praise',
            title: '获得赞',
            templet: `<div>{{ d.praise }} <i class="layui-icon layui-icon-praise"></i></div>`,
          },
        ],
      ],
      skin: 'line',
    })
  })

  // 项目进展
  layui.use('table', () => {
    let table = layui.table
    table.render({
      elem: '#LAY-id-prograss',
      url: setter.api + 'json/home/prograss.json',
      cols: [
        [
          { type: 'checkbox', fixed: 'left' },
          { field: 'prograss', title: '任务' },
          { field: 'time', title: '所需时间' },
          {
            field: 'complete',
            title: '完成情况',
            templet: (d) => {
              if (d.complete === '已完成') {
                return `<del class="layui-text-green">${d.complete}</del>`
              } else if (d.complete === '进行中') {
                return `<span class="layui-text-orange">${d.complete}</span>`
              } else {
                return `<span class="layui-text-red">${d.complete}</span>`
              }
            },
          },
        ],
      ],
      skin: 'line',
    })
  })

  // 回复留言
  admin.events.replyNote = (othis) => {
    let uid = othis.data('id')
    layer.prompt(
      {
        title: `回复留言 ID:${uid}`,
        formType: 2,
      },
      (value, index) => {
        // 这里可以请求 Ajax
        layer.msg(`回复了: ${value}`)
        layer.close(index)
      },
    )
  }

  /**
   * @description 渲染图表
   * @param {object} elem jQuery对象
   * @param {array} data
   * @param {number} [index=0]
   */
  function renderDataView(elem, data, index = 0) {
    let echarts = layui.echarts,
      echartsApp = []
    echartsApp[index] = echarts.init(elem[index], layui.echartsTheme)
    echartsApp[index].setOption(data[index])
    window.onresize = echartsApp[index].resize
  }

  /**
   * @description 更新日志
   * @param {array} data
   */
  function renderUpdateLogs(data) {
    laytpl(
      [
        '<div class="layui-row layui-col-space10">',
        '{{# layui.each(d, (index, item)=>{ }}',
        '<div class="layui-col-xs12 layui-col-sm4">',
        '<div class="layadmin-update-logs"><div class="layadmin-text-top">',
        '<i class="layui-icon {{ item.icon }}"></i>',
        '<a href="{{ item.link }}" target="_blank">{{ item.title }}</a></div>',
        '<p class="layadmin-text-center">{{ item.content }}</p>',
        '<p class="layadmin-text-bottom">',
        '<a href="{{ item.link }}" target="_blank">{{ item.module }}</a>',
        '<span>{{ item.date }}</span></p></div></div>{{# }) }}</div>',
      ].join(''),
    ).render(data, (html) => {
      $('#LAY-id-updateLogs').html(html)
    })
  }

  /**
   * @description 用户动态
   * @param {array} data
   */
  function renderUserNews(data) {
    laytpl(
      [
        '{{# layui.each(d, (index, item) => { }}<dd>',
        '<div class="layadmin-avatar-img layui-bg-green layui-circle">',
        '<a href="javascript:;"><img src="{{ layui.setter.api }}{{ item.avatar }}" alt="" /></a>',
        '</div><div><p>{{ item.user }} {{ item.text }}</p>',
        '<span>{{ item.date }}</span></div></dd>{{# }) }}',
      ].join(''),
    ).render(data, (html) => {
      $('#LAY-id-userNews').html(html)
    })
  }

  /**
   * @description 重点组件
   * @param {array} data
   */
  function renderComponents(data) {
    laytpl(
      [
        '{{# layui.each(d, (index, item) =>{ }}',
        '<li class="layui-col-xs6"><a href="{{ item.link }}" target="_blank">',
        '<span class="layui-bg-green layui-circle layadmin-cpn-img">',
        '<img src="{{ item.img }}" ></span><span>{{ item.text }}</span>',
        '</a></li>{{# }) }}',
      ].join(''),
    ).render(data, (html) => {
      $('#LAY-id-components').html(html)
    })
  }

  /**
   * @description 卡片列表
   * @param {array} data
   */
  function renderCardList(data) {
    laytpl(
      [
        '{{# layui.each(d, (index, item) => { }}',
        '<div class="layui-col-sm6 layui-col-md3" style="padding-bottom: 0;">',
        '<div class="layui-card"><div class="layui-card-header">',
        '{{ item.title }}<span class="layadmin-badge layui-badge {{ item.badgeBg }}">{{ item.badgeText }}</span></div>',
        '<div class="layui-card-body layadmin-card-list">',
        '<p class="layadmin-big-font">{{ item.content }}</p>',
        '<p>{{ item.bottomText }}<span class="layadmin-span-color">{{ item.bottomSpan }}',
        '<i class="layui-inline layui-icon {{ item.bottomIcon }}"></i></span>',
        '</p></div></div></div>{{# }) }}',
      ].join(''),
    ).render(data, (html) => {
      $('#LAY-id-cardList').html(html)
    })
  }

  /**
   * @description 用户留言
   * @param {array} data
   */
  function renderUserNotes(data) {
    laytpl(
      [
        '{{# layui.each(d, (index, item) => { }}<li>',
        '<h3>{{ item.user }}</h3><p>{{ item.content }}</p><span>{{ item.date }}</span>',
        '<a href="javascript:;" data-id="{{ item.id }}" class="layui-btn layui-btn-xs layadmin-reply" layadmin-event="replyNote">回复</a>',
        '</li>{{# }) }}',
      ].join(''),
    ).render(data, (html) => {
      $('#LAY-id-userNotes').html(html)
    })
  }

  exports('sample', {})
})
