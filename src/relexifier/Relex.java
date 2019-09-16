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

	public Relex(WordAnalyzer analyzer) {
		this.analyzer = analyzer;
	}
	
	/**
	 * Create a relex of words (relex = a list of element words will match a list of created words)
	 * 
	 * @param elementsFile : The name of the elements file that will be used as source of words
	 * @param analysisFilePath : The name of the analysis file that will be used to create new words
	 * @param relexFile : The name of the destination file of the relex
	 * @throws JSONException : for JSON error
	 * @throws IOException : for file management errors
	 */
	public void createRelex() throws JSONException, IOException {
		WordsFilesManager.parseAnalysisFile(analyzer);
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
		WordsFilesManager.printStringListInResultFile(analyzer, relex);
	}

	/**
	 * check the created word fits the source word length
	 * 
	 * @param sourceWord : the source word
	 * @param createdWord : the new word
	 * @return
	 */
	private boolean checkWordLength(String sourceWord, String createdWord) {
		return createdWord.length() >= sourceWord.length() - 2 && createdWord.length() <= sourceWord.length() + 2;
	}
	
	/**
	 * Use a relex to relexify a text
	 * 
	 * @param textPath : The path to the text to relexify
	 * @param relexPath : The path to the relex to use
	 * @throws IOException
	 * @throws JSONException
	 */
	public void relexifyText() throws IOException, JSONException {
		JSONObject relex = readRelex(analyzer.getResultsFilePath());
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
	 * @param relex : the relex to use
	 * @param word : the word to convert
	 * @return
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
	 * @param relexPath : The path to the relex to use
	 * @return a JSONObject that contains the relex
	 * @throws IOException
	 * @throws JSONException
	 */
	private JSONObject readRelex(String relexPath) throws IOException, JSONException {
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
