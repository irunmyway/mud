package com.eztv.mud.command.commands.store.auction;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.cache.manager.ItemManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.cache.manager.AuctionManager.getAuction;
import static com.eztv.mud.cache.manager.AuctionManager.remove;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-10 9:35
 * 功能: 取消寄卖【通过寄卖id】
 **/
public class JmCancel extends BaseCommand {

    public JmCancel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        MsgMap msgMap = new MsgMap();
        msgMap = msgMap.到消息(getMsg().getMsg());
        int auctionId =0;
        if(msgMap.取值("auction")!=null)
            try{
                auctionId = Integer.parseInt(msgMap.取值("auction").toString());
            }catch(Exception e){e.printStackTrace();}
        Auction auction =  getAuction(auctionId);
        if (auction!=null){
            //判断该寄卖是不是自己的
            if(!auction.getRole().equals(getPlayer().getAccount()))return;
            //数据库里删除该寄卖信息 缓存里清空该寄卖
            remove(auction);
            //玩家背包里增加该物品
            switch (auction.getItemType()){
                case normal:
                    getPlayer().getPlayerData().getBag().giveItem(auction.getItem(),auction.getNum());
                    break;
                case skill:
                    getPlayer().getPlayerData().getBag().giveSkill(auction.getItem(),auction.getNum());
                    break;
                case yb:
                    getPlayer().getPlayerData().getBag().changeYbMoney(auction.getNum());
                    break;
            }
            //发送取消寄卖的消息
            Item item=null;
            switch (auction.getItemType()){
                case normal:
                    item = ItemManager.getItemById(auction.getItem()+"");
                    break;
                case skill:
                    item = ItemManager.getSkillById(auction.getItem()+"");
                    break;
            }
            //显示撤回元宝寄卖
            if(auction.getItemType()== Enum.itemType.yb){
                if(item==null)return;
                String sendStr = getPropByFile("store","auction_sell_cancel",
                        getPropByFile("bag", "bag_yb")
                );
                Chat chat = Chat.system(sendStr);
                GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
                return;
            }
            //显示撤回商品寄卖
            if(item==null)return;
            String sendStr = getPropByFile("store","auction_sell_cancel",
                    item.getName()
            );
            Chat chat = Chat.system(sendStr);
            GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
            return;
        }
    }

}
