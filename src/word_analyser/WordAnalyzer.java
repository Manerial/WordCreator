package word_analyser;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.BasicFunctions;

public class WordAnalyzer {
	private static Random	random						= new Random();
	// The sum of letters of analyzed words in the element file
	private Integer			totalLetters				= 0;
	// The total number of analyzed words in the element file
	private Integer			totalAnalyzedWords			= 0;
	// The frequency of a bigrams of letters and the frequency of the next letters that can follow them
	private JSONObject		trigramsAnalysis	= new JSONObject();
	private String analysisFilePath;
	private String elementsFilePath;
	private String resultsFilePath;

	public String getAnalysisFilePath() {
		return analysisFilePath;
	}

	public void setAnalysisFilePath(String analysisFilePath) {
		this.analysisFilePath = analysisFilePath;
	}

	public String getElementsFilePath() {
		return elementsFilePath;
	}

	public void setElementsFilePath(String elementsFilePath) {
		this.elementsFilePath = elementsFilePath;
	}

	public String getResultsFilePath() {
		return resultsFilePath;
	}

	public void setResultsFilePath(String resultsFilePath) {
		this.resultsFilePath = resultsFilePath;
	}

	public Integer getTotalLetters() {
		return totalLetters;
	}

	public void setTotalLetters(Integer totalLetters) {
		this.totalLetters = totalLetters;
	}

	public void incrementTotalLetters() {
		totalLetters++;
	}

	public Integer getTotalAnalyzedWords() {
		return totalAnalyzedWords;
	}

	public void setTotalAnalyzedWords(Integer totalAnalysedWords) {
		this.totalAnalyzedWords = totalAnalysedWords;
	}

	public void incrementTotalAnalyzedWords() {
		totalAnalyzedWords++;
	}

	public JSONObject getTrigramsAnalysis() {
		return trigramsAnalysis;
	}

	public void setTrigramsAnalysis(JSONObject trigramsAnalysis) {
		this.trigramsAnalysis = trigramsAnalysis;
	}

	/**
	 * Analyze a word : it number of letters and the bigrams used in it
	 * 
	 * @param word : The word to analyze
	 * @throws JSONException : All the JSON exceptions
	 */
	public void analysisWord(String word) throws JSONException {
		String characterAtPosA;
		String characterAtPosB;
		String characterAtPosC;
		for (int charPosition = 0; charPosition < word.length(); charPosition++) {
			int nextCharPosition = charPosition + 1;
			int secondCharPosition = charPosition + 2;
			characterAtPosA = BasicFunctions.getCharAtPosition(word, charPosition);
			characterAtPosB = BasicFunctions.getCharAtPosition(word, nextCharPosition);
			characterAtPosC = BasicFunctions.getCharAtPosition(word, secondCharPosition);
			totalLetters++;
			saveAnalysis(characterAtPosA, characterAtPosB, characterAtPosC, charPosition);
		}
		totalAnalyzedWords++;
	}

	/**
	 * Save the bigrams in the analyzer
	 * 
	 * @param characterAtPosA : The character at the position characterPosition
	 * @param characterAtPosB : The character at the position characterPosition + 1
	 * @param characterAtPosC : The character at the position characterPosition + 2
	 * @param characterPosition : The position of the character characterAtPosA
	 * @throws JSONException : All the JSON exceptions
	 */
	private void saveAnalysis(String characterAtPosA, String characterAtPosB, String characterAtPosC, int characterPosition)
			throws JSONException {
		if (BasicFunctions.isFirstCharacter(characterPosition)) {
			registerTrigram(characterAtPosA, characterAtPosB);
		}

		if(characterAtPosB != "") {
			String bigramme = characterAtPosA + characterAtPosB;
			registerTrigram(bigramme, characterAtPosC);
		}
	}

	/**
	 * Save the frequency analysis of a bigram
	 * 
	 * @param bigram : The bigram to register
	 * @param nextChar : Next possible char of the bigram that will be register
	 * @throws JSONException : All the JSON exceptions
	 */
	private void registerTrigram(String bigram, String nextChar) throws JSONException {
		JSONObject thirdLetter = getFrequencyThirdLetter(bigram);
		incrementFrequencyThirdLetter(nextChar, thirdLetter);
		trigramsAnalysis.put(bigram, thirdLetter);
	}

	/**
	 * Get the third letter of a trigram using the two first letters
	 * 
	 * @param bigram : The two first letters of a trigram
	 * @return the frequency analysis of the third letter of a trigram
	 * @throws JSONException : All the JSON exceptions
	 */
	private JSONObject getFrequencyThirdLetter(String bigram) throws JSONException {
		return (trigramsAnalysis.has(bigram)) ? trigramsAnalysis.getJSONObject(bigram) : new JSONObject();
	}

	/**
	 * Add 1 occurrence to the third letter of the current trigram
	 * 
	 * @param thirdLetter : The third letter of the trigram
	 * @param trigram : The trigram to update
	 * @throws JSONException : All the JSON exceptions
	 */
	private void incrementFrequencyThirdLetter(String thirdLetter, JSONObject trigram) throws JSONException {
		if (trigram.has(thirdLetter)) {
			int nextFrequency = trigram.getInt(thirdLetter) + 1;
			trigram.put(thirdLetter, nextFrequency);
		} else {
			trigram.put(thirdLetter, 1);
		}
	}

