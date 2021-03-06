package syamwu.xchushi.easylog.constant;

import syamwu.xchushi.fw.common.entity.Entity;

/**
 * 日志传输实体
 * 
 * @author: syam_wu
 * @date: 2018
 */
public class LoggerEntity extends Entity<LoggerEvent>{

    public LoggerEntity(LoggerEvent data, EntityType entityType) {
        super(data, entityType);
    }
    
    public LoggerEntity(LoggerEvent data) {
        super(data, EntityType.nomal);
    }

}
