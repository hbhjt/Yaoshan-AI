-- 创建tcm_ai数据库
CREATE DATABASE IF NOT EXISTS tcm_ai
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE tcm_ai;

-- 用户表
CREATE TABLE t_user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一ID（主表主键）',
    nickname VARCHAR(50) COMMENT '用户昵称（微信昵称/自定义昵称）',
    avatar_url VARCHAR(255) COMMENT '用户头像URL（微信头像/自定义头像）',
    phone VARCHAR(20) UNIQUE COMMENT '手机号（可用于普通登录的账号，也可仅作为联系信息，唯一）',
    status TINYINT DEFAULT 1 COMMENT '用户状态：1-正常，0-禁用（防止无效账号登录）',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '账号创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '信息更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户主表（存储所有用户公共信息）';

-- 登录凭证表
CREATE TABLE t_user_auth (
    auth_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '凭证ID（主键）',
    user_id BIGINT NOT NULL COMMENT '关联的用户ID（关联t_user.user_id）',
    auth_type TINYINT NOT NULL COMMENT '登录方式类型：1-微信登录，2-普通账号密码登录',
    identity VARCHAR(64) NOT NULL COMMENT '登录标识：\n- 微信登录时存openid\n- 普通登录时存“账号”（可自定义账号名或用手机号）',
    credential VARCHAR(128) COMMENT '登录凭证：\n- 微信登录时可存空（因openid已唯一，无需额外凭证）\n- 普通登录时存【加密后的密码】（必须用BCrypt等算法加密）',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '凭证创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '凭证更新时间（如修改密码时）',
    -- 关键约束：同一登录方式下，登录标识不能重复（避免同一账号被多个用户绑定）
    UNIQUE KEY uk_auth_type_identity (auth_type, identity),
    -- 关联用户主表，确保用户存在（用户删除时，凭证也删除）
    FOREIGN KEY fk_auth_user (user_id) REFERENCES t_user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录凭证表（区分微信/普通账号登录）';

-- 健康画像
CREATE TABLE health_profile (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '健康画像ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    age INT,
    gender TINYINT COMMENT '1=男, 0=女',
    blood_pressure TINYINT COMMENT '-1=偏低, 0=正常, 1=偏高',
    blood_sugar TINYINT COMMENT '-1=偏低, 0=正常, 1=偏高',
    symptoms JSON COMMENT '身体状况自述标签, 如 ["疲劳","失眠"]',
    diseases JSON COMMENT '慢性病/疾病史, 如 ["高血压","糖尿病"]',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '填写时间',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康画像表';


-- 药膳 & 中药奶茶表
CREATE TABLE recipe (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type TINYINT NOT NULL COMMENT '0=药膳, 1=中药奶茶',
    name VARCHAR(100) NOT NULL COMMENT '名称',
    intro TEXT COMMENT '简介',
    taboo TEXT COMMENT '饮食忌口',
    effect TEXT COMMENT '药膳=主治; 奶茶=功能',
    suitable_time VARCHAR(50) COMMENT '适宜饮用时间',
    method TEXT COMMENT '具体做法',
    tags JSON COMMENT '功能标签, 如 ["养生","祛湿","安神"]',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 

-- 推荐记录表
CREATE TABLE recommendation (
    rec_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '推荐记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    profile_id BIGINT NOT NULL COMMENT '健康画像ID',
    recipe_id BIGINT NOT NULL COMMENT '推荐的药膳/奶茶ID',
    recommend_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '推荐时间',
    FOREIGN KEY (user_id) REFERENCES t_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (profile_id) REFERENCES health_profile(profile_id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推荐记录表';


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
