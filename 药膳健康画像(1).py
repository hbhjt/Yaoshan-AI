from flask import Flask, request, jsonify

from Lib.http.cookiejar import debug

app = Flask(__name__)

# 模拟固定的药膳数据，根据需求从数据库获取
MEDICATED_DIET = {
    "药膳名称": "当归红枣乌鸡汤",
    "主要食材": ["当归", "红枣", "乌鸡", "枸杞"],
    "功效": "补血活血、调经止痛，适合气血不足、月经不调者"
}

# 模拟健康画像历史数据
HEALTH_PROFILE_HISTORY = [
    {
        "profileId": 101,
        "age": 20,
        "gender": 1,
        "bloodPressure": 1,
        "bloodSugar": 0,
        "symptoms": ["疲劳", "失眠"],
        "diseases": ["高血压"],
        "createdTime": "2025-09-04 20:30:00"
    },
    {
        "profileId": 102,
        "age": 30,
        "gender": 0,
        "bloodPressure": 0,
        "bloodSugar": 1,
        "symptoms": ["头晕", "乏力"],
        "diseases": ["糖尿病"],
        "createdTime": "2025-09-05 15:45:00"
    }
]

@app.route('/health/profile', methods=['POST'])
def health_profile():
    # 获取前端传入的 JSON 数据
    data = request.json
    # 这里可以对传入的数据进行验证等操作
    user_id = data.get('user_id')
    age = data.get('age')
    gender = data.get('gender')
    blood_pressure = data.get('bloodPressure')
    blood_sugar = data.get('bloodSugar')
    symptoms = data.get('symptoms')
    diseases = data.get('diseases')

    print(
        f"接收到用户ID为{user_id}的健康数据，年龄：{age}，性别：{gender}，血压：{blood_pressure}，血糖：{blood_sugar}，症状：{symptoms}，疾病：{diseases}")

    # 返回固定药膳数据
    return jsonify({
        "code": 200,
        "msg": "success",
        "data": MEDICATED_DIET
    })


if __name__ == '__main__':
    app.run(host='localhost', port=8080,debug=True)