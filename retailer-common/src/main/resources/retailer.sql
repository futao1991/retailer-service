create database if not exists `seata`;

use seata;

#用户表
create table if not exists `user_table`(
   id bigint(20) primary key not null auto_increment,
   name varchar(100) not null,
   password varchar(100) not null,
   money bigint(20) not null,
   version int(11) not null
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

#订单信息表
create table if not exists `order_info`(
  id varchar(50) primary key not null,
  userid bigint(20) not null,
  commodity_id varchar(50) not null,
  `count` int(11) not null,
  total_price bigint(20) not null,
  status varchar(20) not null,
  create_time varchar(20),
  delivery_time varchar(20),
  complete_time varchar(20)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

#mq事务表
create table if not exists `mq_transaction`(
  id varchar(50) primary key not null,
  business varchar(50) primary key not null,
  orderId varchar(50) primary key not null
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;