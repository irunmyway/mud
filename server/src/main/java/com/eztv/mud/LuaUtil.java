package com.eztv.mud;

import com.eztv.mud.bean.Bag;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.task.Task;
import com.eztv.mud.bean.task.TaskAction;
import com.eztv.mud.bean.task.TaskCondition;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDebug;

import java.util.ArrayList;
import java.util.List;

public class LuaUtil {
    private Task task = new Task();
    private List<TaskAction> taskActions = new ArrayList<>();
    public LuaUtil taskCreate(String id, Enum.taskState taskState, String nextId,String desc){//任务状态
        taskActions.clear();
        task.setId(id);
        task.setTaskState(taskState);
        task.setNextId(nextId);
        task.setDesc(desc);
        return this;
    }
    public LuaUtil taskSetReward(Bag bag){//任务奖励
        task.setReward(bag);
        return this;
    }
    public LuaUtil taskCreateAction(String id,int num) {//添加具体任务
        TaskAction taskAction = new TaskAction();
        taskAction.setId(id);
        taskAction.setNum(num);
        taskActions.add(taskAction);
        return this;
    }
    public LuaUtil taskAddCondition(Enum.taskType taskType){//添加一个任务任务类型
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setType(taskType);
        taskCondition.setTaskActions(taskActions);
        task.getTaskConditions().add(taskCondition);
        //taskActions.clear();
        return this;
    }


    public Task getTask() {
        return task;
    }

    ////////////////////////////////////////创建选项部分/////////////////////////////////////////
    private List<Choice> choice = new ArrayList<>();
    public List<Choice> getChoice() {
        return choice;
    }
    public void setChoice(List<Choice> choice) {
        this.choice = choice;
    }
}
