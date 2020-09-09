// 计算公式
// atk攻击
// def防御
// eva闪避
// acc命中
/**
 日期: 2020-07-21 11:29
 用处：玩家的真实伤害， （自身攻击 - 对方防御）
 **/
function 攻击(攻击者, 目标) {
    if (攻击者.getAttribute().getAtk() - 目标.getAttribute().getDef() < 1) return 1;
    return 攻击者.getAttribute().getAtk() - 目标.getAttribute().getDef();
}

/**
 日期: 2020-07-21 11:49
 用处：计算命中 (对方闪避 - 自身命中） / 对方闪避
 **/
function 命中(攻击者, 目标) {
    if ((目标.getAttribute().getEva() - 攻击者.getAttribute().getAcc()) == 0 || 目标.getAttribute().getEva() == 0) return true;
    return (目标.getAttribute().getEva() - 攻击者.getAttribute().getAcc()) / 目标.getAttribute().getEva() < Math.random();
}