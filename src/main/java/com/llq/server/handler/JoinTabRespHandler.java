package com.llq.server.handler;

import com.llq.entity.CardTable;
import com.llq.entity.Player;
import com.llq.message.req.JoinTabReqMsg;
import com.llq.message.resp.JoinTabRespMsg;
import com.llq.server.service.CardTableService;
import com.llq.server.service.ChannelBaseService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * @author 李林麒
 * @date 2023/1/27 12:02
 * @Description 加入对局处理
 */
@ChannelHandler.Sharable
public class JoinTabRespHandler extends SimpleChannelInboundHandler<JoinTabReqMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinTabReqMsg joinTabReqMsg) throws Exception {
        String tableName = joinTabReqMsg.getTableName(), challengeName = joinTabReqMsg.getChallengeName();
        if(CardTableService.INSTANCE.joinTable(tableName, challengeName)){
            //获取主客信息
            CardTable cardTable = CardTableService.INSTANCE.getTableBy(tableName);
            Player master = cardTable.getMaster(), challenge = cardTable.getChallenge();
            //若成功，给主客都发送消息
            JoinTabRespMsg message = new JoinTabRespMsg(true, "");
            Channel masChannel = ChannelBaseService.INSTANCE.getChannel(master.getPlayerInfo().getNickname());
            //TODO 若当前成功的原因是有人加入观战

            //加入桌主的信息
            message.setMasterInfo(master.getPlayerInfo());
            message.setChallengeInfo(challenge.getPlayerInfo());
            ctx.writeAndFlush(message);
            masChannel.writeAndFlush(message);
        } else{
            ctx.writeAndFlush(new JoinTabRespMsg(false, "原因有很多"));
        }
        ReferenceCountUtil.release(joinTabReqMsg);
    }
}
