-- 创建数据库
CREATE DATABASE IF NOT EXISTS health_recipe DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE health_recipe;

-- 1. 用户表（user）
CREATE TABLE `user` (
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户唯一ID（UUID）',
  `openid` VARCHAR(64) DEFAULT NULL COMMENT '微信OpenID',
  `phone` VARCHAR(16) DEFAULT NULL COMMENT '手机号（加密存储）',
  `password` VARCHAR(64) DEFAULT NULL COMMENT '登录密码（BCrypt加密）',
  `nickname` VARCHAR(64) NOT NULL COMMENT '用户昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `constitution` VARCHAR(32) DEFAULT NULL COMMENT '体质标签（阴虚/阳虚/平和等）',
  `diet_taboo` JSON DEFAULT NULL COMMENT '饮食禁忌食材（数组）',
  `flavor_prefer` VARCHAR(32) DEFAULT NULL COMMENT '偏好口味',
  `has_complete` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否完善信息（0-未完善，1-已完善）',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '软删除标记（0-正常，1-删除）',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_openid` (`openid`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_constitution` (`constitution`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 验证码表（verify_code）
CREATE TABLE `verify_code` (
  `verify_id` VARCHAR(32) NOT NULL COMMENT '验证码唯一ID（UUID）',
  `phone` VARCHAR(16) NOT NULL COMMENT '接收验证码的手机号',
  `code` VARCHAR(6) NOT NULL COMMENT '6位数字验证码',
  `code_type` ENUM('register', 'find_pwd') NOT NULL COMMENT '验证码类型',
  `send_time` DATETIME NOT NULL COMMENT '发送时间',
  `expire_time` DATETIME NOT NULL COMMENT '过期时间',
  `is_used` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已使用（0-未使用，1-已使用）',
  PRIMARY KEY (`verify_id`),
  KEY `idx_phone_type` (`phone`, `code_type`),
  KEY `idx_expire_used` (`expire_time`, `is_used`),
  KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验证码表';

-- 3. 内容表（content）
CREATE TABLE `content` (
  `content_id` VARCHAR(32) NOT NULL COMMENT '内容唯一ID（UUID）',
  `content_type` ENUM('post', 'video') NOT NULL COMMENT '内容类型',
  `category` ENUM('herbal_meal', 'herbal_milk_tea') NOT NULL COMMENT '内容分类',
  `title` VARCHAR(128) NOT NULL COMMENT '内容标题',
  `cover` VARCHAR(255) DEFAULT NULL COMMENT '封面图URL',
  `intro` VARCHAR(255) DEFAULT NULL COMMENT '内容简介',
  `full_content` TEXT DEFAULT NULL COMMENT '完整文本内容（仅post类型）',
  `video_url` VARCHAR(255) DEFAULT NULL COMMENT '视频链接（仅video类型）',
  `ingredients` JSON DEFAULT NULL COMMENT '食材配方（数组）',
  `method` TEXT DEFAULT NULL COMMENT '制作步骤',
  `efficacy` VARCHAR(128) DEFAULT NULL COMMENT '功效说明',
  `author_id` VARCHAR(32) NOT NULL COMMENT '作者ID',
  `author_name` VARCHAR(64) NOT NULL COMMENT '作者名称',
  `author_avatar` VARCHAR(255) DEFAULT NULL COMMENT '作者头像',
  `like_count` INT(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `collect_count` INT(11) NOT NULL DEFAULT 0 COMMENT '收藏数',
  `comment_count` INT(11) NOT NULL DEFAULT 0 COMMENT '评论数',
  `publish_time` DATETIME NOT NULL COMMENT '发布时间',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '软删除标记',
  PRIMARY KEY (`content_id`),
  FULLTEXT KEY `ft_title` (`title`),
  KEY `idx_type_category` (`content_type`, `category`),
  KEY `idx_efficacy` (`efficacy`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_author_id` (`author_id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表（帖子/视频）';

-- 4. 评论表（comment）
CREATE TABLE `comment` (
  `comment_id` VARCHAR(32) NOT NULL COMMENT '评论唯一ID（UUID）',
  `content_id` VARCHAR(32) NOT NULL COMMENT '关联内容ID',
  `content_type` ENUM('post', 'video', 'ai_recipe') NOT NULL COMMENT '关联内容类型',
  `user_id` VARCHAR(32) NOT NULL COMMENT '评论用户ID',
  `user_nickname` VARCHAR(64) NOT NULL COMMENT '评论用户昵称',
  `user_avatar` VARCHAR(255) DEFAULT NULL COMMENT '评论用户头像',
  `comment_content` VARCHAR(500) NOT NULL COMMENT '评论内容',
  `reply_count` INT(11) NOT NULL DEFAULT 0 COMMENT '回复数',
  `comment_time` DATETIME NOT NULL COMMENT '评论时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '软删除标记',
  PRIMARY KEY (`comment_id`),
  KEY `idx_content_type` (`content_id`, `content_type`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_comment_time` (`comment_time`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 5. 点赞表（like）
CREATE TABLE `like` (
  `like_id` VARCHAR(32) NOT NULL COMMENT '点赞唯一ID（UUID）',
  `content_id` VARCHAR(32) NOT NULL COMMENT '关联内容ID',
  `content_type` ENUM('post', 'video', 'ai_recipe') NOT NULL COMMENT '关联内容类型',
  `user_id` VARCHAR(32) NOT NULL COMMENT '点赞用户ID',
  `like_time` DATETIME NOT NULL COMMENT '点赞时间',
  `is_cancel` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否取消点赞（0-正常，1-取消）',
  PRIMARY KEY (`like_id`),
  UNIQUE KEY `uk_user_content_type` (`user_id`, `content_id`, `content_type`, `is_cancel`),
  KEY `idx_content_type_cancel` (`content_id`, `content_type`, `is_cancel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- 6. 收藏表（collect）
CREATE TABLE `collect` (
  `collect_id` VARCHAR(32) NOT NULL COMMENT '收藏唯一ID（UUID）',
  `user_id` VARCHAR(32) NOT NULL COMMENT '收藏用户ID',
  `content_id` VARCHAR(32) NOT NULL COMMENT '关联内容ID',
  `content_type` ENUM('post', 'video', 'ai_recipe') NOT NULL COMMENT '关联内容类型',
  `collect_remark` VARCHAR(128) DEFAULT NULL COMMENT '收藏备注',
  `collect_time` DATETIME NOT NULL COMMENT '收藏时间',
  `is_cancel` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否取消收藏（0-正常，1-取消）',
  PRIMARY KEY (`collect_id`),
  UNIQUE KEY `uk_user_content_type` (`user_id`, `content_id`, `content_type`, `is_cancel`),
  KEY `idx_user_time` (`user_id`, `collect_time`),
  KEY `idx_content_type_cancel` (`content_id`, `content_type`, `is_cancel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 7. AI配方表（ai_recipe）
CREATE TABLE `ai_recipe` (
  `recipe_id` VARCHAR(32) NOT NULL COMMENT 'AI配方唯一ID（UUID）',
  `user_id` VARCHAR(32) NOT NULL COMMENT '生成用户ID',
  `generate_type` ENUM('herbal_meal', 'herbal_milk_tea') NOT NULL COMMENT '生成类型',
  `recipe_name` VARCHAR(128) NOT NULL COMMENT '配方名称',
  `ingredients` JSON NOT NULL COMMENT '食材清单（含用量、功效）',
  `make_steps` JSON NOT NULL COMMENT '制作步骤（数组）',
  `adapt_efficacy` VARCHAR(255) NOT NULL COMMENT '适配功效',
  `taboo_tips` VARCHAR(255) DEFAULT NULL COMMENT '禁忌提示',
  `season` VARCHAR(64) DEFAULT NULL COMMENT '时令',
  `extra_demand` VARCHAR(128) DEFAULT NULL COMMENT '额外需求',
  `cover` VARCHAR(255) DEFAULT NULL COMMENT '配方封面图',
  `generate_time` DATETIME NOT NULL COMMENT '生成时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '软删除标记',
  PRIMARY KEY (`recipe_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type_season` (`generate_type`, `season`),
  KEY `idx_adapt_efficacy` (`adapt_efficacy`),
  KEY `idx_generate_time` (`generate_time`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI配方表';

-- 8. 时令配置表（season_config）
CREATE TABLE `season_config` (
  `season_id` VARCHAR(32) NOT NULL COMMENT '时令唯一ID（UUID）',
  `season_name` VARCHAR(64) NOT NULL COMMENT '时令名称（如夏季-芒种）',
  `season` ENUM('spring', 'summer', 'autumn', 'winter') NOT NULL COMMENT '季节',
  `solar_term` VARCHAR(32) DEFAULT NULL COMMENT '节气',
  `health_tips` VARCHAR(255) NOT NULL COMMENT '时令养生要点',
  `suitable_ingredients` JSON DEFAULT NULL COMMENT '适合时令的食材（数组）',
  `start_date` DATE NOT NULL COMMENT '时令开始日期',
  `end_date` DATE NOT NULL COMMENT '时令结束日期',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`season_id`),
  KEY `idx_date_range` (`start_date`, `end_date`),
  KEY `idx_season_term` (`season`, `solar_term`),
  KEY `idx_season_name` (`season_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='时令配置表';

-- 9. 搜索历史表（search_history）
CREATE TABLE `search_history` (
  `history_id` VARCHAR(32) NOT NULL COMMENT '搜索记录唯一ID（UUID）',
  `user_id` VARCHAR(32) NOT NULL COMMENT '搜索用户ID',
  `keyword` VARCHAR(128) NOT NULL COMMENT '搜索关键词',
  `content_types` VARCHAR(128) DEFAULT NULL COMMENT '筛选内容类型（逗号分隔）',
  `filters` JSON DEFAULT NULL COMMENT '高级筛选条件',
  `sort_type` ENUM('relevance', 'hot', 'new') DEFAULT 'relevance' COMMENT '排序方式',
  `search_time` DATETIME NOT NULL COMMENT '搜索时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '软删除标记',
  PRIMARY KEY (`history_id`),
  KEY `idx_user_time` (`user_id`, `search_time`),
  KEY `idx_keyword` (`keyword`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索历史表';