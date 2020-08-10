package com.eztv.mud.command.commands.store.auction;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.cache.AuctionCache;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.eztv.mud.GameUtil.*;

public class JmSell extends BaseCommand {
    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-08-08 17:37
     * 功能: 寄卖背包物品列表
     **/
    public JmSell(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        MsgMap msgMap = new MsgMap();
        if (getMsg().getMsg() != null) {
            msgMap = msgMap.到消息(getMsg().getMsg());
            if (msgMap.取值("cmd") != null) {
                switch (msgMap.取值("cmd").toString()) {
                    case "寄卖货币":
                        sellCurrency();
                        break;
                    case "preSell":
                        preSell();
                        break;
                    case "sellToMoney":
                        sellToCurrency(Enum.currency.money);
                        break;
                    case "sellToYb":
                        sellToCurrency(Enum.currency.yb);
                        break;
                    case "sellNum":
                        sellNum();
                        break;
                    case "doSell":
                        doSell();
                        break;

                }
                return;
            }
        }

        WinMessage winMsg = new WinMessage();
        winMsg.setCol(2);
        List<Choice> choice = new ArrayList<>();
        List<Item> items = getPlayer().getPlayerData().getBag().getItems();
        if (items.size() < 1) {
            getWinMsg().setDesc(getPropByFile("store", "auction_item_none"));
        }
        for (Item item : items) {
            msgMap.清空();
            msgMap.添加("itemType", item.getType());
            msgMap.添加("id", item.getId());
            msgMap.添加("cmd", "preSell");
            choice.add(Choice.createChoice(getPropByFile("bag", "bag_item",
                    item.getName(),
                    (item.getNum() < 2 ? "1" : item.getNum())),
                    Enum.messageType.action, getMsg().getCmd(), msgMap.到文本(), null, Enum.winAction.open));
        }
        winMsg.setChoice(choice);
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.pop, "", object2JsonStr(winMsg), getMsg().getMsg(), null));

    }

    private void sellCurrency() {
        MsgMap msgMap = new MsgMap();
        msgMap.清空();
        msgMap.添加("itemType", Enum.currency.yb);
        msgMap.添加("cmd", "sellToMoney");
        getMsg().setMsg(msgMap.到文本());
        sellToCurrency(Enum.currency.money);

    }


    public void preSell() {
        MsgMap msgMap = new MsgMap();
        msgMap = msgMap.到消息(getMsg().getMsg());
        getWinMsg().setDesc("选择出售类型");
        msgMap.添加("cmd", "sellToMoney");
        getChoice().add(Choice.createChoice(
                "以铜币出售",
                Enum.messageType.action, getMsg().getCmd(), msgMap.到文本(), null)
                .setBgColor(Enum.color.blue));
        msgMap.添加("cmd", "sellToYb");
        getChoice().add(Choice.createChoice(
                "以元宝出售",
                Enum.messageType.action, getMsg().getCmd(), msgMap.到文本(), null)
                .setBgColor(Enum.color.red));
        getWinMsg().setChoice(getChoice());
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.pop, "", object2JsonStr(getWinMsg()), getMsg().getMsg(), null));
    }

    public void sellToCurrency(Enum.currency currency) {
        getWinMsg().setDesc("单价");
        MsgMap msgMap = new MsgMap();
        msgMap = msgMap.到消息(getMsg().getMsg());
        msgMap.添加("cmd", "sellNum");
        msgMap.添加("currency", currency.toString());
        getChoice().add(Choice.createChoice(
                "确定",
                Enum.messageType.action, getMsg().getCmd(), msgMap.到文本(), msgMap.到文本())
                .setBgColor(Enum.color.red));
        getWinMsg().setChoice(getChoice());
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.input, getMsg().getCmd(), object2JsonStr(getWinMsg()), getMsg().getMsg(), null));
    }

    private void sellNum() {
        getWinMsg().setDesc("数量");
        MsgMap msgMap = new MsgMap();
        msgMap = msgMap.到消息(getMsg().getMsg());
        msgMap.添加("cmd", "doSell");
        msgMap.添加("price", getMsg().getRole());
        getChoice().add(Choice.createChoice(
                "确定",
                Enum.messageType.action, getMsg().getCmd(), msgMap.到文本(), msgMap.到文本(), Enum.winAction.closeAll)
                .setBgColor(Enum.color.red));
        getWinMsg().setChoice(getChoice());
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.input, getMsg().getCmd(), object2JsonStr(getWinMsg()), getMsg().getMsg(), null));
    }

    private void doSell() {
        MsgMap msgMap = new MsgMap();
        msgMap = msgMap.到消息(getMsg().getMsg());
        if (getMsg().getRole() == null || msgMap.取值("price") == null) return;
        long price = 0;
        int num = 0;
        try {
            price = Long.parseLong(msgMap.取值("price").toString());
            num = Integer.parseInt(getMsg().getRole());
            if (price < 1) throw new Exception();
        } catch (Exception e) {//发送信息
            String sendStr = getPropByFile("store", "auction_error");
            Chat chat = Chat.system(sendStr);
            GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
            return;
        }
        if (price < 1 || num < 1) return;
        String currencyAlias = "";
        Enum.itemType itemType;
        Enum.currency currency;
        int itemId=-1;
        try {
            itemType = Enum.itemType.valueOf(msgMap.取值("itemType").toString());
            currency = Enum.currency.valueOf(msgMap.取值("currency").toString());
            switch (currency) {
                case money:
                    currencyAlias = getPropByFile("bag", "bag_money");
                    break;
                case yb:
                    currencyAlias = getPropByFile("bag", "bag_yb");
                    break;
            }
            try{
                itemId =  Integer.parseInt(msgMap.取值("id").toString());
            }catch(Exception e){}
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        //玩家所要寄卖的物品
        Item item = new Item();
        item.setType(itemType);
        item.setId(itemId);
        Item bagItem;
        //查看玩家背包是否存在该物品  移除玩家物品
        switch (itemType) {
            case yb:
                if(num>100000||num<-100000)return;
                if(getPlayer().getPlayerData().getBag().getYbMoney()<num) {
                    String sendStr = getPropByFile("store", "auction_num_not_enough");
                    Chat chat = Chat.system(sendStr);
                    GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
                    return;
                }
                if (sell(item, price, num, currency)) {
                    getPlayer().getPlayerData().getBag().changeYbMoney(num>0?-num:num);
                    //发送出售信息
                    String sendStr = getPropByFile("store", "auction_sellYb_success",
                            price,
                            currencyAlias,
                            num<0?-num:num,
                           getPropByFile("bag", "bag_yb")
                    );
                    Chat chat = Chat.system(sendStr);
                    GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
                    return;
                }
                break;
            default:
                bagItem = getPlayer().getPlayerData().getBag().getItem(itemId);
                if (bagItem != null && getPlayer().getPlayerData().getBag().getItems().contains(item)) {
                    if (bagItem.getNum() >= num) {
                        if(num>100000||num<-100000)return;
                        if (sell(item, price, num, currency)) {
                            getPlayer().getPlayerData().getBag().delItem(itemId, num);
                            //发送出售信息
                            String sendStr = getPropByFile("store", "auction_sell_success",
                                    price,
                                    currencyAlias,
                                    num,
                                    bagItem.getName()
                            );
                            Chat chat = Chat.system(sendStr);
                            GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
                            return;
                        }
                    } else {
                        String sendStr = getPropByFile("store", "auction_num_not_enough");
                        Chat chat = Chat.system(sendStr);
                        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
                        return;
                    }
                }
                break;
        }

    }

    //保存到寄卖数据库
    private boolean sell(Item item, long price, int num, Enum.currency currency) {
        Auction auction = new Auction();
        auction.setId(BDate.getNowMillsByTen());
        auction.setRole(getPlayer().getAccount());
        auction.setCreateat(new Date());
        auction.setCurrency(currency);
        auction.setPrice(price);
        auction.setNum(num);
        auction.setTotal(num);
        auction.setItemType(item.getType());
        auction.setItem(item.getId());
        return AuctionCache.add(auction);
//        int affected_rows = DataBase.getInstance().init().createSQL(
//               "insert into t_auction (role,item,itemType,price,currency,num,createat) values("+
//               "?,?,?,?,?,?,now()"+
//                       ");",
//                getPlayer().getAccount(),
//                item.getId(),
//                item.getType(),
//                price,
//                currency,
//                num
//        ).update();
    }
}
