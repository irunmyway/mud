package com.eztv.mud.command.commands.store.auction;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Auction;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.cache.AuctionCache;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDebug;

import java.util.List;

import static com.eztv.mud.GameUtil.*;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-10 22:23
 * 功能: 寄卖收益
 **/
public class JmReward extends BaseCommand {

    public JmReward(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        List<Auction> auctions = AuctionCache.getAuctionsByRole(getPlayer().getAccount());
        int num=0;
        long yb=0;
        long money=0;
        long tmp=0;
        for (Auction auction:auctions){
            if(auction.getNum()==0){//出售完了
                AuctionCache.remove(auction);
            }
            tmp = auction.getTotal()-auction.getNum();
            if(tmp<0){//奇怪了
                AuctionCache.remove(auction);
                continue;
            }
            switch (auction.getCurrency()){
                case money:
                    money+=auction.getPrice()*tmp;
                    break;
                case yb:
                    yb+=auction.getPrice()*tmp;
                    break;
            }
            if(auction.getState()==1)num++;
            auction.setTotal(auction.getNum());
        }
        AuctionCache.replace(auctions);
        getPlayer().getPlayerData().getBag().changeMoney(money);
        getPlayer().getPlayerData().getBag().changeMoney(yb);
        //发送收益信息
        String sendStr = getPropByFile("store","auction_reward",
                num,
                money,getPropByFile("bag","bag_money"),
                yb,getPropByFile("bag","bag_yb")
                );
        Chat chat = Chat.system(sendStr);
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));


    }



}