	/**
	 * Create a new word using the parameters of the analyzer
	 * 
	 * @param begin : If set, will be the beginning of the new word
	 * @return a new word
	 * @throws JSONException : All the JSON exceptions
	 */
	public String createWord(String begin) throws JSONException {
		String newWord = begin;

		if (newWord.isEmpty()) {
			newWord = getWordBeginning();
			if (newWord.length() == 1) {
				return newWord;
			}
		}

		while (true) {
			JSONObject nextTrigramsPossibilities = getNextTrigramsPossibilities(newWord);
			int randomNextCharRank = getRandomCharRank(nextTrigramsPossibilities);
			int sumOfPreviousCharRank = 0;
			String nextChar = "";
			Iterator<?> iteratorNextChar = nextTrigramsPossibilities.keys();
			while (!BasicFunctions.rankFound(sumOfPreviousCharRank, randomNextCharRank) && iteratorNextChar.hasNext()) {
				nextChar = iteratorNextChar.next().toString();
				sumOfPreviousCharRank += nextTrigramsPossibilities.getInt(nextChar);
			}
			newWord += nextChar;

			if(endOfWord(nextChar)) {
				break;
			}
		}
		return newWord;
	}

	/**
	 * Get a random character rank in all the availables trigrams
	 * 
	 * @param trigrams : all the availables trigrams
	 * @return the random rank of a character
	 * @throws JSONException : All the JSON exceptions
	 */
	private int getRandomCharRank(JSONObject trigrams) throws JSONException {
		int sumOfTrigramsFrequency = getSumOfTrigramsFrequency(trigrams);
		return random.nextInt(sumOfTrigramsFrequency) + 1;
	}

	/**
	 * Check if the character is a word end (empty)
	 * 
	 * @param character : the character to check
	 * @return true if the character is a word end
	 */
	private boolean endOfWord(String character) {
		return character.equals("");
	}

	/**
	 * Get all the next possibles trigrams that match the end of a word
	 * 
	 * @param word : The word to use
	 * @return the next possibles trigrams that can match the end of the word
	 * @throws JSONException : All the JSON exceptions
	 */
	private JSONObject getNextTrigramsPossibilities(String word) throws JSONException {
		int lastStone = word.length();
		int firstStone = (lastStone >= 2) ? lastStone - 2 : 0;
		String lastBigram = word.substring(firstStone, lastStone);
		JSONObject nextTrigrams = trigramsAnalysis.getJSONObject(lastBigram);
		return nextTrigrams;
	}

	/**
	 * Get the two first letters of a word
	 * 
	 * @return The two first letters of a word
	 * @throws JSONException : All the JSON exceptions
	 */
	private String getWordBeginning() throws JSONException {
		int rankToFind = getRandomMonogramRank();
		int sumOfPreviousTrigramsFrequency = 0;
		String singleLetter = "";
		String secondLetter = "";
		JSONObject trigramsTwoLetters = new JSONObject();

		// Get first letter
		Iterator<?> iteratorSingleLetter = trigramsAnalysis.keys();
		while (!BasicFunctions.rankFound(sumOfPreviousTrigramsFrequency, rankToFind) && iteratorSingleLetter.hasNext()) {
			do {
				singleLetter = iteratorSingleLetter.next().toString();
			} while (singleLetter.length() != 1);
			trigramsTwoLetters = trigramsAnalysis.getJSONObject(singleLetter);
			sumOfPreviousTrigramsFrequency += getSumOfTrigramsFrequency(trigramsTwoLetters);
		}

		sumOfPreviousTrigramsFrequency -= getSumOfTrigramsFrequency(trigramsTwoLetters);

		//Get second letter
		Iterator<?> iteratorSecondLetter = trigramsTwoLetters.keys();
		while (!BasicFunctions.rankFound(sumOfPreviousTrigramsFrequency, rankToFind) && iteratorSecondLetter.hasNext()) {
			secondLetter = iteratorSecondLetter.next().toString();
			sumOfPreviousTrigramsFrequency += trigramsTwoLetters.getInt(secondLetter);
		}

		if((singleLetter + secondLetter).equals("")) {
			System.out.println(rankToFind);
		}
		return singleLetter + secondLetter;
	}

	/**
	 * Get a random number that symbolizes the rank of a monogram
	 * (bigram of 1 letter)
	 * 
	 * @return the random rank of a monogram
	 */
	private int getRandomMonogramRank() {
		return random.nextInt(totalAnalyzedWords) + 1;
	}

	/**
	 * Get the number of appearance of a bigram
	 * 
	 * @param trigrams : The bigram we need to get the frequency
	 * @return The frequency of appearance of a bigram
	 * @throws JSONException : All the JSON exceptions
	 */
	private int getSumOfTrigramsFrequency(JSONObject trigrams) throws JSONException {
		int sumFrequencies = 0;
		Iterator<?> thirdLetters = trigrams.keys();
		while (thirdLetters.hasNext()) {
			String thirdLetter = thirdLetters.next().toString();
			sumFrequencies += trigrams.getInt(thirdLetter);
		}
		return sumFrequencies;
	}
}