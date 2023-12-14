package com.llq.client;

import com.llq.protocodec.MessageDecoder;
import com.llq.protocodec.MessageEncoder;
import com.llq.utility.PropertiesUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author 李林麒
 * @date 2023/1/14 19:22
 * @Description 卡牌游戏客户端
 */

@Slf4j
public class CardClient {

    public void start(){
        try{
            connect(PropertiesUtil.getStr("REMOTE_SERVER_IP"),
                    PropertiesUtil.getInt("REMOTE_SERVER_PORT"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void connect(String host, int port){
        EventLoopGroup group = new NioEventLoopGroup();
        MessageDecoder DECODER = new MessageDecoder();
        MessageEncoder ENCODER = new MessageEncoder();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group)
                    .channel(NioSocketChannel.class)      //工厂方法实例化Channel
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {  //通道初始化配置
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(2048, 8, 2,2,0));
                            socketChannel.pipeline().addLast(DECODER);
                            socketChannel.pipeline().addLast(ENCODER);
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture future = bs.connect();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
