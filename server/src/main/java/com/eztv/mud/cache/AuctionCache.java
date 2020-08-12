package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Auction;

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

    public static void initAuctionCache() {
        synchronized (auctions) {
            auctions.clear();
            List<Auction> auctionList = DataBase.getInstance().init().createSQL(AuctionSql).list(Auction.class);
            auctions.addAll(auctionList);
            auctionList = null;
        }
    }


}
