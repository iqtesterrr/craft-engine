package net.momirealms.craftengine.bukkit.plugin.network.payload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.momirealms.craftengine.core.util.MCUtils;
import net.momirealms.sparrow.nbt.CompoundTag;
import net.momirealms.sparrow.nbt.NBT;
import net.momirealms.sparrow.nbt.Tag;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * 随便写了点方便后面重构和客户端通讯
 */
public interface NetWorkCodecs {

    NetWorkCodec<Boolean> BOOLEAN = new NetWorkCodec<>() {
        @Override
        public Boolean decode(ByteBuf in) {
            return in.readBoolean();
        }

        @Override
        public void encode(ByteBuf out, Boolean value) {
            out.writeBoolean(value);
        }
    };

    NetWorkCodec<Byte> BYTE = new NetWorkCodec<>() {
        @Override
        public Byte decode(ByteBuf in) {
            return in.readByte();
        }

        @Override
        public void encode(ByteBuf out, Byte value) {
            out.writeByte(value);
        }
    };

    NetWorkCodec<Float> ROTATION_BYTE = BYTE.map(MCUtils::unpackDegrees, MCUtils::packDegrees);

    NetWorkCodec<Short> SHORT = new NetWorkCodec<>() {
        @Override
        public Short decode(ByteBuf in) {
            return in.readShort();
        }

        @Override
        public void encode(ByteBuf out, Short value) {
            out.writeShort(value);
        }
    };

    NetWorkCodec<Integer> UNSIGNED_SHORT = new NetWorkCodec<>() {
        @Override
        public Integer decode(ByteBuf in) {
            return in.readUnsignedShort();
        }

        @Override
        public void encode(ByteBuf out, Integer value) {
            out.writeShort(value);
        }
    };

    NetWorkCodec<Integer> INTEGER = new NetWorkCodec<>() {
        @Override
        public Integer decode(ByteBuf in) {
            return in.readInt();
        }

        @Override
        public void encode(ByteBuf out, Integer value) {
            out.writeInt(value);
        }
    };

    NetWorkCodec<Integer> VAR_INTEGER = new NetWorkCodec<>() {
        @Override
        public Integer decode(ByteBuf in) {
            int result = 0;
            int bytesRead = 0;
            byte currentByte;
            do {
                currentByte = in.readByte();
                result |= (currentByte & 127) << bytesRead++ * 7;
                if (bytesRead > 5) {
                    throw new RuntimeException("VarInt too big");
                }
            } while ((currentByte & 128) == 128);
            return result;
        }

        @Override
        public void encode(ByteBuf out, Integer value) {
            while ((value & -128) != 0) {
                out.writeByte(value & 127 | 128);
                value >>>= 7;
            }
            out.writeByte(value);
        }
    };

    NetWorkCodec<OptionalInt> OPTIONAL_VAR_INTEGER = VAR_INTEGER.map(
            integer -> integer == 0 ? OptionalInt.empty() : OptionalInt.of(integer - 1),
            optionalInt -> optionalInt.isPresent() ? optionalInt.getAsInt() + 1 : 0
    );

    NetWorkCodec<Long> LONG = new NetWorkCodec<>() {
        @Override
        public Long decode(ByteBuf in) {
            return in.readLong();
        }

        @Override
        public void encode(ByteBuf out, Long value) {
            out.writeLong(value);
        }
    };

    NetWorkCodec<Long> VAR_LONG = new NetWorkCodec<>() {
        @Override
        public Long decode(ByteBuf in) {
            long result = 0L;
            int bytesRead = 0;
            byte currentByte;
            do {
                currentByte = in.readByte();
                result |= (long)(currentByte & 127) << bytesRead++ * 7;
                if (bytesRead > 10) {
                    throw new RuntimeException("VarLong too big");
                }
            } while ((currentByte & 128) == 128);
            return result;
        }

        @Override
        public void encode(ByteBuf out, Long value) {
            while ((value & -128L) != 0L) {
                out.writeByte((int)(value & 127L) | 128);
                value >>>= 7;
            }
            out.writeByte(value.intValue());
        }
    };

