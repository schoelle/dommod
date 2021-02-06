package de.fams.dommod;

public class Print {

	public static void info(String format, Object...args) {
		System.out.println("I: " + String.format(format, args));
	}
	
	public static void error(String format, Object...args) {
		System.out.println("E: " + String.format(format, args));
	}
	
	public static void warn(String format, Object...args) {
		System.out.println("W: " + String.format(format, args));
	}
	
	public static void print(String format, Object...args) {
		System.out.println(String.format(format, args));
	}
	
}
