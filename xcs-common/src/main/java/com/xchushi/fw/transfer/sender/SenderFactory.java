package com.xchushi.fw.transfer.sender;

public class SenderFactory {

    public static Sender getSender(Class<?> cls){
        return HttpAndHttpsSender.getSender(cls);
    }
    
}
