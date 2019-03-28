package word_analyser;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class WordAnalyzer {
	private static Random	random						= new Random();
	// The sum of letters of analyzed words in the element file
	private Integer			totalLetters				= 0;
	// The total number of analyzed words in the element file
	private Integer			totalAnalyzedWords			= 0;
	// The frequency of the bigrams of letters and the frequency of the next letters that can follow them
	private JSONObject		frequencyBigramsAnalysis	= new JSONObject();

	public Integer getTotalLetters() {
		return totalLetters;
	}

	public void setTotalLetters(Integer totalLetters) {
		this.totalLetters = totalLetters;
	}

	public Integer getTotalAnalyzedWords() {
		return totalAnalyzedWords;
	}

	public void setTotalAnalyzedWords(Integer totalAnalysedWords) {
		this.totalAnalyzedWords = totalAnalysedWords;
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
		String characterI0;
		String characterI1;
		String characterI2;
		for (int charPlace = 0; charPlace < word.length(); charPlace++) {
			int nextCharPlace = charPlace + 1;
			int secondCharPlace = charPlace + 2;
			totalLetters++;
			characterI0 = Character.toString(word.charAt(charPlace));
			characterI1 = (nextCharPlace < word.length()) ? Character.toString(word.charAt(nextCharPlace)) : "";
			characterI2 = (secondCharPlace < word.length()) ? Character.toString(word.charAt(secondCharPlace)) : "";

			if (!characterI1.equals("")) {
				String bigramme = characterI0 + characterI1;
				saveBigramNextChar(bigramme, characterI2);
			}
			
			if (charPlace == 0) {
				String bigramme = "" + characterI0;
				saveBigramNextChar(bigramme, characterI1);
			}
		}
		totalAnalyzedWords++;
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
			String lastBigram = newWord.substring(newWord.length() - 2, newWord.length());
			JSONObject lastBigramAnalysis = frequencyBigramsAnalysis.getJSONObject(lastBigram);
			int sumOfChildsBigramFreq = getSumOfBigramNextCharFrequency(lastBigramAnalysis);
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
	 * Save the frequency analysis of a bigram
	 * 
	 * @param bigramme : The bigram to register
	 * @param nextChar : Next possible char of the bigram that will be register
	 * @throws JSONException : for JSON error
	 */
	private void saveBigramNextChar(String bigramme, String nextChar) throws JSONException {
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
	 * Get the frequency of appearance of a bigram
	 * 
	 * @param bigram : The bigram we need to get the frequency
	 * @return The frequency of appearance of a bigram
	 * @throws JSONException : for JSON error
	 */
	private static int getSumOfBigramNextCharFrequency(JSONObject bigram) throws JSONException {
		int sumOfFrequencies = 0;
		Iterator<?> iter = bigram.keys();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			sumOfFrequencies += bigram.getInt(key);
		}
		return sumOfFrequencies;
	}
}