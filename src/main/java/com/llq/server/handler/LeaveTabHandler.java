package com.llq.server.handler;

import com.llq.entity.CardTable;
import com.llq.message.req.LeaveTabReqMsg;
import com.llq.message.resp.ChallengeLeaveMsg;
import com.llq.server.service.CardTableService;
import com.llq.server.service.ChannelBaseService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * @author 李林麒
 * @date 2023/1/26 21:59
 * @Description 对局离开处理
 */
@ChannelHandler.Sharable
public class LeaveTabHandler extends SimpleChannelInboundHandler<LeaveTabReqMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LeaveTabReqMsg leaveTabReqMsg) throws Exception {
        if(leaveTabReqMsg.isMaster()){
            CardTableService.INSTANCE.deleteCardTable(leaveTabReqMsg.getTableName());
        } else{     //挑战者离开
            CardTable cardtable = CardTableService.INSTANCE.getTableBy(leaveTabReqMsg.getTableName());
            cardtable.setChallenge(null);
            ChannelBaseService.INSTANCE.getChannel(cardtable.getMaster().getPlayerInfo().getNickname())
                    .writeAndFlush(new ChallengeLeaveMsg());
        }
        ReferenceCountUtil.release(leaveTabReqMsg);
    }
}
