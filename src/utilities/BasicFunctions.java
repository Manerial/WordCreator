package utilities;

import java.util.List;

public class BasicFunctions {


	/**
	 * Check if the word is already used
	 * 
	 * @param usedWords : the list of used words
	 * @param createdWord : the word to check
	 * @return true if the word is not already used
	 */
	public static boolean isWordUsed(List<String> usedWords, String createdWord) {
		return usedWords.contains(createdWord);
	}


	public static boolean isFirstCharacter(int characterPosition) {
		return characterPosition == 0;
	}

	public static String getCharAtPosition(String word, int charPosition) {
		return (charPosition < word.length()) ? Character.toString(word.charAt(charPosition)) : "";
	}
}
