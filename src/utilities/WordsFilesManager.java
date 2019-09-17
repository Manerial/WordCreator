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
	 * @throws IOException : All the IO exceptions
	 * @throws JSONException : All the JSON exceptions
	 */
	public static void parseAnalysisFile(WordAnalyzer analyzer) throws JSONException, IOException {
		BufferedReader br = readFile(ANALYSIS_FOLDER + analyzer.getAnalysisFilePath());
		analyzer.setTotalLetters(Integer.parseInt(br.readLine()));
		analyzer.setTotalAnalyzedWords(Integer.parseInt(br.readLine()));
		analyzer.setTrigramsAnalysis(new JSONObject(br.readLine()));
		br.close();
	}

	/**
	 * Save the parameters of an analyzer in a file
	 * 
	 * @param analyzer : The analyzer to save
	 * @throws IOException : All the IO exceptions
	 */
	public static void saveAnalysisFile(WordAnalyzer analyzer) throws IOException {
		PrintWriter outputFile = writeFile(ANALYSIS_FOLDER + analyzer.getAnalysisFilePath());
		outputFile.println(analyzer.getTotalLetters());
		outputFile.println(analyzer.getTotalAnalyzedWords());
		outputFile.println(analyzer.getTrigramsAnalysis());
		outputFile.close();
	}

	/**
	 * Read the content of a ELEMENT file and return a list
	 * 
	 * @param analyzer : The analyzer that contains the ELEMENT file name
	 * @return A list of words contained in the elementFile
	 * @throws IOException : All the IO exceptions
	 */
	public static List<String> parseElementsFileInList(WordAnalyzer analyzer) throws IOException {
		ArrayList<String> result = new ArrayList<String>();
		BufferedReader br = readElementsFile(analyzer);
		String line;
		while ((line = br.readLine()) != null) {
			result.add(line);
		}
		br.close();
		return result;
	}

	/**
	 * Save a list of words in a RESULT file
	 * 
	 * @param analyzer : The analyzer that contains the RESULT file name
	 * @param newWordList : The list of words to save
	 * @throws IOException : All the IO exceptions
	 * @throws JSONException : All the JSON exceptions
	 */
	public static void printListInResultFile(WordAnalyzer analyzer, List<String> newWordList) throws IOException, JSONException {
		PrintWriter resultFile = writeFile(RESULT_FOLDER + analyzer.getResultsFilePath());
		for (String newWord : newWordList) {
			resultFile.println(newWord);
		}
		resultFile.close();
	}

	/**
	 * Return a BufferReader using the ELEMENTS path
	 * 
	 * @param analyzer : The analyzer that contains the element file name
	 * @return a BufferReader configured with the ELEMENT path
	 * @throws FileNotFoundException : All the File Not Found exceptions
	 */
	public static BufferedReader readElementsFile(WordAnalyzer analyzer) throws FileNotFoundException {
		return readFile(ELEMENTS_FOLDER + analyzer.getElementsFilePath());
	}

	/**
	 * Return a BufferReader using the RESULT path
	 * 
	 * @param analyzer : The analyzer that contains the result file name
	 * @return a BufferReader configured with the RESULT path
	 * @throws FileNotFoundException : All the File Not Found exceptions
	 */
	public static BufferedReader readResultsFile(WordAnalyzer analyzer) throws FileNotFoundException {
		return readFile(RESULT_FOLDER + analyzer.getResultsFilePath());
	}

	/**
	 * Return a BufferedReader to read a file
	 * 
	 * @param filePath : the file path to read
	 * @return a BufferedReader with the path of the file to read
	 * @throws FileNotFoundException : All the File Not Found exceptions
	 */
	private static BufferedReader readFile(String filePath) throws FileNotFoundException {
		InputStream ips = new FileInputStream(filePath);
		InputStreamReader ipsr = new InputStreamReader(ips);
		return new BufferedReader(ipsr);
	}

	/**
	 * Return a BufferedWriter to write into a file
	 * 
	 * @param filePath : the file path to write
	 * @return a BufferedWriter with the path of the file to write
	 * @throws IOException : All the IO exceptions
	 */
	private static PrintWriter writeFile(String filePath) throws IOException {
		FileWriter fw = new FileWriter(filePath);
		BufferedWriter bw = new BufferedWriter(fw);
		return new PrintWriter(bw);
	}
}
