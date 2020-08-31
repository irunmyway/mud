/**
 * 作者: hhx QQ1025334900
 * 功能: 重写ajax
 **/
curMap=0;
adminToken = sessionStorage.getItem("adminToken");;
(function ($) {
    ajax = $.ajax;
    prompIndex = -1;
    $.ajax = function (s) {
        if (adminToken == null || adminToken.length < 2 || adminToken == undefined) {
            prompIndex = setToken(prompIndex);
        }
        try {
            s.data["token"] = adminToken;
            var oldError = s.error;
            var oldSuccess = s.success;
            s.success = function (data) {
                if (data.code == -1) {
                    // console.log(data.code)
                    prompIndex = setToken(prompIndex);
                }
                oldSuccess(data);
            }
            s.error = function (data, val, as) {
                console.log("err" + as)
                try {
                    oldError(data);
                }catch (e) {}
            }
        } catch (error) {

        }
        ajax(s);
    }
    function setToken(index) {
        if (index == undefined || index < 0)
            var index = layer.prompt({
                formType: 2,
                value: "123",
                title: "还未登录,请输入密码。"
            }, function (value, index, elem) {
                adminToken = value;
                sessionStorage.setItem("adminToken", value);
                layer.close(index);
                layui.index.render()
                prompIndex = -2;
            });
        return index;
    }
})(jQuery);

// ##############################################技能模块
// ##############################################技能模块
// ##############################################技能模块
// ##############################################技能模块
// ##############################################技能模块
// ##############################################技能模块



doEditSkill = function (id) {
    var name = $("#edt_name").val();
    var script = $("#edt_script").val();
    $.ajax({
        url: layui.setter.mud_api + 'game/api/skill/saveSkill',
        data: { id: id, name: name, script: script },
        type: "post",
        success: function (code) {
            layui.table.reload('LAY-id-table-operate');
        },
        error: function () {
            alert("修改失败了。");
        }
    });
    layer.close(layerIndex);
    return false;
}
doDelSkill = function (obj) {
    $.ajax({
        url: layui.setter.mud_api + 'game/api/skill/delSkill',
        data: { id: obj.data.id },
        type: "post",
        success: function (code) {
            obj.del()
        },
        error: function () {
            alert("删除失败了。");
        }
    });

}

function createSkill() {
    $.ajax({
        url: layui.setter.mud_api + 'game/api/skill/createSkill',
        data: {},
        type: "post",
        success: function (code) {
            layui.table.reload('LAY-id-table-operate');
        },
        error: function () {
            alert("创建失败了。");
        }
    });

}

function searchSkill(val) {
    layui.table.reload('LAY-id-table-operate', {
        where: {
            value: val
        }
    });

}

// ##############################################  Map
// ##############################################  Map
// ##############################################  Map
// ##############################################  Map
// ##############################################  Map

changeEdges = function (edge) {
    console.log(edge)
}


createRoom = function () {
    $.ajax({
        url: layui.setter.mud_api + 'game/api/room/createRoom',
        data: {map:curMap},
        type: "post",
        success: function (code) {
            layui.index.render();
        },
        error: function () {
            layer.msg("创建失败");
        }
    });
}

refresh = function () {
    layui.index.render()
}

findNodes = function (id) {
    var node;
    nodes.forEach(v => {
        if (v.id === id) {
            node = v;
        }
    });
    return node;
}
findEdges = function (id) {
    if (id == undefined) return null;
    id = id.edgeId;
    var edge;
    edges.forEach(v => {
        if (v.id === id) {
            edge = v;
        }
    });
    return edge;
}
delRoom = function (id) {
    $.ajax({
        url: layui.setter.mud_api + 'game/api/room/delRoom',
        data: { id: id },
        type: "post",
        success: function (code) {
            layui.index.render();
        },
        error: function () {
            layer.msg("删除失败");
        }
    });
}
createRoomByBase = function (id, direction) {
    $.ajax({
        url: layui.setter.mud_api + 'game/api/room/createRoomByBase',
        data: { id: id, map:curMap,direction: direction, name: $("#edt_name").val(), desc: $("#edt_desc").val(), script: $("#edt_script").val() },
        type: "post",
        success: function (code) {
            layui.index.render();
        },
        error: function () {
            layer.msg("创建失败");
        }
    });
}

saveRoom = function (id) {
    var newId = $("#id").val();
    $.ajax({
        url: layui.setter.mud_api + 'game/api/room/saveRoom',
        data: { id: id,newId:newId, name: $("#edt_name").val(), desc: $("#edt_desc").val(), script: $("#edt_script").val() },
        type: "post",
        success: function (code) {
            layui.index.render();
        },
        error: function () {
            layer.msg("保存失败");
        }
    });
}
showEditPanel = function (node) {
    layer.open({
        type: 1,
        title: '地图编辑',
        shadeClose: false,
        shade: 0.6,
        // area: ['320px', '500px'],
        content: `  <div class="layui-card-body">
            <form action="" class="layui-form">
             <div class="layui-form-item">
                    <label class="layui-form-label">id</label>
                    <div class="layui-input-block">
                        <input id="id" value="`+ (node.id == undefined ? "" : node.id) + `" type="text" class="layui-input" autocomplete="off" lay-verify="required" />
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">名称</label>
                    <div class="layui-input-block">
                        <input id="edt_name" value="`+ (node.label == undefined ? "" : node.label) + `" type="text" class="layui-input" autocomplete="off" lay-verify="required" />
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">介绍</label>
                    <div class="layui-input-block">
                        <input id="edt_desc" value="`+ (node.desc == undefined ? "" : node.desc) + `" type="text" class="layui-input" autocomplete="off" lay-verify="required" />
                    </div>
                </div>
                 <div class="layui-form-item">
                    <label class="layui-form-label">脚本</label>
                    <div class="layui-input-block">
                        <input id="edt_script" value="`+ (node.script == undefined ? "" : node.script) + `" type="text" class="layui-input" autocomplete="off" lay-verify="required" />
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <div class="layui-block" style="margin-bottom: 10px;">
                        <button onclick="createRoomByBase('`+ node.id + `','l')" class="layui-btn">
                            向左创建
                        </button>
                        <button onclick="createRoomByBase('`+ node.id + `','r')" class="layui-btn">
                            向右创建
                        </button>
                        </div>
                        <div class="layui-block" style="margin-bottom: 10px;">
                        <button onclick="createRoomByBase('`+ node.id + `','u')" class="layui-btn">
                            向上创建
                        </button>
                        <button onclick="createRoomByBase('`+ node.id + `','d')" class="layui-btn">
                            向下创建
                        </button>
                        </div>
                        <button onclick="saveRoom('`+ node.id + `')" class="layui-btn layui-btn-normal">
                            保存
                        </button>
                        <button onclick="delRoom('`+ node.id + `')" type="button" class="layui-btn layui-btn-primary">
                            删除
                        </button>
                    </div>
                </div>
            </form>
        </div>`
    });
}