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
		String analysisFileName = "analysis_animaux.txt";
		String elementsFileName = "animaux.txt";
		String resultsFileName = "new_animaux.txt";
		WordAnalyzer wordAnalyzer = new WordAnalyzer();
		wordAnalyzer.setAnalysisFilePath(analysisFileName);
		wordAnalyzer.setElementsFilePath(elementsFileName);
		wordAnalyzer.setResultsFilePath(resultsFileName);
		testAnalysisAndRegister(wordAnalyzer);
		
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