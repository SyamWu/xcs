package syamwu.xchushi.fw.transfer.sender;

import syamwu.xchushi.fw.common.environment.Configure;
import syamwu.xchushi.fw.transfer.collect.Collected;

/**
 * 抽象传输器
 * 
 * @author: syam_wu
 * @date: 2018
 */
public abstract class AbstractSender implements Sender {
    
    @SuppressWarnings("rawtypes")
    protected Collected collected;
    
    protected Configure configure;
    
    protected boolean started = false;
    
    protected AbstractSender(Configure configure){
        this.configure = configure;
    }

    @SuppressWarnings({ "rawtypes" })
    public void setCollectible(Collected collected){
        this.collected = collected;
    }
    
    public boolean started(){
        return started;
    }
    
    public abstract Object synSend(Object obj) throws Exception;
    
}
