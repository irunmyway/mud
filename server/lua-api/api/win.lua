---@return "多功能"
function 属性(attr) end
function 内容(文本型) end

---@return "窗口"
function 列数(整数型) end
function 添加选项集合(luaUtil) end

---@return "背包"
function 给物品(id,num) end
function 给技能(id,num) end
function 删物品(id,num) end
function 删物品集(id) end
function 给经验(exp) end
function 给铜币(money) end
function 给金币(jb) end
function 给元宝(yb) end

---@return "任务"
function 取状态() end
function 取任务详情() end
function 取任务() end

---@return "物品"
function 类型(物品类型) end
function 到文本(物品类型) end

---@return "玩家操作"
function 学习技能(client,item) end
function 装备(client,item) end
function 当前等级(client) end

---@return "工具"
function 任务创建(id,state,nextId,desc) end
function 任务设置奖励(bag) end
function 任务创建条件(id,num) end
function 任务添加条件集(任务类型) end
function 取任务() end

---@return "通讯"
function 发送消息(client,byte) end
function 返回系统消息(client,str) end
function 返回数组消息(client,byte) end
function 返回元素消息(client,byte) end
