package de.fams.dommod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Data extracted from Dom 5 Inspector CSV files
 */
public class InspectorData {
	
	public static final Map<EntityType, String> CSV_FILES = Maps.newHashMap();
	
	static {
		CSV_FILES.put(EntityType.WEAPON, "weapons.csv");
		CSV_FILES.put(EntityType.ARMOR, "armors.csv");
		CSV_FILES.put(EntityType.ITEM, "BaseI.csv");
		CSV_FILES.put(EntityType.MERCENARY, "Mercenary.csv");
		CSV_FILES.put(EntityType.MONSTER, "BaseU.csv");
		CSV_FILES.put(EntityType.NAMETYPE, "namestypes.csv");
		CSV_FILES.put(EntityType.NATION, "nations.csv");
		CSV_FILES.put(EntityType.SITE, "MagicSites.csv");
		CSV_FILES.put(EntityType.SPELL, "spells.csv");
	}
	
	private static final Map<EntityType, List<Item>> itemTable = Maps.newHashMap();
	
	public static class Item {
		public final int id;
		public final String name;
		public final Map<String, String> data;
		
		public Item(Map<String, String> data) {
			this.data = data;
			this.id = Integer.parseInt(data.get("id"));
			this.name = data.get("name").toLowerCase();
		}
	}
	
	public static List<Item> getBuiltIn(EntityType type) {
		if (!CSV_FILES.containsKey(type)) {
			return Lists.newArrayList();
		}
		return itemTable.computeIfAbsent(type, s -> readFile(CSV_FILES.get(s)));
	}
	
	public static List<Item> readFile(String fName) {
		List<Item> result = Lists.newArrayList();
		try {
			InputStream is = InspectorData.class.getClassLoader().getResourceAsStream("inspector/" + fName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String line = reader.readLine();
			String[] column = line.split("\t");
			line = reader.readLine();
			while (line != null) {
				String[] parts = line.split("\t");
				if(parts.length > column.length) {
					continue;
				}
				Map<String, String> data = Maps.newHashMap();
				for(int i = 0; i < parts.length; i++) {
					data.put(column[i], parts[i]);
				}
				result.add(new Item(data));
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.err.println("ERROR LOADING STATIC DATA FROM " + fName);
			return Lists.newArrayList();
		}
		return result;
	}

	public static boolean isBuiltIn(EntityType type, String name) {
		return getBuiltIn(type).stream().anyMatch(s -> s.name.equals(name.toLowerCase()));
	}
}
