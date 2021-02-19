package de.fams.dommod;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

import static de.fams.dommod.EntityType.*;

/**
 * Static tables extracted from the Dominions 5 Modding manuals 
 */
public class StaticTables {

	//@formatter:off
	public static final Set<String> WEAPON_CMDS = Sets.newHashSet(
			"selectweapon",
			"copyweapon",
			"newweapon",
			"secondaryeffect",
			"secondaryeffectalways",
			"weapon");

	public static final Set<String> ARMOR_CMDS = Sets.newHashSet(
			"selectarmor", 
			"newarmor", 
			"copyarmor", 
			"armor");

	public static final Set<String> MONSTER_CMDS = Sets.newHashSet(
			"selectmonster", 
			"newmonster", 
			"copystats",
			"copyspr", 
			"monpresentrec", 
			"ownsmonrec", 
			"raiseshape", 
			"shapechange", 
			"prophetshape", 
			"firstshape",
			"secondshape", 
			"secondtmpshape", 
			"forestshape", 
			"plainshape", 
			"foreignshape", 
			"homeshape", 
			"domshape",
			"notdomshape", 
			"springshape", 
			"summershape", 
			"autumnshape", 
			"wintershape", 
			"landshape", 
			"watershape",
			"twiceborn", 
			"domsummon", 
			"domsummon2", 
			"domsummon20", 
			"raredumsummon", 
			"templetrainer", 
			"makemonsters1",
			"makemonsters2", 
			"makemonsters3", 
			"makemonsters4", 
			"makemonsters5", 
			"summon1", 
			"summon2", 
			"summon3",
			"summon4", 
			"summon5", 
			"battlesum1", 
			"battlesum2", 
			"battlesum3", 
			"battlesum4", 
			"battlesum5", 
			"batstartsum1",
			"batstartsum2", 
			"batstartsum3", 
			"batstartsum4", 
			"batstartsum5", 
			"batstartsum1d3", 
			"batstartsum1d6",
			"batstartsum2d6", 
			"batstartsum3d6", 
			"batstartsum4d6", 
			"batstartsum5d6", 
			"batstartsum6d6", 
			"batstartsum7d6",
			"batstartsum8d6", 
			"batstartsum9d6", 
			"slaver", 
			"damagemon", 
			"farsumcom", 
			"onlymnr", 
			"notmnr", 
			"unit",
			"homemon", 
			"homecom", 
			"mon", 
			"com", 
			"natmon", 
			"natcom", 
			"summon", 
			"summonlvl2", 
			"summonlvl3", 
			"summonlvl4",
			"wallcom", 
			"wallunit", 
			"uwwallunit", 
			"uwwallcom", 
			"startcom", 
			"coastcom1", 
			"coastcom2", 
			"addforeignunit",
			"addforeigncom", 
			"rec", 
			"terrainrec", 
			"startscout", 
			"terraincom", 
			"startunittype1", 
			"startunittype2",
			"addrecunit", 
			"addreccom", 
			"uwrec", 
			"uwcom", 
			"coastunit1", 
			"coastunit2", 
			"coastunit3", 
			"landrec", 
			"landcom",
			"hero1",
			"hero2",
			"hero3",
			"hero4",
			"hero5",
			"hero6",
			"hero7",
			"hero8",
			"hero9",
			"hero10",
			"multihero1",
			"multihero2",
			"multihero3",
			"multihero4",
			"multihero5",
			"multihero6",
			"multihero7",
			"defcom1", 
			"defcom2", 
			"defunit1", 
			"defunit1b", 
			"defunit1c", 
			"defunit1d", 
			"defunit2", 
			"defunit2b", 
			"wallcom",
			"addgod", 
			"delgod", 
			"cheapgod20", 
			"cheapgod40", 
			"guardspirit",
			"req_godismnr",
			"req_monster",
			"req_2monsters",
			"req_5monsters",
			"req_nomonster",
			"req_mnr",
			"req_nomnr",
			"req_deadmnr",
			"req_targmnr",
			"req_targnomnr",
			"assassin",
			"assowner",
			"stealthcom",
			"2com",
			"4com",
			"5com",
			"1unit",
			"1d3units",
			"2d3units",
			"3d3units",
			"4d3units",
			"1d6units",
			"2d6units",
			"3d6units",
			"4d6units",
			"5d6units",
			"6d6units",
			"7d6units",
			"8d6units",
			"9d6units",
			"10d6units",
			"11d6units",
			"12d6units",
			"13d6units",
			"14d6units",
			"15d6units",
			"16d6units",
			"killmon",
			"kill2d6mon",
			"killcom",
			"transform",
			"fireboost",
			"airboost",
			"waterboost",
			"earthboost",
			"astralboost",
			"deathboost",
			"natureboost",
			"bloodboost",
			"holyboost",
			"pathboost");

	public static final Set<String> NAMETYPE_CMDS = Sets.newHashSet(
			"nametype", 
			"selectnametype");

	public static final Set<String> SPELL_CMDS = Sets.newHashSet(
			"enchrebate50", 
			"enchrebate25p", 
			"enchrebate50p",
			"onebattlespell", 
			"selectspell", 
			"newspell", 
			"copyspell", 
			"spell", 
			"autospell");

	public static final Set<String> ITEM_CMDS = Sets.newHashSet(
			"startitem", 
			"selectitem", 
			"newitem", 
			"copyitem",
			"item",
			"req_targitem",
			"req_targnoitem");

	public static final Set<String> SITE_CMDS = Sets.newHashSet(
			"onlyatsite", 
			"selectsite", 
			"newsite", 
			"startsite",
			"islandsite",
			"req_nositenbr",
			"addsite",
			"removesite",
			"hiddensite");

	public static final Set<String> NATION_CMDS = Sets.newHashSet(
			"restricted", 
			"notfornation", 
			"nationrebate",
			"nat", 
			"selectnation", 
			"newnation",
			"req_nation",
			"req_nonation",
			"req_fornation",
			"req_notfornation",
			"req_notforally",
			"req_fullowner",
			"req_domowner",
			"req_targowner",
			"nation"
			);

	public static final Set<String> MERCENARY_CMDS = Sets.newHashSet(
			"newmerc");

	public static final Set<String> POPTYPE_CMDS = Sets.newHashSet(
			"selectpoptype");

	public static final Set<String> EVENT_CMDS = Sets.newHashSet(
			"newevent");
	//@formatter:on

	public static final Map<String, EntityType> REFTYPE_BY_NAME = Maps.newHashMap();

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
		EVENT_CMDS.stream().forEach(e -> REFTYPE_BY_NAME.put(e, EVENT));
	}

	public static final Map<String, EntityType> STARTCMD_FOR_NAME = Maps.newHashMap();

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
		STARTCMD_FOR_NAME.put("newevent", EVENT);
	}

}
