package com.eztv.mud.algorithm;

import com.eztv.mud.bean.GameObject;
import com.eztv.mud.utils.BDebug;

public class AttackAlgorithm {
    /**
     作者：hhx QQ1025334900
     日期: 2020-07-21 11:29
     用处：玩家的真实伤害， （自身攻击 - 对方防御）
    **/
    public static float computeRealAtk(GameObject self,GameObject target){
        if(self.getAttribute().getAtk()-target.getAttribute().getDef()<1)return 1;
        return self.getAttribute().getAtk()-target.getAttribute().getDef();
    }
    /**
     作者：hhx QQ1025334900
     日期: 2020-07-21 11:49
     用处：计算命中 (对方闪避 - 自身命中） / 对方闪避
    **/
    public static boolean computeAccuracy(GameObject self,GameObject target){
        if((target.getAttribute().getEva()-self.getAttribute().getAcc())==0||target.getAttribute().getEva()==0)return true;
        return  (target.getAttribute().getEva()-self.getAttribute().getAcc())/ (double)target.getAttribute().getEva()< Math.random();
    }
}
