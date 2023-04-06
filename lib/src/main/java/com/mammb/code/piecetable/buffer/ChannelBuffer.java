/*
 * Copyright 2019-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mammb.code.piecetable.buffer;

import com.mammb.code.piecetable.array.ChannelArray;
import com.mammb.code.piecetable.array.IntArray;
import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

/**
 * ByteChannel buffer.
 * @author Naotsugu Kobayashi
 */
public class ChannelBuffer implements Buffer, Closeable {

    /** The default size of pitch. */
    private static final short DEFAULT_PITCH = 256;

    /** The channel array. */
    private final ChannelArray ch;

    /** The length of code point counts. */
    private final int length;

    /** The size of pitch. */
    private final short pilePitch;

    /** The piles. */
    private final int[] piles;

    /** The lru cache. */
    private final LruCache cache;


    /**
     * Constructor.
     * @param ch the ChannelArray
     * @param length the length of code point counts
     * @param pilePitch the size of pitch
     * @param piles the piles
     */
    private ChannelBuffer(ChannelArray ch, int length, short pilePitch, int[] piles) {
        this.ch = ch;
        this.length = length;
        this.pilePitch = pilePitch;
        this.piles = piles;
        this.cache = LruCache.of();
    }


    /**
     * Create a new Buffer.
     * @param channel the byte channel
     * @return a created buffer
     */
    public static Buffer of(SeekableByteChannel channel) {
        return of(channel, DEFAULT_PITCH);
    }


    /**
     * Create a new Buffer.
     * @param channel the byte channel
     * @param pitch the pitch
     * @return a created buffer
     */
    static Buffer of(SeekableByteChannel channel, short pitch) {
        var ch = ChannelArray.of(channel);
        var charCount = 0;
        var piles = IntArray.of();
        for (int i = 0; i < ch.length(); i++) {
            if (charCount++ % pitch == 0) {
                piles.add(i);
            }
            i += (Utf8.followsCount(ch.get(i)) - 1);
        }
        return new ChannelBuffer(ch, charCount, pitch, piles.get());
    }


    @Override
    public int length() {
        return length;
    }


    @Override
    public byte[] charAt(int index) {
        int rawIndex = asIndex(index);
        return Utf8.asCharBytes(ch.get(rawIndex, Math.min(rawIndex + 4, ch.length())), 0);
    }


    @Override
    public byte[] bytes(int rawStart, int rawEnd) {
        return ch.get(rawStart, rawEnd);
    }


    @Override
    public byte[] bytes() {
        return ch.get(0, ch.length());
    }


    @Override
    public Buffer subBuffer(int start, int end) {
        return ReadBuffer.of(ch.get(asIndex(start), asIndex(end)));
    }


    @Override
    public int asIndex(int index) {
        if (index == length) {
            return ch.length();
        }
        var cached = cache.get(index);
        if (cached.isPresent()) {
            return cached.get();
        }
        int i = piles[index / pilePitch];
        int remaining = index % pilePitch;
        for (; remaining > 0 && i < ch.length(); remaining--, i++) {
            i += (Utf8.followsCount(ch.get(i)) - 1);
        }
        cache.put(index, i);
        return i;
    }


    @Override
    public void close() throws IOException {
        ch.close();
    }

}
