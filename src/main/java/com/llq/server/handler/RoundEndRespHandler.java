package com.llq.server.handler;

import com.llq.entity.CardTable;
import com.llq.entity.PlayerInfo;
import com.llq.message.req.RoundEndReqMsg;
import com.llq.message.resp.RoundEndRespMsg;
import com.llq.message.resp.RoundStartRespMsg;
import com.llq.server.service.CardTableService;
import com.llq.server.service.ChannelBaseService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 李林麒
 * @date 2023/2/22 18:49
 * @Description 回合结束处理
 */
@ChannelHandler.Sharable
public class RoundEndRespHandler extends SimpleChannelInboundHandler<RoundEndReqMsg>{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RoundEndReqMsg roundEndReqMsg) throws Exception {
        CardTable cardTable = CardTableService.INSTANCE.getTableBy(roundEndReqMsg.getTableName());
        if(cardTable.isEmpty()) return;

        PlayerInfo playerInfo = roundEndReqMsg.getPlayerInfo();
        final PlayerInfo masterInfo = cardTable.getMaster().getPlayerInfo(), challengeInfo = cardTable.getChallenge().getPlayerInfo();
        boolean isGameOver = false;

        //表示当前一轮结束
        if((cardTable.getIdx()&1) == 1){
            isGameOver = cardTable.roundEndBattle();
        }

        //如果结束，通知所有，然后直接结束
        if(isGameOver){
            Set<String> MContainsPlayer = new HashSet<>(cardTable.getMembers()){{
                add(masterInfo.getNickname());
                add(challengeInfo.getNickname());
            }};
            List<Channel> channelList = ChannelBaseService.INSTANCE.getChannels(MContainsPlayer);
            channelList.forEach( c -> c.writeAndFlush(new RoundEndRespMsg(true)));
            //删除对战桌
            CardTableService.INSTANCE.deleteCardTable(cardTable.getTableName());
        } else{
            //同步信息
            if(roundEndReqMsg.isMaster()) cardTable.getMaster().setPlayerInfo(playerInfo);
            else cardTable.getChallenge().setPlayerInfo(playerInfo);

            //通知另外一方开始,这里的if中的逻辑我后来看也懵了,索性不要了
            //if((cardTable.getIdx()&1) == 0){
                String name = roundEndReqMsg.isMaster() ? challengeInfo.getNickname() : masterInfo.getNickname();
                Channel channel = ChannelBaseService.INSTANCE.getChannel(name);
                channel.writeAndFlush(new RoundStartRespMsg(true, ""));
            //}

            //这句最好在前面，以防止程序进入死等
            cardTable.turnPlayer();
            //通知请求结束者
            ctx.writeAndFlush(new RoundEndRespMsg(false));
        }

        ReferenceCountUtil.release(roundEndReqMsg);
    }
}
