package de.fams.dommod.tasks;

import java.util.Map;

import com.google.common.collect.Maps;

public class TaskRegistry {

	public static Map<String, Task> TASK_TABLE = Maps.newHashMap();
	
	static {
		TASK_TABLE.put("check", new Check());
	}
	
}