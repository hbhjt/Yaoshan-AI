Page({
  data: {
    currentTab: 'medicine', 
    medicineList: [
      {
        image: '/assets/folder.png', 
        title: '百合杞子养心汤',
        desc: '滋阴养心，安神助眠',
        suitable: '适宜平和体质',
        collectTime: '3天前收藏'
      },
      {
        image: '/assets/forward.png',
        title: '当归生姜羊肉汤',
        desc: '温补气血，祛寒暖身',
        suitable: '适宜阳虚体质',
        collectTime: '上周收藏'
      },
      {
        image: '/assets/forward.png',
        title: '枸杞菊花茶',
        desc: '清肝明目，养阴清热',
        suitable: '适宜阴虚体质',
        collectTime: '1个月前收藏'
      },
      {
        image: '/assets/scan.png',
        title: '枸杞菊花茶',
        desc: '清肝明目，养阴清热',
        suitable: '适宜阴虚体质',
        collectTime: '1个月前收藏'
      },
      {
        image: '/assets/scan.png',
        title: '枸杞菊花茶',
        desc: '清肝明目，养阴清热',
        suitable: '适宜阴虚体质',
        collectTime: '1个月前收藏'
      }
    ],
    milkTeaList: [
      {
        image: '/assets/forward.png',
        title: '珍珠奶茶',
        desc: '经典黑糖珍珠 + 鲜牛奶 + 红茶底',
        suitable: '喜欢甜口人群',
        collectTime: '2天前收藏'
      },
      {
        image: '/assets/index.png',
        title: '抹茶奶绿',
        desc: '宇治抹茶粉 + 淡奶油 + 绿茶底',
        suitable: '喜欢微苦回甘口感',
        collectTime: '5天前收藏'
      },
      {
        image: '/assets/milk.png',
        title: '杨枝甘露',
        desc: '芒果泥 + 西柚粒 + 椰奶 + 西米',
        suitable: '喜欢热带水果风味',
        collectTime: '1周前收藏'
      },
      {
        image: '/assets/milk.png',
        title: '杨枝甘露',
        desc: '芒果泥 + 西柚粒 + 椰奶 + 西米',
        suitable: '喜欢热带水果风味',
        collectTime: '1周前收藏'
      }
    ]
  },
  handleTabSwitch(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ currentTab: tab });
  }
});