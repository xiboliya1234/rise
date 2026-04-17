-- video path prefix table init
-- db: rise

USE rise;

CREATE TABLE IF NOT EXISTS `video_path_prefix` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `prefix_name` VARCHAR(100) DEFAULT NULL COMMENT 'display name',
  `prefix_value` VARCHAR(255) NOT NULL COMMENT 'path prefix value',
  `sort_num` INT DEFAULT 0 COMMENT 'sort order',
  `enabled` TINYINT(1) DEFAULT 1 COMMENT '1 enabled, 0 disabled',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT 'remark',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'created time',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updated time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prefix_value` (`prefix_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='video path prefix config';

INSERT INTO `video_path_prefix` (`prefix_name`, `prefix_value`, `sort_num`, `enabled`, `remark`)
VALUES
  ('Local C Folder', 'C://file', 10, 1, 'sample prefix'),
  ('Local D Video', 'D://video', 20, 1, 'sample prefix')
ON DUPLICATE KEY UPDATE
  `prefix_name` = VALUES(`prefix_name`),
  `sort_num` = VALUES(`sort_num`),
  `enabled` = VALUES(`enabled`),
  `remark` = VALUES(`remark`);