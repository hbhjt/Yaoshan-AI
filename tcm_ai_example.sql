USE tcm_ai;

-- 插入用户
INSERT INTO t_user (openid, phone, nickname, avatar_url, physique_tags, dietary_restrictions, taste_preferences)
VALUES 
('openid_001', '13800138000', '张三', 'http://example.com/avatar1.jpg', '平和质', '无羊肉', '辛辣,甜'),
('openid_002', '13800138001', '李四', 'http://example.com/avatar2.jpg', '痰湿质', '少盐', '清淡,酸'),
('openid_003', '13800138002', '王五', 'http://example.com/avatar3.jpg', '阴虚质', '忌海鲜', '清淡'),
('openid_004', '13800138003', '赵六', 'http://example.com/avatar4.jpg', '阴虚火旺质', '忌辛辣', '清淡,甜'),
('openid_005', '13800138004', '孙七', 'http://example.com/avatar5.jpg', '湿热质', '忌油腻', '麻辣,咸'),
('openid_006', '13800138005', '周八', 'http://example.com/avatar6.jpg', '气虚质', '忌生冷', '香甜,淡');

-- 插入内容（药膳/中药奶茶）
INSERT INTO t_content (title, description, content_type, image_url, video_url, author_id)
VALUES
('四神汤的做法', '经典养生汤品，适合脾虚肾虚体质食用。', 'article', 'http://example.com/sishen.jpg', NULL, 1),
('杞枣奶茶推荐', '枸杞+红枣搭配牛奶，补益气血。', 'article', 'http://example.com/qizao.jpg', NULL, 2),
('养胃药膳教学视频', '视频教学：山药健脾养胃食疗。', 'video', 'http://example.com/shanyao.jpg', 'http://example.com/shanyao.mp4', 3),
('百合银耳羹的做法', '润肺止咳，滋阴润燥，适合阴虚燥咳体质食用。', 'article', 'http://example.com/baihe.jpg', NULL, 4),
('红豆薏米水推荐', '祛湿消肿、健脾益胃，适合湿热体质人群。', 'article', 'http://example.com/hongdou.jpg', NULL, 5),
('枸杞菊花茶的做法', '养肝明目、清热降火，适合经常用眼、肝火旺的人。', 'article', 'http://example.com/gouqicha.jpg', NULL, 6),;
-- 插入点赞
INSERT INTO t_content_like (user_id, content_id) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 4),
(5, 5),
(6, 6);

-- 插入收藏
INSERT INTO t_content_favorite (user_id, content_id) VALUES
(1, 1),
(2, 2),
(3, 2),
(4, 4),
(5, 5),
(6, 6);

-- 插入评论
INSERT INTO t_content_comment (content_id, user_id, comment_text) VALUES
(1, 1, '四神汤味道很好，养生效果不错！'),
(1, 2, '我也试过，确实健脾益气。'),
(2, 3, '杞枣奶茶很甜，女生应该喜欢。'),
(4, 4, '百合银耳羹很滋润，秋冬喝正好。'),
(5, 5, '红豆薏米水喝了几天，感觉身体轻快多了！'),
(6, 6, '枸杞菊花茶味道不错，眼睛也没那么干涩了。')；
