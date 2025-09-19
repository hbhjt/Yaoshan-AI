Page({
  data: {
    currentDrink: null // 存储当前饮品的详细信息
  },

  onLoad(options) {
    // 1. 获取从列表页传递的饮品id
    const drinkId = Number(options.id); // 转换为数字类型（与数据源保持一致）

    // 2. 获取数据源（两种方式，选其一）
    // 方式一：从全局数据获取（推荐，避免数据冗余）
    const app = getApp();
    const allDrinks = app.globalData.drinkDetails; // 需在app.js中定义globalData

    // 方式二：从列表页数据复制（简单但数据冗余）
    // const allDrinks = [
    //   { id: 1, ... }, // 与列表页的drinkDetails保持一致
    //   { id: 2, ... }
    // ];

    // 3. 根据id匹配对应的饮品信息
    const currentDrink = allDrinks.find(drink => drink.id === drinkId);

    // 4. 更新数据到视图
    if (currentDrink) {
      this.setData({ currentDrink });
    } else {
      // 未找到对应饮品时的处理（如返回上一页）
      wx.showToast({ title: '未找到饮品信息', icon: 'none' });
      setTimeout(() => wx.navigateBack(), 1500);
    }
  },
  // 详情页JS中请求接口
//onLoad(options) {
  //const drinkId = options.id;
 // wx.request({
   // url: `https://你的接口地址/drink/${drinkId}`,
    //success: (res) => {
     // this.setData({ currentDrink: res.data });
   // }
  //});
//}
})