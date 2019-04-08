package word_analyser;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import utilities.WordsFilesManager;

public class WordAnalyserManager {

	/**
	 * Analysis a file containing words and save the result of the analysis in a file
	 * 
	 * @param elementsFile : The name of the file that contains the words to be analyzed
	 * @param analysisFile : The name of the file where the analysis result will be stored
	 */
	public static void analysisElementsFileAndRegisterResult(String elementsFile, String analysisFile) {
		WordAnalyzer analyzer = new WordAnalyzer();
		try {
			BufferedReader br = WordsFilesManager.readElementsFile(elementsFile);
			
			String word;
			while ((word = br.readLine()) != null) {
				analyzer.analysisWord(word);
			}
			br.close();
			WordsFilesManager.saveAnalysisFile(analysisFile, analyzer);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * Create a certain amount of words using the analysis of a list of words
	 * 
	 * @param numberOfWords : Number of word to create
	 * @param beginning : If not empty, will be the begin of the new word
	 * @param analysisFile : The name of the analysis file to use to create words
	 * @param resultFile : The name of the file where the words creation will be stored
	 * @throws JSONException : for JSON error
	 * @throws IOException : for file management errors
	 */
	public static void createWordList(int numberOfWords, String beginning, String analysisFile, String resultFile) throws JSONException, IOException {
		WordAnalyzer analyzer = new WordAnalyzer();
		WordsFilesManager.parseAnalysisFile(analysisFile, analyzer);
		List<String> createdWordList = new ArrayList<>();
		for(int i = 0; i < numberOfWords; i++) {
			String createdWord;
			do {
				createdWord = analyzer.createWord(beginning);
			} while (createdWordList.contains(createdWord));
			createdWordList.add(createdWord);
		}
		WordsFilesManager.printStringListInResultFile(resultFile, createdWordList);
	}

	/**
	 * Create a certain amount of words with a fixed length using the analysis of a list of words
	 * 
	 * @param numberOfWords : Number of word to create
	 * @param beginning : If not empty, will be the begin of the new word
	 * @param analysisFile : The name of the analysis file to use to create words
	 * @param resultFile : The name of the file where the words creation will be stored
	 * @param wordLength : The length of the created words
	 * @throws JSONException : for JSON error
	 * @throws IOException : for file management errors
	 */
	public static void createWordListFixLength(int numberOfWords, String beginning, String analysisFile, String resultFile, int wordLength) throws JSONException, IOException {
		WordAnalyzer analyzer = new WordAnalyzer();
		WordsFilesManager.parseAnalysisFile(analysisFile, analyzer);
		List<String> createdWordList = new ArrayList<>();
		for(int i = 0; i < numberOfWords; i++) {
			String createdWord;
			do {
				createdWord = analyzer.createWord(beginning);
			} while (createdWordList.contains(createdWord) || createdWord.length() != wordLength);
			createdWordList.add(createdWord);
		}
		WordsFilesManager.printStringListInResultFile(resultFile, createdWordList);
	}
}