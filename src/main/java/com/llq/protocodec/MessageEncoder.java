package com.llq.protocodec;

import com.llq.message.Message;
import com.llq.utility.PropertiesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 李林麒
 * @date 2023/1/14 22:29
 * @Description 编码器
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageEncoder extends MessageToMessageEncoder<Message> {

    private static final int MAGIC_NUMBER =  0x01020304;

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> list) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        //魔数-4B
        buffer.writeInt(MAGIC_NUMBER);
        //消息类型-4B
        buffer.writeInt(message.getMessageType().getType());

        Serializer.Algorithm alg = Serializer.Algorithm.valueOf(PropertiesUtil.getStr("SERIALIZER_ALGORITHM"));
        byte[] bytes = alg.serializer(message);

        int length = bytes.length;
        byte low = (byte)(length & 0xFF);
        byte high = (byte)((length>>8 & 0xFF));

        //长度-2B
        buffer.writeByte(high);
        buffer.writeByte(low);
        //序列化方式-1B
        buffer.writeByte((byte)alg.ordinal());
        //填充
        buffer.writeByte(0xFF);
        buffer.writeBytes(bytes);
        list.add(buffer);
    }
}
