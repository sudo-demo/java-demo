-- 使用 demo 数据库
USE `demo`;

-- 修改 sys_user 表结构
-- 删除 real_name 字段
ALTER TABLE `sys_user` DROP COLUMN `real_name`;

-- 更新初始数据中的字段名
UPDATE `sys_user` SET `user_id` = 'admin', `user_name` = '管理员' WHERE `id` = 1;
UPDATE `sys_user` SET `user_id` = 'test', `user_name` = '测试用户' WHERE `id` = 2;

-- 查询验证
SELECT 'sys_user table updated successfully!' AS message;
SELECT `id`, `user_id`, `user_name`, `email`, `phone` FROM `sys_user`;