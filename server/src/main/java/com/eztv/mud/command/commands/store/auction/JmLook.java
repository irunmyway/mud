package com.eztv.mud.command.commands.store.auction;

import com.eztv.mud.DataBase;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.manager.ItemManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.List;

import static com.eztv.mud.Constant.PlayerSql;
import static com.eztv.mud.Constant.pageLimitCol2;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.cache.manager.AuctionManager.*;

public class JmLook extends BaseCommand {
    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-08-09 22:08
     * 功能: 查看寄卖 其中包括查看自己的寄卖 或者 具体那个分类的寄卖
     **/
    public JmLook(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    int pageIndex = 0;

    @Override
    public void execute() {


        MsgMap msgMap = new MsgMap();
        if (getMsg().getMsg() != null) {
            msgMap = msgMap.到消息(getMsg().getMsg());
            if (msgMap.取值("cmd") != null) {
                try {
                    pageIndex = Integer.parseInt(msgMap.取值("pageIndex").toString());
                } catch (Exception e) {
                }
                switch (msgMap.取值("cmd").toString()) {
                    case "我的寄卖":
                        getAuctions(getAuctionsByRole(getPlayer().getAccount(), pageIndex, pageLimitCol2));
                        break;
                    case "商品":
                        getAuctions(getAuctionsByType(Enum.itemType.normal, pageIndex));
                        break;
                    case "技能":
                        getAuctions(getAuctionsByType(Enum.itemType.skill, pageIndex));
                        break;
                    case "所有":
                        getAuctions(getAuctionsByType(null, pageIndex));
                        break;
                    case "货币":
                        getAuctions(getAuctionsByType(Enum.itemType.yb, pageIndex));
                        break;
                    case "寄卖操作":
                        actionManagePanel();
                        break;
                }
            }
        }
    }


    //我的寄卖
    private void getAuctions(List<Auction> auctions) {
        MsgMap msgMap = new MsgMap();
        msgMap = msgMap.到消息(getMsg().getMsg());
        String tmpCmd = msgMap.取值("cmd").toString();
        msgMap.添加("cmd", "寄卖操作");
        Item item = null;
        if (auctions.size() < 1) {
            getWinMsg().setDesc(getPropByFile("store", "auction_sell_none"));
        }
        //翻页
        if (pageIndex > pageLimitCol2 - 1) {
            msgMap.添加("pageIndex", pageIndex - pageLimitCol2);
            msgMap.添加("cmd", tmpCmd);
            getChoice().add(Choice.createChoice(
                    "上一页",
                    Enum.messageType.action,
                    getMsg().getCmd(),
                    msgMap.到文本(),
                    null).setBgColor(Enum.color.blue)
            );
        }
        for (Auction auction : auctions) {
            switch (auction.getItemType()) {
                case normal:
                    item = ItemManager.getItemById(auction.getItem() + "");
                    break;
                case skill:
                    item = ItemManager.getSkillById(auction.getItem() + "");
                    break;
                case yb:
                    break;
            }
            msgMap.添加("auction", auction.getId());
            if (auction.getItemType() == Enum.itemType.yb) {
                getChoice().add(Choice.createChoice(
                        getPropByFile("bag", "bag_item",
                                getPropByFile("bag", "bag_yb"),
                                getPropByFile("store", "auction_item",
                                        (auction.getNum() < 2 ? "1" : auction.getNum()),
                               auction.getPrice() + getCurrencyAlias(auction.getCurrency()))
                        ),
                        Enum.messageType.action,
                        getMsg().getCmd(),
                        msgMap.到文本(),
                        null)
                );
                continue;
            }
            if (item == null) continue;
            item.setNum(auction.getNum());
            getChoice().add(Choice.createChoice(
                    getPropByFile("bag", "bag_item",
                            item.getName(),
                            getPropByFile("store", "auction_item",
                                    (auction.getNum() < 2 ? "1" : auction.getNum()),
                            auction.getPrice() + getCurrencyAlias(auction.getCurrency()))
                    ),Enum.messageType.action,
                    getMsg().getCmd(),
                    msgMap.到文本(),
                    null)
            );
        }
        //翻页
        if (auctions.size() > 0) {
            msgMap.添加("pageIndex", pageIndex + pageLimitCol2);
            msgMap.添加("cmd", tmpCmd);
            getChoice().add(Choice.createChoice(
                    "下一页",
                    Enum.messageType.action,
                    getMsg().getCmd(),
                    msgMap.到文本(),
                    null).setBgColor(Enum.color.blue)
            );
        }
        getWinMsg().setCol(2);
        getWinMsg().setChoice(getChoice());
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.pop, "", object2JsonStr(getWinMsg()), getMsg().getMsg(), null));
    }

    //操作面板  //下架 查看当前寄卖的信息 比如金钱等等
    private void actionManagePanel() {
        MsgMap msgMap = new MsgMap();
        msgMap = msgMap.到消息(getMsg().getMsg());
        Auction auction = null;
        if (msgMap.取值("auction") != null)
            try {
                auction = getAuction(Integer.parseInt(msgMap.取值("auction").toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (auction != null) {
            if (auction.getRole().equals(getPlayer().getAccount())) {
                getWinMsg().setDesc(getPropByFile("store", "auction_sell_remaining_desc",
                        auction.getTotal(),
                        auction.getNum()
                ));
                getChoice().add(Choice.createChoice("下架",
                        Enum.messageType.action,
                        "jmCancel",
                        msgMap.到文本(),
                        null).setBgColor(Enum.color.red)
                );
            } else {
                getChoice().add(Choice.createChoice("购买",
                        Enum.messageType.action,
                        "jmBuy",
                        msgMap.到文本(),
                        null).setBgColor(Enum.color.blue)
                );
            }
            //设置出售者的信息
            Player player = DataBase.getInstance().init().createSQL(PlayerSql, auction.getRole()).unique(Player.class);
            if (player != null && getWinMsg().getDesc() == null) {
                getWinMsg().setDesc(getPropByFile("store", "auction_sell_desc",
                        player.getName()
                ));
            }
            getWinMsg().setCol(2);
            getWinMsg().setChoice(getChoice());
            GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.pop, "", object2JsonStr(getWinMsg()), getMsg().getMsg(), null));

        }
    }
}
