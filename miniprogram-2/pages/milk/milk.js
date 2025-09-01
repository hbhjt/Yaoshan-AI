Page({
  data: {
    tabs: ['全部', '养生', '减脂', '祛火', '祛湿', '安神'],
    currentTab: 0, // 当前选中标签索引
    tabContents: [
      // 「全部」标签对应的内容：包含“为你推荐”“季节限定”“人气奶茶”三个板块
      {
        sections: [
          {
            id:1,
            title: '为你推荐',
            drinks: [
              { 
                name: '茯苓安神奶茶', 
                desc: '经典配方融合优质牛乳，安神助眠且无咖啡因影响，适合晚间饮用', 
                tags: ['安神', '助眠'] ,
                image:"/assets/find.png",
                ingredients: ['茯苓粉', '全脂牛奶', '蜂蜜', '燕麦'], 
                price: 18, 
                nutrition: '热量：180kcal/杯 | 蛋白质：3.2g', 
                detail: '采用云南茯苓研磨成粉，搭配牧场直供鲜牛乳，慢火熬制2小时，保留食材原香，无添加蔗糖，健康无负担。' 
              },
            ]
          },
          {
            id:2,
            title: '季节限定',
            drinks: [
              {
                name: '薄荷陈皮茶饮', 
                desc: '清新组合带来清凉体验，含薄荷、陈皮、甘草等成分，解暑化湿', 
                tags: ['夏季特供', '祛湿'] 
              },
            ]
          },
          {
            id:3,
            title: '人气奶茶',
            drinks: [
              { 
                name: '玫瑰红枣奶茶', 
                desc: '玫瑰与红枣的完美结合，养颜美容改善气色', 
                tags: ['人气爆棚'] 
              },
            ]
          }
        ]
      },
      {
        sections: [
          {
            title: '养生推荐',
            drinks: [
              { 
                name: '养生枸杞奶茶', 
                desc: '枸杞搭配温润牛乳，养生补气血', 
                tags: ['养生', '补气血'] 
              },
            ]
          },
          {
            title: '养生推荐',
            drinks: [
              { 
                name: '养生枸杞奶茶', 
                desc: '枸杞搭配温润牛乳，养生补气血', 
                tags: ['养生', '补气血'] 
              },
            ]
          }
        ]
      },
      {
        sections: [
          {
            title: '减脂特选',
            drinks: [
              { 
                name: '低脂茯苓茶', 
                desc: '低脂配方，茯苓助力代谢，适合减脂期', 
                tags: ['减脂', '低脂'] 
              },
            ]
          }
        ]
      },
      {
        sections: [
          {
            title: '祛火茶饮',
            drinks: [
              { 
                name: '菊花金银花奶茶', 
                desc: '菊花与金银花结合，清热祛火', 
                tags: ['祛火', '清热'] 
              },
            ]
          }
        ]
      },
      {
        sections: [
          {
            title: '祛湿推荐',
            drinks: [
              { 
                name: '红豆薏米奶茶', 
                desc: '红豆薏米经典祛湿配方，搭配奶茶更易入口', 
                tags: ['祛湿', '消肿'] 
              },
            ]
          }
        ]
      },
      {
        sections: [
          {
            title: '安神特调',
            drinks: [
              { 
                name: '百合莲子奶茶', 
                desc: '百合莲子助眠安神，晚间饮用佳', 
                tags: ['安神', '助眠'] 
              },
            ]
          }
        ]
      }
    ],
  },
  onTabChange(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({ currentTab: index });
  },
  onDrinkTap(e) {
    // 获取当前点击饮品的id
    const drinkId = e.currentTarget.dataset.id;
    // 跳转到详情页（假设详情页路径为/pages/milk-detail/milk-detail）
    wx.navigateTo({
      url: `/pages/milk-detail/milk-detail?id=${drinkId}` // 携带id参数
    });
  }
})