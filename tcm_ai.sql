-- 创建数据库
CREATE DATABASE IF NOT EXISTS tcm_ai
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE tcm_ai;

-- 用户表
CREATE TABLE t_user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    openid VARCHAR(64) NOT NULL UNIQUE COMMENT '微信OpenID，唯一标识',
    phone VARCHAR(20) UNIQUE COMMENT '手机号（可空）',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    physique_tags VARCHAR(255) COMMENT '体质标签（如阴虚/阳虚）',
    dietary_restrictions VARCHAR(255) COMMENT '饮食禁忌',
    taste_preferences VARCHAR(255) COMMENT '口味偏好',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 内容表
CREATE TABLE t_content (
    content_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '内容ID',
    title VARCHAR(100) NOT NULL COMMENT '内容标题',
    description TEXT COMMENT '内容简介',
    content_type VARCHAR(20) NOT NULL COMMENT '内容类型(article/video)',
    image_url VARCHAR(255) COMMENT '图片URL',
    video_url VARCHAR(255) COMMENT '视频URL',
    author_id BIGINT COMMENT '作者ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_content_author FOREIGN KEY (author_id) REFERENCES t_user(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表';

-- 点赞表
CREATE TABLE t_content_like (
    like_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content_id BIGINT NOT NULL COMMENT '内容ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES t_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_like_content FOREIGN KEY (content_id) REFERENCES t_content(content_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_content (user_id, content_id) -- 防止重复点赞
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容点赞表';

-- 收藏表
CREATE TABLE t_content_favorite (
    fav_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content_id BIGINT NOT NULL COMMENT '内容ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    CONSTRAINT fk_fav_user FOREIGN KEY (user_id) REFERENCES t_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_fav_content FOREIGN KEY (content_id) REFERENCES t_content(content_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_content (user_id, content_id) -- 防止重复收藏
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容收藏表';

-- 评论表
CREATE TABLE t_content_comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    content_id BIGINT NOT NULL COMMENT '内容ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    comment_text TEXT NOT NULL COMMENT '评论内容',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    CONSTRAINT fk_comment_content FOREIGN KEY (content_id) REFERENCES t_content(content_id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES t_user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容评论表';