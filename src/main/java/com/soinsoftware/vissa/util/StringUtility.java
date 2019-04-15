package com.soinsoftware.vissa.util;

public class StringUtility {

	/**
	 * Metodo para concatenar nombre y apellido
	 * 
	 * @param name
	 * @param lastName
	 * @return
	 */
	public static String concatName(String name, String lastName) {
		return name.concat(" ").concat(lastName);
	}

	/**
	 * Metodo para validar si una cadena de texto es nula o vacía
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String string) {
		if (string == null || string.length() == 0)
			return true;

		int l = string.length();
		for (int i = 0; i < l; i++) {
			if (!StringUtility.isWhitespace(string.codePointAt(i)))
				return false;
		}
		return true;
	}

	public static boolean isWhitespace(int c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
	}

	public static String deleteWhitespace(String string) {
		return string.trim().replaceAll(" ", "");
	}

}
