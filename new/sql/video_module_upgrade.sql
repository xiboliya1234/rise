-- Video module upgrade script (compatible with MySQL 5.7/8.0)
-- Database: rise

USE `rise`;

-- 1) Create table if not exists
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Video info table';

-- 2) Add missing columns (for old table compatibility)
SET @col_exists := (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'videoinfo' AND COLUMN_NAME = 'videourl'
);
SET @sql := IF(@col_exists = 0,
  'ALTER TABLE `videoinfo` ADD COLUMN `videourl` VARCHAR(500) NOT NULL DEFAULT '''' COMMENT ''Playable URL/path''',
  'SELECT ''skip add videourl''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'videoinfo' AND COLUMN_NAME = 'videoimg'
);
SET @sql := IF(@col_exists = 0,
  'ALTER TABLE `videoinfo` ADD COLUMN `videoimg` VARCHAR(500) DEFAULT NULL COMMENT ''Cover URL''',
  'SELECT ''skip add videoimg''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'videoinfo' AND COLUMN_NAME = 'videotag'
);
SET @sql := IF(@col_exists = 0,
  'ALTER TABLE `videoinfo` ADD COLUMN `videotag` VARCHAR(255) DEFAULT NULL COMMENT ''Video tags''',
  'SELECT ''skip add videotag''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'videoinfo' AND COLUMN_NAME = 'created_at'
);
SET @sql := IF(@col_exists = 0,
  'ALTER TABLE `videoinfo` ADD COLUMN `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT ''Created time''',
  'SELECT ''skip add created_at''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'videoinfo' AND COLUMN_NAME = 'updated_at'
);
SET @sql := IF(@col_exists = 0,
  'ALTER TABLE `videoinfo` ADD COLUMN `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ''Updated time''',
  'SELECT ''skip add updated_at''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 3) Add indexes safely
SET @idx_exists := (
  SELECT COUNT(*) FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'videoinfo' AND INDEX_NAME = 'idx_videoname'
);
SET @sql := IF(@idx_exists = 0,
  'ALTER TABLE `videoinfo` ADD KEY `idx_videoname` (`videoname`)',
  'SELECT ''skip add idx_videoname''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @idx_exists := (
  SELECT COUNT(*) FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'videoinfo' AND INDEX_NAME = 'idx_created_at'
);
SET @sql := IF(@idx_exists = 0,
  'ALTER TABLE `videoinfo` ADD KEY `idx_created_at` (`created_at`)',
  'SELECT ''skip add idx_created_at''');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 4) Unique index on videocode (only if no duplicate values)
SET @dup_cnt := (
  SELECT COUNT(*) FROM (
    SELECT videocode
    FROM videoinfo
    WHERE videocode IS NOT NULL AND videocode <> ''
    GROUP BY videocode
    HAVING COUNT(*) > 1
  ) t
);

SET @idx_exists := (
  SELECT COUNT(*) FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'videoinfo' AND INDEX_NAME = 'uk_videocode'
);

SET @sql := IF(@idx_exists > 0,
  'SELECT ''skip add uk_videocode (already exists)''',
  IF(@dup_cnt > 0,
    'SELECT ''skip add uk_videocode (duplicate videocode exists, please clean data first)''',
    'ALTER TABLE `videoinfo` ADD UNIQUE KEY `uk_videocode` (`videocode`)'
  )
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Optional: check duplicates (if you need to clean before unique index)
-- SELECT videocode, COUNT(*) AS cnt
-- FROM videoinfo
-- WHERE videocode IS NOT NULL AND videocode <> ''
-- GROUP BY videocode
-- HAVING COUNT(*) > 1;
