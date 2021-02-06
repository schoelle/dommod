package de.fams.dommod;

public class Reference {

	public final RefType type;
	public final Integer numref;
	public final String strref;
	
	public Reference(RefType type, Integer numref, String strref) {
		this.type = type;
		this.numref = numref;
		this.strref = strref;
	}
	
	@Override
	public String toString() {
		if (numref != null) {
			return "->" + type.toString() + ":" + numref;
		} 
		return "->" + type.toString() + ":\"" + strref + "\"";
	}
	
	public boolean referencesBuiltIn() {
		if (numref == null) {
			return false;
		}
		if (numref >= type.minId && numref < type.minModId) {
			return true;
		}
		return false;
	}
	
}
