package com.mammb.code.piecetable.piece;

import com.mammb.code.piecetable.buffer.Buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;

/**
 * A piece consists of three param.
 * @param target which buffer
 * @param bufIndex start index in the buffer
 * @param length length in the buffer
 *
 * @author Naotsugu Kobayashi
 */
public record Piece(Buffer target, int bufIndex, int length) {

    public record Pair(Piece left, Piece right) { }

    public Pair split(int offset) {
        if (offset >= length) {
            throw new RuntimeException("Illegal offset value. offset[%s], length[%s]"
                    .formatted(offset, length));
        }
        return new Pair(
                new Piece(target, bufIndex, offset),
                new Piece(target, bufIndex + offset, length - offset));
    }

    public Optional<Piece> marge(Piece other) {
        if (target == other.target) {
            if (end() == other.bufIndex) {
                return Optional.of(new Piece(target, bufIndex, length + other.length));
            }
            if (other.end() == bufIndex) {
                return Optional.of(new Piece(target, other.bufIndex, length + other.length));
            }
        }
        return Optional.empty();
    }

    void writeTo(WritableByteChannel channel, ByteBuffer buf) throws IOException {
        target.write(channel, buf, bufIndex, length);
    }

    public int end() {
        return bufIndex + length;
    }

}
