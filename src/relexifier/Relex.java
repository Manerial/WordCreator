package relexifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.BasicFunctions;
import utilities.WordsFilesManager;
import word_analyser.WordAnalyzer;

public class Relex {
	private WordAnalyzer analyzer;

	public Relex(WordAnalyzer analyzer) throws JSONException, IOException {
		setWordAnalyzer(analyzer);
	}

	public void setWordAnalyzer(WordAnalyzer analyzer) throws JSONException, IOException {
		WordsFilesManager.parseAnalysisFile(analyzer);
		this.analyzer = analyzer;
	}
	
	/**
	 * Create a relex of words (relex = a list of element words will match a list of created words)
	 * 
	 * @throws JSONException : for JSON error
	 * @throws IOException : for file management errors
	 */
	public void createRelex() throws JSONException, IOException {
		List<String> sourceWordList = WordsFilesManager.parseElementsFileInList(analyzer);
		List<String> relex = new ArrayList<>();
		List<String> usedWords = new ArrayList<>();
		for(String sourceWord : sourceWordList) {
			String createdWord;
			do {
				createdWord = analyzer.createWord("");
			} while (!checkWordLength(sourceWord, createdWord) || BasicFunctions.isWordUsed(usedWords, createdWord));
			usedWords.add(createdWord);
			relex.add(sourceWord + "\t:\t" + createdWord);
		}
		WordsFilesManager.printStringListInFile(analyzer, relex);
	}

	/**
	 * check the created word fits the source word length
	 * 
	 * @param sourceWord : the source word
	 * @param createdWord : the new word
	 * @return true if the created word matches the source one
	 */
	private boolean checkWordLength(String sourceWord, String createdWord) {
		return createdWord.length() >= sourceWord.length() - 2 && createdWord.length() <= sourceWord.length() + 2;
	}
	
	/**
	 * Use a relex to relexify a text
	 * 
	 * @throws IOException : All the IO exceptions
	 * @throws JSONException : All the JSON exceptions
	 */
	public void relexifyText() throws IOException, JSONException {
		JSONObject relex = readRelex();
		BufferedReader br = WordsFilesManager.readElementsFile(analyzer);
		String line;
	    while ((line = br.readLine()) != null) {
	    	for (String baseWord : line.split(" ")) {
	    		baseWord = baseWord.replaceAll(",", "").toLowerCase();
	    		String word = relexifyWord(relex, baseWord);
	    		System.out.print(word + " ");
	    	}
	    	System.out.println();
	    }
	}
	
	/**
	 * Convert a word into an other from the relex
	 * 
	 * @param relex : the JSONObject relex to use
	 * @param word : the word to convert
	 * @return the relexified word
	 */
	public String relexifyWord(JSONObject relex, String word) {
		String relexWord = word;
		try {
			relexWord = relex.getString(word);
		} catch (JSONException e) {}
		return relexWord;
	}
	
	/**
	 * Open a relex and store it in a JSONObject
	 * 
	 * @return a JSONObject that contains the relex
	 * @throws IOException : All the IO exceptions
	 * @throws JSONException : All the JSON exceptions
	 */
	private JSONObject readRelex() throws IOException, JSONException {
		JSONObject relex = new JSONObject();
		BufferedReader br = WordsFilesManager.readResultsFile(analyzer);
		String line;
		while ((line = br.readLine()) != null) {
			String baseWord = line.split("\t:\t")[0];
			String relexWord = line.split("\t:\t")[1];
			relex.put(baseWord, relexWord);
		}
		return relex;
	}
}
