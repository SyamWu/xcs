package syamwu.xchushi.fw.common;

public final class Asset {

    public final static void isNull(Object obj, String message) {
        if (obj != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public final static void isNull(Object obj) {
        isNull(obj, "Assert fail, obj is Null");
    }

    public final static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public final static void notNull(Object obj) {
        notNull(obj, "Assert fail, obj can't be Null");
    }

    public final static void isTrue(boolean bl, String message) {
        if (!bl) {
            throw new IllegalArgumentException(message);
        }
    }

    public final static void isTrue(boolean bl) {
        isTrue(bl, "Assert fail, bl can't be true");
    }

    public static void isFalse(boolean bl, String message) {
        if (bl) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isFalse(boolean bl) {
        isFalse(bl, "Assert fail, bl is true");
    }
    
    public static void assetFail(String message) {
        throw new IllegalArgumentException(message);
    }
    
    public static void assetFail(Throwable e) {
        throw new IllegalArgumentException(e);
    }
    
    public static void throwRuntimeException(Throwable e) {
        throw new RuntimeException(e);
    }
    
    public final static void isAssignableFrom(Class<?> parentClass, Class<?> sonClass) {
        isTrue(parentClass.isAssignableFrom(sonClass), parentClass.getName() + " isn't assignable from " + sonClass.getName());
    }
    
    public final static <T> T getNull(){
        return null;
    }

}
