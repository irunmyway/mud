package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Auction;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.AuctionSql;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-09 23:14
 * 功能: 寄卖缓存
 **/
public class AuctionCache {
    public static List<Auction> auctions = new ArrayList<>();
    public static void initAuctionCache(){
        synchronized (auctions){
            auctions.clear();
            List<Auction> auctionList = DataBase.getInstance().init().createSQL(AuctionSql).list(Auction.class);
            auctions.addAll(auctionList);
            auctionList=null;
        }
    }
    //添加寄卖
    public static boolean add(Auction auction){
        int affected_rows=0;
        synchronized (auctions){
            affected_rows = DataBase.getInstance().init().query(auction).insert();
            initAuctionCache();
        }
        return affected_rows>0;
    }


    public static void replace(List<Auction> auctionList){
        synchronized (auctions) {
            for (Auction auction:auctionList){
                DataBase.getInstance().init().query(auction).update();
            }
            initAuctionCache();
        }
    }
    //添加寄卖
    public static boolean replace(Auction auction){
        int affected_rows=0;
        synchronized (auctions){
            affected_rows = DataBase.getInstance().init().query(auction).update();
            initAuctionCache();
        }
        return affected_rows>0;
    }
    //下架寄卖
    public static boolean remove(Auction auction){
        int affected_rows=0;
        synchronized (auctions){
            affected_rows = DataBase.getInstance().init().query(auction).delete();
            auctions.remove(auction);
            initAuctionCache();
        }
        return affected_rows>0;
    }
    //获取寄卖 通过类型
    public static List<Auction> getAuctionsByType(Enum.itemType itemType){
        List<Auction> auctionList = new ArrayList<>();
        for (Auction auction:auctions){
            if(auction.getNum()<1)continue;
            if(itemType==null){
                auctionList.add(auction);
                continue;
            }
            if(auction.getItemType()== itemType){
                auctionList.add(auction);
            }
        }
        return auctionList;
    }

    //获取玩家的寄卖
    public static List<Auction> getAuctionsByRole(String account){
        List<Auction> auctionList = new ArrayList<>();
        for (Auction auction:auctions){
            if(auction.getRole().equals(account)){
                auctionList.add(auction);
            }
        }
        return auctionList;
    }
    //通过id获取寄卖的指定物品
    public static Auction getAuction(int id){
        for (Auction au:auctions){
            if(au.getId()==id)
                return au;
        }
        return null;
    }

}
