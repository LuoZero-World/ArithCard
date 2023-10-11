package com.llq.server.handler;

import com.llq.entity.CardTable;
import com.llq.entity.Player;
import com.llq.message.req.GameStartReqMsg;
import com.llq.message.resp.GameStartRespMsg;
import com.llq.server.service.CardTableService;
import com.llq.server.service.ChannelBaseService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 李林麒
 * @date 2023/1/29 19:58
 * @Description 游戏开始请求处理
 */
@ChannelHandler.Sharable
public class GameStartRespHandler extends SimpleChannelInboundHandler<GameStartReqMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameStartReqMsg gameStartReqMsg) throws Exception {
        CardTable cardtable = CardTableService.INSTANCE.getTableBy(gameStartReqMsg.getTableName());
        cardtable.load();
        GameStartRespMsg message = new GameStartRespMsg();
        //传输信息
        Player master = cardtable.getMaster(), challenge = cardtable.getChallenge();
        message.setMasterInfo(master.getPlayerInfo());
        message.setChallengeInfo(challenge.getPlayerInfo());
        ctx.writeAndFlush(message);
        ChannelBaseService.INSTANCE.getChannel(challenge.getPlayerInfo().getNickname()).writeAndFlush(message);
    }
}
