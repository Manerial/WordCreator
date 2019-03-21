package utilities;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class WordAnalyserManager {
	private static String RESSOURCE_PATH = System.getProperty("user.dir") + "\\ressources\\";
	private static String ELEMENTS_FOLDER = RESSOURCE_PATH + "elements\\";
	private static String ANALYSIS_FOLDER = RESSOURCE_PATH + "analysis\\";
	private static String RESULT_FOLDER = RESSOURCE_PATH + "result\\";

	/**
	 * Analysis a file containing words and save the result of the analysis in a file
	 * 
	 * @param elementsFile : The name of the file that contains the words to be analyzed
	 * @param analysisFile : The name of the file where the analysis result will be stored
	 */
	public static void analysisElementsFileAndRegisterResult(String elementsFile, String analysisFile) {
		WordAnalyzer analyzer = new WordAnalyzer();
		try {
			InputStream ips = new FileInputStream(ELEMENTS_FOLDER + elementsFile);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String word;
			while ((word = br.readLine()) != null) {
				analyzer.analysisWord(word);
			}
			br.close();
			saveAnalysisFile(analysisFile, analyzer);
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
		parseAnalysisFile(analysisFile, analyzer);
		List<String> createdWordList = new ArrayList<>();
		for(int i = 0; i < numberOfWords; i++) {
			String createdWord;
			do {
				createdWord = analyzer.createWord(beginning);
			} while (createdWordList.contains(createdWord));
			createdWordList.add(createdWord);
		}
		WordAnalyserManager.printWordListInResultFile(resultFile, createdWordList);
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
		parseAnalysisFile(analysisFile, analyzer);
		List<String> createdWordList = new ArrayList<>();
		for(int i = 0; i < numberOfWords; i++) {
			String createdWord;
			do {
				createdWord = analyzer.createWord(beginning);
			} while (createdWordList.contains(createdWord) || createdWord.length() != wordLength);
			createdWordList.add(createdWord);
		}
		WordAnalyserManager.printWordListInResultFile(resultFile, createdWordList);
	}

	/**
	 * Create a relex of words (relex = a list of element words will match a list of created words)
	 * 
	 * @param elementsFile : The name of the elements file that will be used as source of words
	 * @param analysisFile : The name of the analysis file that will be used to create new words
	 * @param relexFile : The name of the destination file of the relex
	 * @throws JSONException : for JSON error
	 * @throws IOException : for file management errors
	 */
	public static void createRelex(String elementsFile, String analysisFile, String relexFile) throws JSONException, IOException {
		WordAnalyzer analyzer = new WordAnalyzer();
		parseAnalysisFile(analysisFile, analyzer);
		List<String> sourceWordList = parseElementsFileInList(elementsFile);
		List<String> createdWordList = new ArrayList<>();
		List<String> alreadyUsedWords = new ArrayList<>();
		for(String sourceWord : sourceWordList) {
			String createdWord;
			do {
				createdWord = analyzer.createWord("");
			} while ((createdWord.length() < sourceWord.length() - 2 || createdWord.length() > sourceWord.length() + 2) || alreadyUsedWords.contains(createdWord));
			alreadyUsedWords.add(createdWord);
			createdWordList.add(sourceWord + "\t:\t" + createdWord);
		}
		WordAnalyserManager.printWordListInResultFile(relexFile, createdWordList);
	}

	/**
	 * Parse an analysis file to feed an analyzer
	 * 
	 * @param analysisFile : The name of the analysis file
	 * @param analyzer : The analyzer object to feed with the analysis file data
	 */
	private static void parseAnalysisFile(String analysisFile, WordAnalyzer analyzer) {
		try {
			InputStream ips = new FileInputStream(ANALYSIS_FOLDER + analysisFile);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);

			String STotalLetters = br.readLine();
			analyzer.setTotalLetters(Integer.parseInt(STotalLetters));

			String STotalAnalysedWords = br.readLine();
			analyzer.setTotalAnalyzedWords(Integer.parseInt(STotalAnalysedWords));

			String SFrequencyBigramsAnalysis = br.readLine();
			analyzer.setFrequencyBigramsAnalysis(new JSONObject(SFrequencyBigramsAnalysis));
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Save the content of an element file and return a list
	 * 
	 * @param elementsFile : The name of the file containing the words to get
	 * @return A list of words contained in the elementFile
	 * @throws IOException : for file management errors
	 * @throws JSONException : for JSON error
	 */
	private static List<String> parseElementsFileInList(String elementsFile) throws IOException, JSONException {
		InputStream ips = new FileInputStream(ELEMENTS_FOLDER + elementsFile);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String line;
		ArrayList<String> result = new ArrayList<String>();
		while ((line = br.readLine()) != null) {
			result.add(line);
		}
		br.close();
		return result;
	}

	/**
	 * Save a list of words in a file
	 * 
	 * @param resultFile : The name of the file that will sore the words list
	 * @param newWordList : The list of words to save
	 * @throws IOException : for file management errors
	 * @throws JSONException : for JSON error
	 */
	private static void printWordListInResultFile(String resultFile, List<String> newWordList) throws IOException, JSONException {
		FileWriter fw = new FileWriter(RESULT_FOLDER + resultFile);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter outputFile = new PrintWriter(bw);
		for (String newWord : newWordList) {
			outputFile.println(newWord);
		}
		outputFile.close();
	}

	/**
	 * Save the parameters of an analyzer in a file
	 * 
	 * @param analysisFile : The name of the analysis file that will store the analyzer parameters
	 * @param analyzer : The analyzer to save
	 * @throws IOException : for file management errors
	 */
	private static void saveAnalysisFile(String analysisFile, WordAnalyzer analyzer) throws IOException {
		FileWriter fw = new FileWriter(ANALYSIS_FOLDER + analysisFile);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter outputFile = new PrintWriter(bw);
		outputFile.println(analyzer.getTotalLetters());
		outputFile.println(analyzer.getTotalAnalyzedWords());
		outputFile.println(analyzer.getFrequencyBigramsAnalysis());
		outputFile.close();
	}
}