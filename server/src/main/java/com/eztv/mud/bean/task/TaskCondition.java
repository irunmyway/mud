package com.eztv.mud.bean.task;

import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

/**
 作者：hhx QQ1025334900
 日期: 2020-07-15 11:12
 用处：任务类型
**/
public class TaskCondition {
    private Enum.taskType type;//任务类型
    //条件集合
    private List<TaskAction> taskActions = new ArrayList<>();

    public List<TaskAction> getTaskActions() {
        return taskActions;
    }

    public void setTaskActions(List<TaskAction> taskActions) {
        this.taskActions = taskActions;
    }

    public Enum.taskType getType() {
        return type;
    }

    public void setType(Enum.taskType type) {
        this.type = type;
    }
}
