//格式为 名称 类型 id 价格
var 商城=[
    {name:"药水",type:"normal",id:"1",price:100},
    {name:"小木剑/1金币",type:"normal",id:"2",price:130},
]
//金币商城
function 初始化(client,窗口,消息){
    if(消息.getRole()!=null){
        if(消息.getMsg()==null){
            执行购买(client,窗口,消息);
        }else{
            初始化购买(client,窗口,消息);
        }
    }else{
        index=-1
        商城.forEach(function(obj,index,array){
            脚本工具.添加选项(obj.name, "action", "jbStore", obj.type, obj.name, "close");
        });
        窗口.添加选项集合(脚本工具)
        脚本工具.返回元素消息(client,"action","doTalk",null,窗口);
    }
}

function 初始化购买(client,窗口,消息){
    if(存在商城(消息.getRole(),商城)) {
        临时物品 = {name:消息.getRole(),type:消息.getMsg()}//名称  类型
    }else{
        临时物品 = { name:0,type:""}
    }
    窗口.内容("选择购买的数量哦。");
    脚本工具.添加执行选项("买1个", "jbStore", null, "1", "closeAll","red");
    脚本工具.添加执行选项("买10个","jbStore", null, "10", "closeAll","yellow");
    脚本工具.添加执行选项("买50个","jbStore", null, "10", "closeAll","blue");
    窗口.添加选项集合(脚本工具)
    脚本工具.返回元素消息(client,"action","doTalk",null,窗口);
}

function 执行购买(client,窗口,消息){
    if(存在商城(临时物品.name,商城)){
        if (消息.getRole()!=null) {
            if(临时物品.type=="normal"){
                var 要买的物品 = 获取商品(临时物品.name);
                脚本工具.购买(client,要买的物品.id,消息.getRole(),要买的物品.price);
            }else{
                脚本工具.购买技能(client,要买的物品.id,消息.getRole(),要买的物品.price);
            }

        }
    }
}
function 获取商品(value){
    var flag = 0;
    商城.forEach(function(obj,index,array){
        if(obj.name==value)flag = obj;
    });
    return flag;
}

function 存在商城(value, tbl){
    var flag = false;
    tbl.forEach(function(obj,index,array){
        if (obj.name==value)flag = true;
    });
    return flag;
}

