package syamwu.xchushi.fw.arithmetic.loadbalanc.code;

import syamwu.xchushi.fw.arithmetic.loadbalanc.exception.LoadBalanceException;

public class HashUniqueCode implements UniqueCode {

    private Object obj;

    public HashUniqueCode(Object obj) {
        if (obj == null)
            throw new LoadBalanceException("obj can't be null");
        this.obj = obj;
    }

    @Override
    public int get() {
        return obj.hashCode();
    }

}
