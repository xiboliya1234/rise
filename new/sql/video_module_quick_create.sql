-- Quick create script for video module
-- Compatible with MySQL 5.7/8.0

USE `rise`;

CREATE TABLE IF NOT EXISTS `videoinfo` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `videoname` VARCHAR(200) NOT NULL COMMENT 'Video name',
  `videocode` VARCHAR(100) NOT NULL COMMENT 'Video code',
  `videomsg` VARCHAR(1000) DEFAULT NULL COMMENT 'Video description',
  `videodate` VARCHAR(50) DEFAULT NULL COMMENT 'Video date/year',
  `videoactor` VARCHAR(255) DEFAULT NULL COMMENT 'Actor information',
  `videoimg` VARCHAR(500) DEFAULT NULL COMMENT 'Cover URL',
  `videotag` VARCHAR(255) DEFAULT NULL COMMENT 'Video tags',
  `videourl` VARCHAR(500) NOT NULL COMMENT 'Playable URL/path',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_videocode` (`videocode`),
  KEY `idx_videoname` (`videoname`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Video info table';

