package com.llq.server;

import com.llq.message.MessageType;
import com.llq.protocodec.MessageDecoder;
import com.llq.protocodec.MessageEncoder;
import com.llq.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @author 李林麒
 * @date 2023/1/14 19:46
 * @Description 卡牌游戏服务端
 */
public class CardServer {

    public void start(){
        try {
            connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        MessageDecoder DECODER = new MessageDecoder();
        MessageEncoder ENCODER = new MessageEncoder();
        LogoutHandler LOGOUT = new LogoutHandler();
        LoginRespHandler LOGIN = new LoginRespHandler();
        TableRespHandler TABLE_HANDLER = new TableRespHandler();
        CreateTabRespHandler TABLE_CREATE_HANDLER = new CreateTabRespHandler();
        LeaveTabHandler TABLE_LEAVE_HANDLER = new LeaveTabHandler();
        JoinTabRespHandler TABLE_JOIN_HANDLER = new JoinTabRespHandler();
        GameStartRespHandler GAME_START_HANDLER = new GameStartRespHandler();
        RoundStartRespMsgHandler ROUND_START_HANDLER = new RoundStartRespMsgHandler();
        RoundEndRespHandler ROUND_END_HANDLER = new RoundEndRespHandler();
        GetCardRespHandler CARD_GET_HANDLER = new GetCardRespHandler();
        PingHandler PING_HANDLER = new PingHandler();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)      //实例化Channel
                    .localAddress(new InetSocketAddress(18000))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(2048, 8, 2,2,0));
                            socketChannel.pipeline().addLast(DECODER);
                            socketChannel.pipeline().addLast(ENCODER);
                            socketChannel.pipeline().addLast(LOGIN);
                            socketChannel.pipeline().addLast(PING_HANDLER);
                            socketChannel.pipeline().addLast(TABLE_HANDLER);
                            socketChannel.pipeline().addLast(TABLE_CREATE_HANDLER);
                            socketChannel.pipeline().addLast(TABLE_LEAVE_HANDLER);
                            socketChannel.pipeline().addLast(TABLE_JOIN_HANDLER);
                            socketChannel.pipeline().addLast(GAME_START_HANDLER);
                            socketChannel.pipeline().addLast(ROUND_START_HANDLER);
                            socketChannel.pipeline().addLast(CARD_GET_HANDLER);
                            socketChannel.pipeline().addLast(ROUND_END_HANDLER);
                            socketChannel.pipeline().addLast(LOGOUT);
                        }
                    });

            //绑定端口
            Channel channel = b.bind().sync().channel();
            System.out.println("在" + channel.localAddress() + "上开启监听");
            channel.closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {

        MessageType.initPackets();
        CardServer server = new CardServer();
        server.start();
    }
}
