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
    var 当前技能 = 脚本工具.取当前技能(攻击者);
    var 技能输出 = 当前技能.getAttribute().getAtk();

    var 我的输出 = 攻击者.getAttribute().getAtk()+技能输出;
    var 对方防御 = 目标.getAttribute().getDef()
    if ((我的输出 -  对方防御)< 1) return 1;
    return 我的输出 -  对方防御;
}

/**
 日期: 2020-07-21 11:49
 用处：计算命中 (对方闪避 - 自身命中） / 对方闪避
 **/
function 命中(攻击者, 目标) {
    var 对方闪避 = 目标.getAttribute().getEva();
    var 自身命中 = 攻击者.getAttribute().getAcc();
    if (( 对方闪避- 自身命中) == 0 || 对方闪避 == 0) return true;
    return (对方闪避 - 自身命中) / 对方闪避 < Math.random();
}