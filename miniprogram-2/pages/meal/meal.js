// pages/meal/meal.js
Page({
  data: {
    bloodPressure: 'normal', 
    bloodSugar: 'normal' ,
    tags: [
      { name: '疲劳', checked: false },
      { name: '失眠', checked: false },
      { name: '消化不良', checked: false },
      { name: '易感冒', checked: false },
      { name: '头晕', checked: false },
      { name: '食欲不振', checked: false },
      { name: '便秘', checked: false },
      { name: '腰酸背痛', checked: false },
      { name: '手脚冰凉', checked: false },
      { name: '无不良反应', checked: false },
      { name: '妊娠', checked: false }
    ],
    diseases: [
      { name: '高血压', checked: false },
      { name: '糖尿病', checked: false },
      { name: '胃炎', checked: false },
      { name: '冠心病', checked: false },
      { name: '痛风', checked: false },
      { name: '过敏史', checked: false },
      { name: '其他', checked: false },
      { name: '无', checked: false }
    ]
  },
  handleIndexSelect(e) {
    const { type, value } = e.currentTarget.dataset;
    this.setData({
      [type]: value
    });
  },
  handleTagChange(e) {
    const selectedValues = e.detail.value;
    const newTags = this.data.tags.map(item => {
      item.checked = selectedValues.includes(item.name);
      return item;
    });
    this.setData({ tags: newTags });
    console.log('当前选中的标签：', selectedValues);
  },
  handleDiseaseChange(e) {
    const selectedValues = e.detail.value;
    const newDiseases = this.data.diseases.map(item => {
      item.checked = selectedValues.includes(item.name);
      return item;
    });
    this.setData({ diseases: newDiseases });
    console.log('选中的值：', selectedValues);
  },
  onGenerate() {
    wx.showToast({
      title: '生成中...',
      icon: 'loading',
      duration: 2000
    })
  }
})