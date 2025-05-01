package cn.xiaym.unifiedconf;

public class EnumReference<T extends Enum<T>> {
    private T value;

    public EnumReference(T defaultValue) {
        this.value = defaultValue;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
