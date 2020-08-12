package com.eztv.mud.cache.manager;

import com.eztv.mud.DataBase;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Auction;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.pageLimitCol2;
import static com.eztv.mud.cache.AuctionCache.auctions;
import static com.eztv.mud.cache.AuctionCache.initAuctionCache;

public class AuctionManager {

    //获取货币别名
    public static String getCurrencyAlias(Enum.currency currency){
        switch (currency){
            case money:
                return GameUtil.getPropByFile("bag","bag_money");
            case yb:
                return GameUtil.getPropByFile("bag","bag_yb");
            case jb:
                return GameUtil.getPropByFile("bag","bag_jb");
            default:
                return GameUtil.getPropByFile("bag","bag_money");
        }
    }

    //添加寄卖
    public static boolean add(Auction auction) {
        int affected_rows = 0;
        synchronized (auctions) {
            affected_rows = DataBase.getInstance().init().query(auction).insert();
            initAuctionCache();
        }
        return affected_rows > 0;
    }


    public static void replace(List<Auction> auctionList) {
        synchronized (auctions) {
            for (Auction auction : auctionList) {
                DataBase.getInstance().init().query(auction).update();
            }
            initAuctionCache();
        }
    }

    //添加寄卖
    public static boolean replace(Auction auction) {
        int affected_rows = 0;
        synchronized (auctions) {
            affected_rows = DataBase.getInstance().init().query(auction).update();
            initAuctionCache();
        }
        return affected_rows > 0;
    }

    //下架寄卖
    public static boolean remove(Auction auction) {
        int affected_rows = 0;
        synchronized (auctions) {
            affected_rows = DataBase.getInstance().init().query(auction).delete();
            auctions.remove(auction);
            initAuctionCache();
        }
        return affected_rows > 0;
    }

    //获取寄卖 通过类型
    public static List<Auction> getAuctionsByType(Enum.itemType itemType, int index) {
        List<Auction> auctionList = new ArrayList<>();
        int size = getAuctionsSizeByType(itemType);
        for (int i = index; i < pageLimitCol2 + index; i++) {
            if (i >= size) break;
            Auction auction = auctions.get(i);
            if (auction.getNum() < 1) continue;
            if (itemType == null) {
                auctionList.add(auction);
                continue;
            }
            if (auction.getItemType() == itemType) {
                auctionList.add(auction);
            }
        }
        return auctionList;
    }

    //获取寄卖类型的数量
    public static int getAuctionsSizeByType(Enum.itemType itemType) {
        int size = 0;
        for (Auction auction : auctions) {
            if (auction.getItemType() == itemType || itemType == null) {
                size++;
            }
        }
        return size;
    }

    //获取自己寄卖的数量
    public static int getAuctionsSizeByRole(String account) {
        int size = 0;
        for (Auction auction : auctions) {
            if (auction.getRole().equals(account)) {
                size++;
            }
        }
        return size;
    }

    //获取玩家的寄卖
    public static List<Auction> getAuctionsByRole(String account, int index, int limit) {
        List<Auction> auctionList = new ArrayList<>();
        int size = getAuctionsSizeByRole(account);
        limit = limit==0? size:limit+index;
        for (int i = index; i < limit; i++) {
            if (i >= size) break;
            Auction auction = auctions.get(i);
            if (auction.getRole().equals(account)) {
                auctionList.add(auction);
            }
        }
        return auctionList;
    }

    //通过id获取寄卖的指定物品
    public static Auction getAuction(int id) {
        for (Auction au : auctions) {
            if (au.getId() == id)
                return au;
        }
        return null;
    }

}