    NetWorkCodec<Float> FLOAT = new NetWorkCodec<>() {
        @Override
        public Float decode(ByteBuf in) {
            return in.readFloat();
        }

        @Override
        public void encode(ByteBuf out, Float value) {
            out.writeFloat(value);
        }
    };

    NetWorkCodec<Double> DOUBLE = new NetWorkCodec<>() {
        @Override
        public Double decode(ByteBuf in) {
            return in.readDouble();
        }

        @Override
        public void encode(ByteBuf out, Double value) {
            out.writeDouble(value);
        }
    };

    NetWorkCodec<byte[]> BYTE_ARRAY = new NetWorkCodec<>() {
        @Override
        public byte[] decode(ByteBuf in) {
            int maxSize = in.readableBytes();
            int size = VAR_INTEGER.decode(in);
            if (size > maxSize) {
                throw new DecoderException("ByteArray with size " + size + " is bigger than allowed " + maxSize);
            } else {
                byte[] bytes = new byte[size];
                in.readBytes(bytes);
                return bytes;
            }
        }

        @Override
        public void encode(ByteBuf out, byte[] value) {
            VAR_INTEGER.encode(out, value.length);
            out.writeBytes(value);
        }
    };

    NetWorkCodec<long[]> LONG_ARRAY = new NetWorkCodec<>() {
        @Override
        public long[] decode(ByteBuf in) {
            int arrayLength = VAR_INTEGER.decode(in);
            int maxPossibleElements = in.readableBytes() / 8;
            if (arrayLength > maxPossibleElements) {
                throw new DecoderException("LongArray with size " + arrayLength + " is bigger than allowed " + maxPossibleElements);
            } else {
                long[] longArray = new long[arrayLength];
                for (int i = 0; i < longArray.length; i++) {
                    longArray[i] = in.readLong();
                }
                return longArray;
            }
        }

        @Override
        public void encode(ByteBuf out, long[] value) {
            VAR_INTEGER.encode(out, value.length);
            for (long element : value) {
                out.writeLong(element);
            }
        }
    };

    NetWorkCodec<String> STRING_UTF8 = new NetWorkCodec<>() {
        private static final int MAX_STRING_LENGTH = 32767;

        @Override
        public String decode(ByteBuf in) {
            int maxEncodedBytes = ByteBufUtil.utf8MaxBytes(MAX_STRING_LENGTH);
            int encodedLength = VAR_INTEGER.decode(in);
            if (encodedLength > maxEncodedBytes) {
                throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + encodedLength + " > " + maxEncodedBytes + ")");
            } else if (encodedLength < 0) {
                throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
            } else {
                int availableBytes = in.readableBytes();
                if (encodedLength > availableBytes) {
                    throw new DecoderException("Not enough bytes in buffer, expected " + encodedLength + ", but got " + availableBytes);
                } else {
                    String decodedString = in.toString(in.readerIndex(), encodedLength, StandardCharsets.UTF_8);
                    in.readerIndex(in.readerIndex() + encodedLength);
                    if (decodedString.length() > MAX_STRING_LENGTH) {
                        throw new DecoderException("The received string length is longer than maximum allowed (" + decodedString.length() + " > " + MAX_STRING_LENGTH + ")");
                    } else {
                        return decodedString;
                    }
                }
            }
        }

