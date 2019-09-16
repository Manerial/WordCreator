package word_analyser;
import java.util.HashMap;
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
	// The frequency of the bigrams of letters and the frequency of the next letters that can follow them
	private JSONObject		frequencyBigramsAnalysis	= new JSONObject();
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

	public JSONObject getFrequencyBigramsAnalysis() {
		return frequencyBigramsAnalysis;
	}

	public void setFrequencyBigramsAnalysis(JSONObject frequencyBigramsAnalysis) {
		this.frequencyBigramsAnalysis = frequencyBigramsAnalysis;
	}


	/**
	 * Analyze a word : it number of letters and the bigrams used in it
	 * 
	 * @param word : The word to analyze
	 * @throws JSONException : for JSON error
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


	private void saveAnalysis(String characterAtPosA, String characterAtPosB, String characterAtPosC, int characterPosition)
			throws JSONException {
		if (BasicFunctions.isFirstCharacter(characterPosition)) {
			saveBigramNextChar(characterAtPosA, characterAtPosB);
		}
		
		if(characterAtPosB != "") {
			String bigramme = characterAtPosA + characterAtPosB;
			saveBigramNextChar(bigramme, characterAtPosC);
		}
	}


	/**
	 * Save the frequency analysis of a bigram
	 * 
	 * @param bigramme : The bigram to register
	 * @param nextChar : Next possible char of the bigram that will be register
	 * @throws JSONException : for JSON error
	 */
	public void saveBigramNextChar(String bigramme, String nextChar) throws JSONException {
		if (frequencyBigramsAnalysis.has(bigramme)) {
			JSONObject analysebigrammeDiag = frequencyBigramsAnalysis.getJSONObject(bigramme);
			if (analysebigrammeDiag.has(nextChar)) {
				analysebigrammeDiag.put(nextChar, analysebigrammeDiag.getInt(nextChar) + 1);
			} else {
				analysebigrammeDiag.put(nextChar, 1);
			}
			frequencyBigramsAnalysis.put(bigramme, analysebigrammeDiag);
		} else {
			HashMap<String, Integer> analysebigrammeDiag = new HashMap<String, Integer>();
			analysebigrammeDiag.put(nextChar, 1);
			frequencyBigramsAnalysis.put(bigramme, analysebigrammeDiag);
		}
	}

	/**
	 * 
	 * Create a new word using the parameters of the analyzer
	 * 
	 * @param begin : If set, will be the beginning of the new word
	 * @return A new word
	 * @throws JSONException : for JSON error
	 */
	public String createWord(String begin) throws JSONException {
		String newWord = begin;
		boolean end = false;
		
		if (newWord.isEmpty()) {
			newWord = getBeginOfWord();
			if (newWord.length() == 1) {
				return newWord;
			}
		}

		while (!end) {
			int minLengthOfWord = (newWord.length() >= 2) ? newWord.length() - 2 : 0;
			String lastBigram = newWord.substring(minLengthOfWord, newWord.length());
			JSONObject lastBigramAnalysis = frequencyBigramsAnalysis.getJSONObject(lastBigram);
			int sumOfChildsBigramFreq = getBigramFrequencyNumber(lastBigramAnalysis);
			int nextCharRank = random.nextInt(sumOfChildsBigramFreq) + 1;
			int sumOfPrevBigramsFreq = 0;
			Iterator<?> iter = lastBigramAnalysis.keys();
			while (sumOfPrevBigramsFreq < nextCharRank && iter.hasNext()) {
				String nextChar = iter.next().toString();
				sumOfPrevBigramsFreq += lastBigramAnalysis.getInt(nextChar);
				if (sumOfPrevBigramsFreq >= nextCharRank) {
					newWord += nextChar;
					if (nextChar.equals("")) {
						end = true;
					}
				}
			}
		}
		return newWord;
	}

	/**
	 * Create the beginning of a word (the first bigram)
	 * 
	 * @return The beginning of a word
	 * @throws JSONException : for JSON error
	 */
	private String getBeginOfWord() throws JSONException {
		String newWord = "";
		int bigramRank = random.nextInt(totalAnalyzedWords);
		int sumOfPrevBigramsFreq = 0;
		boolean find = false;

		Iterator<?> iter = frequencyBigramsAnalysis.keys();
		while (!find && iter.hasNext()) {
			String firstChar = iter.next().toString();
			while (firstChar.length() != 1) {
				firstChar = iter.next().toString();
			}
			Iterator<?> iter2 = frequencyBigramsAnalysis.getJSONObject(firstChar).keys();
			while (!find && iter2.hasNext()) {
				String secondChar = iter2.next().toString();
				sumOfPrevBigramsFreq += frequencyBigramsAnalysis.getJSONObject(firstChar).getInt(secondChar);
				if (sumOfPrevBigramsFreq >= bigramRank) {
					newWord = firstChar + secondChar;
					find = true;
				}
			}
		}
		return newWord;
	}

	/**
	 * Get the number of appearance of a bigram
	 * 
	 * @param bigram : The bigram we need to get the frequency
	 * @return The frequency of appearance of a bigram
	 * @throws JSONException : for JSON error
	 */
	private int getBigramFrequencyNumber(JSONObject bigram) throws JSONException {
		int sumOfFrequencies = 0;
		Iterator<?> iter = bigram.keys();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			sumOfFrequencies += bigram.getInt(key);
		}
		return sumOfFrequencies;
	}
}