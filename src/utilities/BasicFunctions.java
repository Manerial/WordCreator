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

	/**
	 * Check if the current character is the first one
	 * 
	 * @param characterPosition : the current position of the character
	 * @return true if the character is the first one
	 */
	public static boolean isFirstCharacter(int characterPosition) {
		return characterPosition == 0;
	}

	/**
	 * Get the character at the current position.
	 * If the character's position doesn't exist, return an empty string.
	 * 
	 * @param word : the word to get the character
	 * @param charPosition : the position of the character to get
	 * @return
	 */
	public static String getCharAtPosition(String word, int charPosition) {
		return (charPosition < word.length()) ? Character.toString(word.charAt(charPosition)) : "";
	}

	/**
	 * Check if the current rank matches the searched rank
	 * 
	 * @param currentRank : The current rank
	 * @param searchedRank : The rank we look for
	 * @return true if the current rank is greater than the searched rank
	 */
	public static boolean rankFound(int currentRank, int searchedRank) {
		return currentRank >= searchedRank;
	}
}
