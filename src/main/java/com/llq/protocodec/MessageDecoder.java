package com.llq.protocodec;

import com.llq.message.IllegalPacketException;
import com.llq.message.Message;
import com.llq.message.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 李林麒
 * @date 2023/1/14 22:30
 * @Description 解码器
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        int magicNum = in.readInt();
        if(magicNum != 0x01020304) {
            log.debug("协议识别出错");
            throw new IllegalPacketException("协议识别错误");
        }
        int type = in.readInt();
        int high = in.readByte() & 0xFF;
        int low = in.readByte() & 0xFF;
        int length = ((high << 8) | low);

        int serializerAlg = in.readByte();
        in.readByte();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

//        不同序列化用不同方法
        Serializer.Algorithm alg = Serializer.Algorithm.values()[serializerAlg];
        Message msg = alg.deserializer(MessageType.getMessageClassBy(type), bytes);

        list.add(msg);

    }
}
