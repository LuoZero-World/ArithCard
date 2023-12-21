package com.llq.server.handler;

import com.llq.entity.CardTable;
import com.llq.entity.PlayerInfo;
import com.llq.message.req.RoundEndReqMsg;
import com.llq.message.resp.RoundEndRespMsg;
import com.llq.message.resp.RoundStartRespMsg;
import com.llq.message.resp.StateChangeMsg;
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

        Channel masterChannel = ChannelBaseService.INSTANCE.getChannel(masterInfo.getNickname());
        Channel challengeChannel = ChannelBaseService.INSTANCE.getChannel(challengeInfo.getNickname());

        //改变状态
        if(roundEndReqMsg.isMaster()){
            cardTable.getMaster().setPlayerInfo(playerInfo);
        } else{
            cardTable.getChallenge().setPlayerInfo(playerInfo);
        }

        //当前一轮结束，判断这一轮结束后游戏是否结束
        if((cardTable.getIdx()&1) == 1){
            isGameOver = cardTable.roundEndBattle();
        }

        //如果游戏结束，通知所有，然后直接结束
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

            cardTable.turnPlayer();

            //同步C-S信息
            //如果是master发来的回合结束，那么通知guest开始
            if(roundEndReqMsg.isMaster()){
                challengeChannel.writeAndFlush(
                        new StateChangeMsg(challengeInfo.getHP(), playerInfo.getHP(), challengeInfo.isDefend(), challengeInfo.isAttack(), playerInfo.isDefend(), playerInfo.isAttack())
                );
                challengeChannel.writeAndFlush(new RoundStartRespMsg(true, ""));

            } else {     //否则是guest发来的回合结束，然后通知master这一轮结束，让其开始下一轮
                PlayerInfo newMasterInfo = cardTable.getMaster().getPlayerInfo(), newChallengeInfo = cardTable.getChallenge().getPlayerInfo();

                //调整对战双方战斗消息
                masterChannel.writeAndFlush(
                        new StateChangeMsg(newMasterInfo.getHP(), newChallengeInfo.getHP(), newMasterInfo.isDefend(), newMasterInfo.isAttack(), newChallengeInfo.isDefend(), newChallengeInfo.isAttack())
                );
                challengeChannel.writeAndFlush(
                        new StateChangeMsg(newChallengeInfo.getHP(), newMasterInfo.getHP(), newChallengeInfo.isDefend(), newChallengeInfo.isAttack(), newMasterInfo.isDefend(), newMasterInfo.isAttack())
                );

                masterChannel.writeAndFlush(new RoundEndRespMsg(false));
            }
        }
        ReferenceCountUtil.release(roundEndReqMsg);
    }
}
