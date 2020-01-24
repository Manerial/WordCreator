package launcher;
import java.io.*;
import org.json.*;

import relexifier.Relex;
import word_analyzer.WordAnalyzerManager;
import word_analyzer.WordAnalyzer;

/**
 * 
 * @author Manerial
 * Version 1.0, 13/03/2019
 * 
 */
public class Launcher {

	/**
	 * Entry point
	 * 
	 * @param args : Not used
	 * @throws JSONException : for JSON error
	 * @throws IOException : for file management errors
	 */
	public static void main(String[] args) throws JSONException, IOException {
		String analysisFileName = "analysis_français.txt";
		String elementsFileName = "français_all.txt";
		String resultsFileName = "new_français.txt";
		WordAnalyzer wordAnalyzer = new WordAnalyzer();
		wordAnalyzer.setAnalysisFilePath(analysisFileName);
		wordAnalyzer.setElementsFilePath(elementsFileName);
		wordAnalyzer.setResultsFilePath(resultsFileName);
		testAnalysisAndRegister(wordAnalyzer);
		
		testCreateWordList(wordAnalyzer);
		
		resultsFileName = "new_français_length_5.txt";
		wordAnalyzer.setResultsFilePath(resultsFileName);
		testCreateWordListFixLength(wordAnalyzer);

		elementsFileName = "français_common.txt";
		resultsFileName = "relex_français.txt";
		WordAnalyzer relexAnalyzer = new WordAnalyzer();
		relexAnalyzer.setAnalysisFilePath(analysisFileName);
		relexAnalyzer.setElementsFilePath(elementsFileName);
		relexAnalyzer.setResultsFilePath(resultsFileName);
		testCreateRelex(relexAnalyzer);

		elementsFileName = "test_relex.txt";
		relexAnalyzer.setElementsFilePath(elementsFileName);
		testRelexifyText(relexAnalyzer);
	}
	
	public static void testAnalysisAndRegister(WordAnalyzer wordAnalyzer) throws JSONException, IOException {
		System.out.println("testAnalysisAndRegister");
		WordAnalyzerManager wordAnalyserManager = new WordAnalyzerManager(wordAnalyzer);
		wordAnalyserManager.analysisAndRegister();
	}
	
	public static void testCreateWordList(WordAnalyzer wordAnalyzer) throws JSONException, IOException {
		System.out.println("testCreateWordList");
		WordAnalyzerManager wordAnalyserManager = new WordAnalyzerManager(wordAnalyzer);
		wordAnalyserManager.createWordList(1000, "");
	}
	
	public static void testCreateWordListFixLength(WordAnalyzer wordAnalyzer) throws JSONException, IOException {
		System.out.println("testCreateWordListFixLength");
		WordAnalyzerManager wordAnalyserManager = new WordAnalyzerManager(wordAnalyzer);
		wordAnalyserManager.setWordAnalyzer(wordAnalyzer);
		wordAnalyserManager.createWordListFixLength(1000, "", 5);
	}
	
	public static void testCreateRelex(WordAnalyzer relexAnalyzer) throws JSONException, IOException {
		System.out.println("testCreateRelex");
		Relex relex = new Relex(relexAnalyzer);
		relex.createRelex();
	}
	
	public static void testRelexifyText(WordAnalyzer relexAnalyzer) throws IOException, JSONException {
		System.out.println("testRelexifyText");
		Relex relex = new Relex(relexAnalyzer);
		relex.relexifyText();
	}
}