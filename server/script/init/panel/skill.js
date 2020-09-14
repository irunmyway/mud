//技能面板查看
function 物品查看(client, 技能, 窗口, msg){
    脚本工具.添加选项("升级", "action", "doTalk", "", "testTask");
    脚本工具.添加选项("下级", "action", "doTalk", "", "testTask");
    脚本工具.添加选项("删除", "action", "doTalk", "", "testTask");
    var 技能名称 = 技能.getName();
    var 技能属性 = 技能.getAttribute();
    var 额外伤害 = 技能属性.getAtk();
    var 扣除魔法 = 技能属性.getMp();
    var 升级所需潜能 = 技能.getPot();
    窗口.内容(技能名称 + "</p><br>火球术技能书,可学习：火球术<br>" +
    "<blue>额外伤害</>:<red>"+额外伤害+"</> <br>"+
    "<blue>扣除魔法</>:<red>"+扣除魔法+"</><br>"+
    "<blue>升级所需潜能</>:<red>"+升级所需潜能+"</>"
        );
    窗口.添加选项集合(脚本工具);
    窗口.列数(3);
    脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
}