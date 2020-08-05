/**

 @Name：layuiAdmin 主页控制台
 @Author：贤心
 @Site：http://www.layui.com/admin/
 @License：LPPL

 */

layui.define('laytpl', (exports) => {
  let $ = layui.$,
    admin = layui.admin,
    setter = layui.setter,
    laytpl = layui.laytpl

  // 请求数据
  admin.req({
    url: setter.api + 'json/console/index.json',
    success(res) {
      renderShortcut(res.data.shortcut)
      renderBacklog(res.data.backlog)
      renderNews(res.data.news)

      // 区块轮播切换
      layui.use(['carousel', 'echarts'], () => {
        let carousel = layui.carousel,
          element = layui.element,
          device = layui.device(),
          echarts = layui.echarts,
          echartsApp = [],
          carouselIndex = 0,
          // 拿到图表的DOM容器
          elemDataView = $('#LAY-id-dataview').children('div'),
          // 渲染图表
          renderDataView = (index) => {
            echartsApp[index] = echarts.init(elemDataView[index], layui.echartsTheme)
            echartsApp[index].setOption(res.data.echarts[index])
            window.onresize = echartsApp[index].resize
          }

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

        // 没找到DOM，终止执行
        if (!elemDataView[0]) return
        renderDataView(carouselIndex)

        // 监听数据概览轮播
        carousel.on('change(LAY-filter-dataview)', (data) => {
          renderDataView((carouselIndex = data.index))
        })
        // 监听侧边伸缩
        admin.on('side', () => {
          setTimeout(() => {
            renderDataView(carouselIndex)
          }, 300)
        })
        // 监听路由
        admin.on('hash(tab)', () => {
          layui.router().path.join('') || renderDataView(carouselIndex)
        })

        element.render('progress')
      })
    },
  })

  layui.use('table', () => {
    let table = layui.table
    // 今日热搜
    table.render({
      elem: '#LAY-id-topSearch', // 渲染容器
      url: setter.api + 'json/console/top-search.json', // 数据接口
      page: true, // 是否开启分页
      // 表头
      cols: [
        [
          { type: 'numbers', fixed: 'left' },
          {
            field: 'keywords',
            title: '关键词',
            minWidth: 300,
            templet: `<div><a href="https://www.baidu.com/s?wd={{ d.keywords }}" target="_blank" class="layui-table-link">{{ d.keywords }}</a></div>`,
          },
          { field: 'frequency', title: '搜索次数', minWidth: 120, sort: true },
          { field: 'userNums', title: '用户数', sort: true },
        ],
      ],
      skin: 'line', // 表格外观风格
    })

    // 今日热贴
    table.render({
      elem: '#LAY-id-topCard',
      url: setter.api + 'json/console/top-card.json',
      page: true,
      cellMinWidth: 120,
      cols: [
        [
          { type: 'numbers', fixed: 'left' },
          {
            field: 'title',
            title: '标题',
            minWidth: 300,
            templet: `<div><a href="{{ d.href }}" target="_blank" class="layui-table-link">{{ d.title }}</a></div>`,
          },
          { field: 'username', title: '发帖者' },
          { field: 'channel', title: '类别' },
          { field: 'crt', title: '点击率', sort: true },
        ],
      ],
      skin: 'line',
    })
  })

  /**
   * @description 快捷方式
   * @param {array} data
   */
  function renderShortcut(data) {
    laytpl(
      [
        '<div carousel-item>{{# layui.each(d, (index, ul)=>{ }}',
        '<ul class="layui-row layui-col-space10">',
        '{{# layui.each(ul.list, (idx, li)=>{ }}',
        '<li class="layui-col-xs3"><a lay-href="{{ li.url }}">',
        '<i class="layui-icon {{ li.icon }}"></i><cite>{{ li.text }}</cite>',
        '</a></li>{{# }) }}</ul>{{# }) }}</div>',
      ].join(''),
    ).render(data, (html) => {
      $('.layadmin-shortcut').html(html)
    })
  }

  /**
   * @description 待办事项
   * @param {array} data
   */
  function renderBacklog(data) {
    laytpl(
      [
        '<div carousel-item>{{# layui.each(d, (index, ul)=>{ }}',
        '<ul class="layui-row layui-col-space10">',
        '{{# layui.each(ul.list, (idx, li)=>{ }}<li class="layui-col-xs6">',
        '<a href="{{ li.href || "javascript:;" }}" {{ li.action ? "onclick="+ li.action : "" }} {{ li.url ? "lay-href="+ li.url : "" }} class="layadmin-backlog-body">',
        '<h3>{{ li.title }}</h3><p><cite class="{{ li.skin }}">{{ li.text }}</cite></p>',
        '</a></li>{{# }) }}</ul>{{# }) }}</div>',
      ].join(''),
    ).render(data, (html) => {
      $('.layadmin-backlog').html(html)
    })
  }

  /**
   * @description 产品动态
   * @param {array} data
   */
  function renderNews(data) {
    laytpl(
      [
        '<div carousel-item>',
        '{{# layui.each(d, (index, item)=>{ }}',
        '<div><a href="{{ item.url }}" class="{{ item.skin }}" target="_blank">{{ item.text }}</a></div>',
        '{{# }) }}</div>',
      ].join(''),
    ).render(data, (html) => {
      $('.layadmin-news').html(html)
    })
  }

  exports('console', {})
})
