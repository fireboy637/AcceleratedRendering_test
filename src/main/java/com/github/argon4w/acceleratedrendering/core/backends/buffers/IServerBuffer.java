package com.github.argon4w.acceleratedrendering.core.backends.buffers;

import java.nio.ByteBuffer;

public interface IServerBuffer {

    int getBufferHandle();

    void delete();

    void bind(int target);

    void data(ByteBuffer data);

    void bindBase(int target, int index);

    void bindRange(int target, int index, long offset, long size);
}
