//我的面板显示的属性
function 初始化(client, 我) {
    var 属性 = 我.getAttribute();
    var 等级称谓 = ["金丹1重","金丹2重","<green>金丹3重</>","金丹4重","金丹5重"];
    var 等级 = Number(我.getLevel());
    var 称谓 = 等级<5?等级称谓[等级-1]:"飞升境界";
    var content =
        "<pink>『境界』 </>" + 称谓 +"<br>"+
        "<yellow>『名称』 : " + 我.getName() + " </><br>" +
        "<red>『攻击』 : " + 属性.getAtk() + " </><br>" +
        "<blue>『防御』 : " + 属性.getDef() + " </><br>" +
        "<purple>『命中』 : " + 属性.getAcc() + " </><br>" +
        "<green>『闪躲』 : " + 属性.getEva() + " </><br>"+
        "<yellow>『幸运』 : " + 属性.getLuk() + " </>";
    return content;
}

