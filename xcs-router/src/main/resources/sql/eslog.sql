CREATE TABLE `ES_LOG_NODE` (
  `ID` varchar(32) NOT NULL,
  `host` varchar(32) DEFAULT NULL COMMENT '节点host/ip',
  `PORT` varchar(50) DEFAULT NULL COMMENT '节点端口',
  `SEARCH_URL` varchar(200) DEFAULT NULL COMMENT '搜索uri',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志节点';


CREATE TABLE `ES_LOG_APP` (
  `ID` varchar(32) NOT NULL,
  `NODE_ID` varchar(32) DEFAULT NULL COMMENT '节点id',
  `APP_CODE` varchar(50) DEFAULT NULL COMMENT '应用编码',
  `APP_NAME` varchar(50) DEFAULT NULL COMMENT '应用名称',
  `APP_SEARCH_CODE` varchar(50) DEFAULT NULL COMMENT '应用搜索编码',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点应用';

