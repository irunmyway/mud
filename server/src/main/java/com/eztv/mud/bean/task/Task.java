package com.eztv.mud.bean.task;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Bag;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.script.LuaOpen;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class Task implements LuaOpen.LuaTask {
	private String id;//任务编号
	private Enum.taskState taskState;//任务状态{可接,不可接,正在完成,已经完成}
	private String desc;
	private List<TaskCondition> taskConditions=new ArrayList<>();//任务条件（conditions）
	private Bag reward;//奖励
	private String nextId;//指引到下一个任务

	@Test
	public void test(){
	}
	public void addCondition(Enum.taskType taskType,List<TaskAction> list){//添加一个任务
		TaskCondition taskCondition = new TaskCondition();
		taskCondition.setTaskActions(((List<TaskAction>)(Object)list));
		this.taskConditions.add(taskCondition);
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Enum.taskState getTaskState() {
		return taskState;
	}

	public void setTaskState(Enum.taskState taskState) {
		this.taskState = taskState;
	}
	public void setTaskState(String state) {
		this.taskState = Enum.taskState.valueOf(state);
	}
	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

	public Bag getReward() {
		return reward;
	}

	public void setReward(Bag reward) {
		this.reward = reward;
		taskState= Enum.taskState.cant;//默认设为不可接
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<TaskCondition> getTaskConditions() {
		return taskConditions;
	}

	public void setTaskConditions(List<TaskCondition> taskConditions) {
		this.taskConditions = taskConditions;
	}

    public String showTaskDetail(){
	    String str="";
        //this.getTaskState()
		for(TaskCondition taskCondition:this.taskConditions){
			for (TaskAction taskAction:taskCondition.getTaskActions()){
				switch (taskCondition.getType()){
					case kill:
						str+="击杀"+GameUtil.getMonstertById(taskAction.getId()).getName()+" 进度 "+taskAction.getProcess()+"/"+taskAction.getNum();
						str+="\n";
						break;
				}
			}
		}
	    return str;
    }

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false ;
		}
		if (obj instanceof Task){
			Task task = (Task) obj;
			if(task.getId() .equals(this.id) ){
				return true ;
			}
		}
		return false ;
	}

	@Override
	public String 取任务详情() {
		String str="";
		//this.getTaskState()
		for(TaskCondition taskCondition:this.taskConditions){
			for (TaskAction taskAction:taskCondition.getTaskActions()){
				switch (taskCondition.getType()){
					case kill:
						str+="击杀"+GameUtil.getMonstertById(taskAction.getId()).getName()+" 进度 "+taskAction.getProcess()+"/"+taskAction.getNum();
						str+="\n";
						break;
					case talk:
						boolean flag = this.getTaskState()== Enum.taskState.finished;
						str+=" 进度 "+(flag?"已完成":"未完成");
						str+="\n";
						break;
				}
			}
		}
		return str;
	}

	@Override
	public String 取状态() {
		return taskState.toString();
	}

	@Override
	public void 置状态(String state) {
		this.taskState = Enum.taskState.valueOf(state);
	}
}
