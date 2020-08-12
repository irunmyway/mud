package com.eztv.mud.command.commands.store.auction;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.cache.manager.ItemManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.cache.manager.AuctionManager.getAuction;
import static com.eztv.mud.cache.manager.AuctionManager.replace;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-10 18:48
 * 功能: 寄卖购买
 **/
public class JmBuy extends BaseCommand {

    public JmBuy(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        MsgMap msgMap = new MsgMap();
        if (getMsg().getRole() != null) {//role 这里会放 购买的数量 数量不为空的时候执行购买
            msgMap = msgMap.到消息(getMsg().getMsg());
            if (msgMap.取值("cmd") != null) {
                switch (msgMap.取值("cmd").toString()) {
                    case "buy":
                        buy();
                        break;
                }
                return;
            }
        }
        buyHowMuch();
    }

    private void buy() {
        int num = 0;
        try {
            num = Integer.parseInt(getMsg().getRole());
        } catch (Exception e) {
            return;
        }
        MsgMap msgMap = new MsgMap();
        msgMap = msgMap.到消息(getMsg().getMsg());
        int auctionId = 0;
        if (msgMap.取值("auction") != null)
            try {
                auctionId = Integer.parseInt(msgMap.取值("auction").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        Auction auction = getAuction(auctionId);
        String itemName = "";
        String currencyName = "";
        if (auction.getRole().equals(getPlayer().getAccount())) return;
        if (auction != null) {
            long price = auction.getPrice();//扣费
            price = price > 0 ? -price : price;
            num = num < 0 ? -num : num;
            if (num > auction.getNum()) {
                //没有那么多
                String sendStr = getPropByFile("store", "auction_no_item");
                sendSystemChat(sendStr);
                return;
            }
            switch (auction.getCurrency()) {
                case yb:
                    if (getPlayer().getPlayerData().getBag().getYbMoney() < auction.getPrice() * num) {
                        //钱不够
                        String sendStr = getPropByFile("store", "auction_no_money");
                        sendSystemChat(sendStr);
                        return;
                    }
                    currencyName = getPropByFile("bag", "bag_yb");
                    break;
                case money:
                    if (getPlayer().getPlayerData().getBag().getMoney() < auction.getPrice() * num) {
                        //钱不够
                        String sendStr = getPropByFile("store", "auction_no_money");
                        sendSystemChat(sendStr);
                        return;
                    }
                    currencyName = getPropByFile("bag", "bag_money");
                    break;
            }
            auction.setState(1);//设置为已经寄卖 有人买了
            auction.setNum(auction.getNum() - num);
            switch (auction.getItemType()) {
                case normal:
                    getClient().getPlayer().getPlayerData().getBag().giveItem(auction.getItem(), num);
                    break;
                case skill:
                    getClient().getPlayer().getPlayerData().getBag().giveSkill(auction.getItem(), num);
                    break;
                case yb:
                    getClient().getPlayer().getPlayerData().getBag().changeYbMoney(num);
                    break;
            }
            getClient().getPlayer().getPlayerData().getBag().changeMoney(price * num);
            replace(auction);//更新缓存和数据库
        }
        Item item = null;
        switch (auction.getItemType()) {
            case yb:
                itemName = getPropByFile("bag", "bag_yb");
                break;
            case normal:
                item = ItemManager.getItemById(auction.getItem() + "");
                break;
            case skill:
                item = ItemManager.getSkillById(auction.getItem() + "");
                break;
        }
        if (item != null) itemName = item.getName();
        //你从寄卖商城购买了 %s 数量的 %s 花费了%s 的%s
        String sendStr = getPropByFile("store", "auction_buy",
                num,
                itemName,
                auction.getPrice() * num,
                currencyName
        );
        Chat chat = Chat.system(sendStr);
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));

    }

    //买多少呢？
    public void buyHowMuch() {
        getWinMsg().setDesc("购买多少?");
        MsgMap msgMap = new MsgMap();
        msgMap = msgMap.到消息(getMsg().getMsg());
        msgMap.添加("cmd", "buy");
        getChoice().add(Choice.createChoice(
                "确定",
                Enum.messageType.action, getMsg().getCmd(), msgMap.到文本(), msgMap.到文本())
                .setBgColor(Enum.color.red));
        getWinMsg().setChoice(getChoice());
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.input, getMsg().getCmd(), object2JsonStr(getWinMsg()), getMsg().getMsg(), null));
    }


    public void sendSystemChat(String sendStr) {
        Chat chat = Chat.system(sendStr);
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));

    }

}
