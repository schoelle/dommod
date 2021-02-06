package de.fams.dommod;

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
}
