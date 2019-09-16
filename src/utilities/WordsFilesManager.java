package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import word_analyser.WordAnalyzer;

public class WordsFilesManager {
	private static String RESOURCE_PATH = System.getProperty("user.dir") + "\\resources\\";
	private static String ELEMENTS_FOLDER = RESOURCE_PATH + "elements\\";
	private static String ANALYSIS_FOLDER = RESOURCE_PATH + "analysis\\";
	private static String RESULT_FOLDER = RESOURCE_PATH + "result\\";

	/**
	 * Parse an analysis file to feed an analyzer
	 * 
	 * @param analyzer : The analyzer object to feed with the analysis file data
	 */
	public static void parseAnalysisFile(WordAnalyzer analyzer) {
		try {
			BufferedReader br = readFile(ANALYSIS_FOLDER + analyzer.getAnalysisFilePath());

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
	 * Save the parameters of an analyzer in a file
	 * 
	 * @param analysisFile : The name of the analysis file that will store the analyzer parameters
	 * @param analyzer : The analyzer to save
	 * @throws IOException : for file management errors
	 */
	public static void saveAnalysisFile(WordAnalyzer analyzer) throws IOException {
		PrintWriter outputFile = writeFile(ANALYSIS_FOLDER + analyzer.getAnalysisFilePath());
		outputFile.println(analyzer.getTotalLetters());
		outputFile.println(analyzer.getTotalAnalyzedWords());
		outputFile.println(analyzer.getFrequencyBigramsAnalysis());
		outputFile.close();
	}
	
	/**
	 * Save the content of a result file and return a list
	 * 
	 * @param fileName : The name of the file containing the words to get
	 * @return A list of words contained in the elementFile
	 * @throws IOException : for file management errors
	 * @throws JSONException : for JSON error
	 */
	public static List<String> parseElementsFileInList(WordAnalyzer analyzer) throws IOException, JSONException {
		BufferedReader br = readElementsFile(analyzer);
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
	 * @param fileName : The name of the file that will sore the words list
	 * @param newWordList : The list of words to save
	 * @throws IOException : for file management errors
	 * @throws JSONException : for JSON error
	 */
	public static void printStringListInResultFile(WordAnalyzer analyzer, List<String> newWordList) throws IOException, JSONException {
		PrintWriter outputFile = writeFile(RESULT_FOLDER + analyzer.getResultsFilePath());
		for (String newWord : newWordList) {
			outputFile.println(newWord);
		}
		outputFile.close();
	}

	/**
	 * Return a BufferReader using the ELEMENTS path
	 * 
	 * @param fileName : the name of the file to read
	 * @return a BufferReader configured with the ELEMENT path
	 * @throws FileNotFoundException
	 */
	public static BufferedReader readElementsFile(WordAnalyzer analyzer) throws FileNotFoundException {
		return readFile(ELEMENTS_FOLDER + analyzer.getElementsFilePath());
	}
	
	/**
	 * Return a BufferReader using the RESULT path
	 * 
	 * @param fileName : the name of the file to read
	 * @return a BufferReader configured with the RESULT path
	 * @throws FileNotFoundException
	 */
	public static BufferedReader readResultsFile(WordAnalyzer analyzer) throws FileNotFoundException {
		return readFile(RESULT_FOLDER + analyzer.getResultsFilePath());
	}
	
	private static BufferedReader readFile(String filePath) throws FileNotFoundException {
		InputStream ips = new FileInputStream(filePath);
		InputStreamReader ipsr = new InputStreamReader(ips);
		return new BufferedReader(ipsr);
	}
	
	private static PrintWriter writeFile(String filePath) throws IOException {
		FileWriter fw = new FileWriter(filePath);
		BufferedWriter bw = new BufferedWriter(fw);
		return new PrintWriter(bw);
	}
}
