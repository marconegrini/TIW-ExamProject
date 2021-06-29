package it.polimi.tiw.projects.beans;

public enum Order {

	ASC, DESC;

	public static Order fromString(String s) {
		return switch (s.toUpperCase()) {
			case "ASC" -> ASC;
			case "DESC" -> DESC;
			default -> throw new IllegalArgumentException();
		};
	}

}
