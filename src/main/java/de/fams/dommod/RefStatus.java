package de.fams.dommod;

/**
 * Status of a reference
 */
public enum RefStatus {
	BUILT_IN(null),  // Referencing an entity with the Dom 5 built-in space (as defined by the mod manual)
	INTERNAL(null),  // Referencing an entity that is defined within the same mod
	EXTERNAL(null),  // Referencing an entity that must be made available though some other mod
	INVALID("Reference number is outside the permitted range"),
	DUPLICATE("String reference resolves to multiple entities"),
	NOT_FOUND("String reference cannot be resolved");

	private final String badText;
	
	private RefStatus(String badText) {
		this.badText = badText;
	}
	
	public String getBadText() {
		return badText;
	}
	
	public boolean isBad() {
		return badText != null;
	}
	
}

