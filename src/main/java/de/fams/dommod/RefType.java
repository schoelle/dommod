package de.fams.dommod;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public enum RefType {
	WEAPON(0, 800, 1999), ARMOR(0, 300, 999), MONSTER(0, 3500, 8999), NAMETYPE(100, 165, 299), SPELL(0, 1300, 3999),
	ITEM(0, 500, 999), SITE(0, 1500, 1999), NATION(0, 120, 249), MERCENARY(0, 100, 200), // Artifical numbers
	POPTYPE(0, 100, 200); // Artifical numbers

	public final int minId;
	public final int minModId;
	public final int maxId;

	RefType(int min, int minMod, int max) {
		this.minId = min;
		this.minModId = minMod;
		this.maxId = max;
	}

	public static final List<String> WEAPON_CMDS = Lists.newArrayList("selectweapon", "copyweapon", "newweapon",
			"secondaryeffect", "secondaryeffectalways", "weapon");

	public static final List<String> ARMOR_CMDS = Lists.newArrayList("selectarmor", "newarmor", "copyarmor", "armor");

	public static final List<String> MONSTER_CMDS = Lists.newArrayList("selectmonster", "newmonster", "copystats",
			"copyspr", "monpresentrec", "ownsmonrec", "raiseshape", "shapechange", "prophetshape", "firstshape",
			"secondshape", "secondtmpshape", "forestshape", "plainshape", "foreignshape", "homeshape", "domshape",
			"notdomshape", "springshape", "summershape", "autumnshape", "wintershape", "landshape", "watershape",
			"twiceborn", "domsummon", "domsummon2", "domsummon20", "raredumsummon", "templetrainer", "makemonsters1",
			"makemonsters2", "makemonsters3", "makemonsters4", "makemonsters5", "summon1", "summon2", "summon3",
			"summon4", "summon5", "battlesum1", "battlesum2", "battlesum3", "battlesum4", "battlesum5", "batstartsum1",
			"batstartsum2", "batstartsum3", "batstartsum4", "batstartsum5", "batstartsum1d3", "batstartsum1d6",
			"batstartsum2d6", "batstartsum3d6", "batstartsum4d6", "batstartsum5d6", "batstartsum6d6", "batstartsum7d6",
			"batstartsum8d6", "batstartsum9d6", "slaver", "damagemon", "farsumcom", "onlymnr", "notmnr", "unit",
			"homemon", "homecom", "mon", "com", "natmon", "natcom", "summon", "summonlvl2", "summonlvl3", "summonlvl4",
			"wallcom", "wallunit", "uwwallunit", "uwwallcom", "startcom", "coastcom1", "coastcom2", "addforeignunit",
			"addforeigncom", "rec", "terrainrec", "startscout", "terraincom", "startunittype1", "startunittype2",
			"addrecunit", "addreccom", "uwrec", "uwcom", "coastunit1", "coastunit2", "coastunit3", "landrec", "landcom",
			"defcom1", "defcom2", "defunit1", "defunit1b", "defunit1c", "defunit1d", "defunit2", "defunit2b", "wallcom",
			"addgod", "delgod", "cheapgod20", "cheapgod40", "guardspirit");

	public static final List<String> NAMETYPE_CMDS = Lists.newArrayList("nametype", "selectnametype");

	public static final List<String> SPELL_CMDS = Lists.newArrayList("enchrebate50", "enchrebate25p", "enchrebate50p",
			"onebattlespell", "selectspell", "newspell", "copyspell", "spell", "autospell");

	public static final List<String> ITEM_CMDS = Lists.newArrayList("startitem", "selectitem", "newitem", "copyitem",
			"item");

	public static final List<String> SITE_CMDS = Lists.newArrayList("onlyatsite", "selectsite", "newsite", "startsite",
			"islandsite");

	public static final List<String> NATION_CMDS = Lists.newArrayList("restricted", "notfornation", "nationrebate",
			"nat", "selectnation", "newnation");

	public static final List<String> MERCENARY_CMDS = Lists.newArrayList("newmerc");

	public static final List<String> POPTYPE_CMDS = Lists.newArrayList("selectpoptype");

	public static final Map<String, RefType> REFTYPE_BY_NAME = Maps.newHashMap();

	static {
		WEAPON_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, WEAPON));
		ARMOR_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, ARMOR));
		MONSTER_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, MONSTER));
		NAMETYPE_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, NAMETYPE));
		SPELL_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, SPELL));
		ITEM_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, ITEM));
		SITE_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, SITE));
		NATION_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, NATION));
		MERCENARY_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, MERCENARY));
		POPTYPE_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, POPTYPE));
	}
	
	public static final Map<String, RefType> STARTCMD_FOR_NAME = Maps.newHashMap();
	
	static {
		STARTCMD_FOR_NAME.put("selectweapon", WEAPON);
		STARTCMD_FOR_NAME.put("newweapon", WEAPON);
		STARTCMD_FOR_NAME.put("selectarmor", ARMOR);
		STARTCMD_FOR_NAME.put("newarmor", ARMOR);
		STARTCMD_FOR_NAME.put("selectmonster", MONSTER);
		STARTCMD_FOR_NAME.put("newmonster", MONSTER);
		STARTCMD_FOR_NAME.put("selectnametype", NAMETYPE);
		STARTCMD_FOR_NAME.put("selectspell", SPELL);
		STARTCMD_FOR_NAME.put("newspell", SPELL);
		STARTCMD_FOR_NAME.put("selectitem", ITEM);
		STARTCMD_FOR_NAME.put("newitem", ITEM);
		STARTCMD_FOR_NAME.put("selectsite", SITE);
		STARTCMD_FOR_NAME.put("newsite", SITE);
		STARTCMD_FOR_NAME.put("selectnation", NATION);
		STARTCMD_FOR_NAME.put("newnation", NATION);
		STARTCMD_FOR_NAME.put("newmerc", MERCENARY);
		STARTCMD_FOR_NAME.put("selectpoptype", POPTYPE);
	}

}
