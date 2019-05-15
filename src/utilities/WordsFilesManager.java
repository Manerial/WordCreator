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
	 * @param analysisFile : The name of the analysis file
	 * @param analyzer : The analyzer object to feed with the analysis file data
	 */
	public static void parseAnalysisFile(String analysisFile, WordAnalyzer analyzer) {
		try {
			BufferedReader br = readFile(ANALYSIS_FOLDER + analysisFile);

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
	public static List<String> parseElementsFileInList(String elementsFile) throws IOException, JSONException {
		BufferedReader br = readElementsFile(elementsFile);
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
	public static void printStringListInResultFile(String resultFile, List<String> newWordList) throws IOException, JSONException {
		PrintWriter outputFile = writeFile(RESULT_FOLDER + resultFile);
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
	public static void saveAnalysisFile(String analysisFile, WordAnalyzer analyzer) throws IOException {
		PrintWriter outputFile = writeFile(ANALYSIS_FOLDER + analysisFile);
		outputFile.println(analyzer.getTotalLetters());
		outputFile.println(analyzer.getTotalAnalyzedWords());
		outputFile.println(analyzer.getFrequencyBigramsAnalysis());
		outputFile.close();
	}

	public static BufferedReader readElementsFile(String elementsFile) throws FileNotFoundException {
		return readFile(ELEMENTS_FOLDER + elementsFile);
	}

	public static BufferedReader readResultFile(String resultFile) throws FileNotFoundException {
		return readFile(RESULT_FOLDER + resultFile);
	}
	
	private static BufferedReader readFile(String path) throws FileNotFoundException {
		InputStream ips = new FileInputStream(path);
		InputStreamReader ipsr = new InputStreamReader(ips);
		return new BufferedReader(ipsr);
	}
	
	private static PrintWriter writeFile(String path) throws IOException {
		FileWriter fw = new FileWriter(path);
		BufferedWriter bw = new BufferedWriter(fw);
		return new PrintWriter(bw);
	}
}
