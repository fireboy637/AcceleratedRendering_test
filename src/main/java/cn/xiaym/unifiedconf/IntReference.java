package cn.xiaym.unifiedconf;

public class IntReference {
    private int value;

    public IntReference(int defaultValue) {
        this.value = defaultValue;
    }

    public void set(int value) {
        this.value = value;
    }

    public int getAsInt() {
        return value;
    }
}
