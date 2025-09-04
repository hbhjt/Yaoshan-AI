USE tcm_ai;

-- 插入用户数据（仅普通账号密码登录）
INSERT INTO t_user (nickname, avatar_url, phone, status)
VALUES 
('张三', 'https://example.com/avatar/zhangsan.png', '13800000001', 1),
('李四', 'https://example.com/avatar/lisi.png', '13800000002', 1),
('王五', 'https://example.com/avatar/wangwu.png', '13800000003', 1);

-- 插入用户认证信息（普通账号密码登录，auth_type=2）
-- 密码演示：都用加密后的 "123456"（示例哈希，实际项目请用BCrypt生成）
INSERT INTO t_user_auth (user_id, auth_type, identity, credential)
VALUES 
(1, 2, 'zhangsan', '$2a$10$abcdefghijklmnopqrstuv'), 
(2, 2, 'lisi', '$2a$10$abcdefghijklmnopqrstuv'),
(3, 2, 'wangwu', '$2a$10$abcdefghijklmnopqrstuv');

-- 插入健康画像
INSERT INTO health_profile (user_id, age, gender, blood_pressure, blood_sugar, symptoms, diseases)
VALUES
(1, 28, 1, 0, 1, JSON_ARRAY('失眠', '疲劳'), JSON_ARRAY('糖尿病')),
(2, 35, 0, 1, 0, JSON_ARRAY('头痛'), JSON_ARRAY('高血压')),
(3, 22, 1, -1, 0, JSON_ARRAY('食欲不振'), JSON_ARRAY());

-- 插入药膳/奶茶数据
INSERT INTO recipe (type, name, intro, taboo, effect, suitable_time, method, tags)
VALUES
(0, '四神汤', '健脾补肾，适合脾胃虚弱人群', '孕妇慎用', '健脾益气', '晚餐', '将茯苓、芡实、莲子、山药炖煮', JSON_ARRAY('健脾', '益气')),
(0, '红枣枸杞粥', '补气养血，适合气血不足人群', '糖尿病患者少食', '养血安神', '早餐', '红枣枸杞与大米同煮', JSON_ARRAY('养血', '安神')),
(1, '菊花枸杞奶茶', '清肝明目，适合用眼疲劳', '孕妇慎用', '清肝明目', '下午茶', '菊花、枸杞加入奶茶冲泡', JSON_ARRAY('清肝', '护眼'));

-- 插入推荐记录
INSERT INTO recommendation (user_id, profile_id, recipe_id)
VALUES
(1, 1, 2),  -- 张三 推荐 红枣枸杞粥
(2, 2, 1),  -- 李四 推荐 四神汤
(3, 3, 3);  -- 王五 推荐 菊花枸杞奶茶

-- 插入内容数据
INSERT INTO t_content (title, description, content_type, image_url, video_url, author_id)
VALUES
('春季养生小知识', '春季养肝，饮食宜清淡', 'article', 'https://example.com/image/spring.png', NULL, 1),
('如何煮好一碗四神汤', '详细步骤讲解', 'video', NULL, 'https://example.com/video/sishentang.mp4', 2),
('中药奶茶的养生功效', '介绍几款常见的中药奶茶', 'article', 'https://example.com/image/tea.png', NULL, 3);

-- 插入收藏数据
INSERT INTO t_content_favorite (user_id, content_id)
VALUES
(1, 2),  -- 张三 收藏了 李四的视频
(2, 1),  -- 李四 收藏了 张三的文章
(3, 3);  -- 王五 收藏了 自己的文章
