--数据库初始化脚本

--创建数据库
CREATE DATABASE seckill;
--使用数据库
use seckill;

--创建秒杀库存表
CREATE TABLE `seckill` (
`seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
`name` varchar(120) NOT NULL COMMENT '商品名称',
`number` int(11) NOT NULL COMMENT '库存数量',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`start_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀开始时间',
`end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀结束时间',
PRIMARY KEY (`seckill_id`),
KEY `idx_start_time` (`start_time`),
KEY `idx_end_time` (`end_time`),
KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1006 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

--初始化数据
INSERT INTO
    sekill(name,number,start_time,end_time)
values
    ('100元秒杀iphone7',100,'2017-3-1 00:00:00','2017-3-2 00:00:00'),
    ('500元秒杀max',300,'2017-3-1 00:00:00','2017-3-2 00:00:00'),
    ('600元秒杀Mi5',500,'2017-3-1 00:00:00','2017-3-2 00:00:00'),
    ('300元秒杀vivo',200,'2017-3-1 00:00:00','2017-3-2 00:00:00'),
    ('10元秒杀note5',300,'2017-3-1 00:00:00','2017-3-2 00:00:00'),
    ('200元秒杀小米7',800,'2017-3-1 00:00:00','2017-3-2 00:00:00');

--秒杀成功明细表
--用户登录认证的相关信息
CREATE TABLE `success_killed` (
`seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品ID',
`user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
`state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '秒杀状态：-1：无效  0;成功  1：已付款',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`seckill_id`,`user_phone`),
KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';