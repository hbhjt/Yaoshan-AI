USE tcm_ai;

-- 插入用户
INSERT INTO t_user (openid, phone, nickname, avatar_url, physique_tags, dietary_restrictions, taste_preferences)
VALUES 
('openid_001', '13800138000', '张三', 'http://example.com/avatar1.jpg', '平和质', '无羊肉', '辛辣,甜'),
('openid_002', '13800138001', '李四', 'http://example.com/avatar2.jpg', '痰湿质', '少盐', '清淡,酸'),
('openid_003', '13800138002', '王五', 'http://example.com/avatar3.jpg', '阴虚质', '忌海鲜', '清淡');

-- 插入内容（药膳/中药奶茶）
INSERT INTO t_content (title, description, content_type, image_url, video_url, author_id)
VALUES
('四神汤的做法', '经典养生汤品，适合脾虚肾虚体质食用。', 'article', 'http://example.com/sishen.jpg', NULL, 1),
('杞枣奶茶推荐', '枸杞+红枣搭配牛奶，补益气血。', 'article', 'http://example.com/qizao.jpg', NULL, 2),
('养胃药膳教学视频', '视频教学：山药健脾养胃食疗。', 'video', 'http://example.com/shanyao.jpg', 'http://example.com/shanyao.mp4', 3);

-- 插入点赞
INSERT INTO t_content_like (user_id, content_id) VALUES
(1, 1),
(2, 1),
(3, 2);

-- 插入收藏
INSERT INTO t_content_favorite (user_id, content_id) VALUES
(1, 1),
(2, 2);

-- 插入评论
INSERT INTO t_content_comment (content_id, user_id, comment_text) VALUES
(1, 1, '四神汤味道很好，养生效果不错！'),
(1, 2, '我也试过，确实健脾益气。'),
(2, 3, '杞枣奶茶很甜，女生应该喜欢。');