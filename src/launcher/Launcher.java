package launcher;
import java.io.*;
import org.json.*;

import relexifier.Relex;
import word_analyser.WordAnalyserManager;
import word_analyser.WordAnalyzer;

/**
 * 
 * @author Julien HERMENT
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
		String analysisFileName = "analysis_fran�ais.txt";
		String elementsFileName = "fran�ais_all.txt";
		String resultsFileName = "new_fran�ais.txt";
		WordAnalyzer wordAnalyzer = new WordAnalyzer();
		wordAnalyzer.setAnalysisFilePath(analysisFileName);
		wordAnalyzer.setElementsFilePath(elementsFileName);
		wordAnalyzer.setResultsFilePath(resultsFileName);
		testAnalysisAndRegister(wordAnalyzer);
		
		testCreateWordList(wordAnalyzer);
		
		resultsFileName = "new_fran�ais_length_5.txt";
		wordAnalyzer.setResultsFilePath(resultsFileName);
		testCreateWordListFixLength(wordAnalyzer);

		elementsFileName = "fran�ais_common.txt";
		resultsFileName = "relex_fran�ais.txt";
		WordAnalyzer relexAnalyzer = new WordAnalyzer();
		relexAnalyzer.setAnalysisFilePath(analysisFileName);
		relexAnalyzer.setElementsFilePath(elementsFileName);
		relexAnalyzer.setResultsFilePath(resultsFileName);
		testCreateRelex(relexAnalyzer);

		elementsFileName = "test_relex.txt";
		relexAnalyzer.setElementsFilePath(elementsFileName);
		testRelexifyText(relexAnalyzer);
	}
	
	public static void testAnalysisAndRegister(WordAnalyzer wordAnalyzer) {
		System.out.println("testAnalysisAndRegister");
		WordAnalyserManager wordAnalyserManager = new WordAnalyserManager(wordAnalyzer);
		wordAnalyserManager.analysisAndRegister();
	}
	
	public static void testCreateWordList(WordAnalyzer wordAnalyzer) throws JSONException, IOException {
		System.out.println("createWordList");
		WordAnalyserManager wordAnalyserManager = new WordAnalyserManager(wordAnalyzer);
		wordAnalyserManager.createWordList(1000, "");
	}
	
	public static void testCreateWordListFixLength(WordAnalyzer wordAnalyzer) throws JSONException, IOException {
		System.out.println("createWordListFixLength");
		WordAnalyserManager wordAnalyserManager = new WordAnalyserManager(wordAnalyzer);
		wordAnalyserManager.setWordAnalyzer(wordAnalyzer);
		wordAnalyserManager.createWordListFixLength(1000, "", 5);
	}
	
	public static void testCreateRelex(WordAnalyzer relexAnalyzer) throws JSONException, IOException {
		System.out.println("createRelex");
		Relex relex = new Relex(relexAnalyzer);
		relex.createRelex();
	}
	
	public static void testRelexifyText(WordAnalyzer relexAnalyzer) throws IOException, JSONException {
		System.out.println("relexifyText");
		Relex relex = new Relex(relexAnalyzer);
		relex.relexifyText();
	}
}