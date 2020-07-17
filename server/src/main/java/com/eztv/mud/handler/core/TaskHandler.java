package com.eztv.mud.handler.core;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.task.Task;
import com.eztv.mud.constant.Enum;

public class TaskHandler {
    public Task checkTask(Client client, Task task){
        int pos =client.getPlayer().getPlayerData().getTasks().indexOf(task);
        if(pos==-1){//尚未接受该任务
            task.setTaskState(Enum.taskState.processing);
            client.getPlayer().getPlayerData().getTasks().add(task);//添加该任务
        }else{//已经接受
            Task mTask = client.getPlayer().getPlayerData().getTasks().get(pos);
            return mTask;
        }
        return task;
    }
}
