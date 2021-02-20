package de.fams.dommod.tasks;

import com.google.common.collect.Maps;

import java.util.Map;

public class TaskRegistry {

	public static Map<String, Task> TASK_TABLE = Maps.newHashMap();
	
	static {
		TASK_TABLE.put("check", new Check());
		TASK_TABLE.put("dot", new Dot());
		TASK_TABLE.put("output", new Output());
		TASK_TABLE.put("json", new ToJson());
		TASK_TABLE.put("info", new Info());
		TASK_TABLE.put("slicenation", new SliceNation());
//		TASK_TABLE.put("withouthats", new WithoutHats());
		TASK_TABLE.put("usedids", new UsedIds());
		TASK_TABLE.put("avoid", new Avoid());
	}
}
