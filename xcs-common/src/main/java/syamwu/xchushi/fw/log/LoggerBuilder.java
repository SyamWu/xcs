package syamwu.xchushi.fw.log;

public interface LoggerBuilder<T extends XcsLogger> {

    T buildLogger(Class<?> cls);
    
}