        @Override
        public void encode(ByteBuf out, String value) {
            if (value.length() > MAX_STRING_LENGTH) {
                throw new EncoderException("String too big (was " + value.length() + " characters, max " + MAX_STRING_LENGTH + ")");
            } else {
                int maxPossibleBytes = ByteBufUtil.utf8MaxBytes(value);
                ByteBuf tempBuffer = out.alloc().buffer(maxPossibleBytes);
                try {
                    int actualEncodedBytes = ByteBufUtil.writeUtf8(tempBuffer, value);
                    int maxAllowedBytes = ByteBufUtil.utf8MaxBytes(MAX_STRING_LENGTH);
                    if (actualEncodedBytes > maxAllowedBytes) {
                        throw new EncoderException("String too big (was " + actualEncodedBytes + " bytes encoded, max " + maxAllowedBytes + ")");
                    }
                    VAR_INTEGER.encode(out, actualEncodedBytes);
                    out.writeBytes(tempBuffer);
                } finally {
                    tempBuffer.release();
                }
            }
        }
    };

    NetWorkCodec<Tag> TAG = new NetWorkCodec<>() {
        @Override
        public Tag decode(ByteBuf in) {
            int initialIndex = in.readerIndex();
            byte marker = in.readByte();
            if (marker == 0) {
                return null;
            } else {
                in.readerIndex(initialIndex);
                try {
                    return NBT.readUnnamedTag(new ByteBufInputStream(in), false);
                } catch (IOException e) {
                    throw new EncoderException("Failed to read NBT compound: " + e.getMessage(), e);
                }
            }
        }

        @Override
        public void encode(ByteBuf out, Tag value) {
            if (value == null) {
                out.writeByte(0);
            } else {
                try {
                    NBT.writeUnnamedTag(value, new ByteBufOutputStream(out), false);
                } catch (IOException e) {
                    throw new EncoderException("Failed to write NBT compound: " + e.getMessage(), e);
                }
            }
        }
    };

    NetWorkCodec<CompoundTag> COMPOUND_TAG = TAG.map(tag -> {
        if (tag instanceof CompoundTag compoundTag) {
            return compoundTag;
        } else {
            throw new DecoderException("Not a compound tag: " + tag);
        }
    }, tag -> tag);

    NetWorkCodec<Optional<CompoundTag>> OPTIONAL_COMPOUND_TAG = new NetWorkCodec<>() {
        @Override
        public Optional<CompoundTag> decode(ByteBuf in) {
            int initialIndex = in.readerIndex();
            byte marker = in.readByte();
            if (marker == 0) {
                return Optional.empty();
            } else {
                in.readerIndex(initialIndex);
                try {
                    if (NBT.readUnnamedTag(new ByteBufInputStream(in), false) instanceof CompoundTag compoundTag) {
                        return Optional.of(compoundTag);
                    }
                } catch (IOException e) {
                    throw new EncoderException("Failed to read NBT compound: " + e.getMessage(), e);
                }
            }
            return Optional.empty();
        }

        @Override
        public void encode(ByteBuf out, Optional<CompoundTag> value) {
            CompoundTag compound = value.orElse(null);
            if (compound == null) {
                out.writeByte(0);
            } else {
                try {
                    NBT.writeUnnamedTag(compound, new ByteBufOutputStream(out), false);
                } catch (IOException e) {
                    throw new EncoderException("Failed to write NBT compound: " + e.getMessage(), e);
                }
            }
        }
    };

    NetWorkCodec<Vector3f> VECTOR3F = new NetWorkCodec<>() {
        @Override
        public Vector3f decode(ByteBuf in) {
            return new Vector3f(in.readFloat(), in.readFloat(), in.readFloat());
        }

        @Override
        public void encode(ByteBuf out, Vector3f value) {
            out.writeFloat(value.x());
            out.writeFloat(value.y());
            out.writeFloat(value.z());
        }
    };

    NetWorkCodec<Quaternionf> QUATERNIONF = new NetWorkCodec<>() {
        @Override
        public Quaternionf decode(ByteBuf in) {
            return new Quaternionf(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
        }

        @Override
        public void encode(ByteBuf out, Quaternionf value) {
            out.writeFloat(value.x());
            out.writeFloat(value.y());
            out.writeFloat(value.z());
            out.writeFloat(value.w());
        }
    };

    NetWorkCodec<Integer> CONTAINER_ID = VAR_INTEGER;

    NetWorkCodec<Integer> RGB_COLOR = new NetWorkCodec<>() {
        @Override
        public Integer decode(ByteBuf in) {
            return 255 << 24 | in.readByte() & 0xFF << 16 | in.readByte() & 0xFF << 8 | in.readByte() & 0xFF;
        }

        @Override
        public void encode(ByteBuf out, Integer value) {
            out.writeByte(value >> 16 & 0xFF);
            out.writeByte(value >> 8 & 0xFF);
            out.writeByte(value & 0xFF);
        }
    };
}
