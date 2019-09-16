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

		System.out.println("analysisElementsFileAndRegisterResult");
		WordAnalyserManager wordAnalyserManager = new WordAnalyserManager(wordAnalyzer);
		wordAnalyserManager.analysisElementsFileAndRegisterResult();

		System.out.println("createWordList");
		wordAnalyserManager.createWordList(1000, "");

		System.out.println("createWordListFixLength");
		resultsFileName = "new_fran�ais_length_5.txt";
		wordAnalyzer.setResultsFilePath(resultsFileName);
		wordAnalyserManager.setWordAnalyzer(wordAnalyzer);
		wordAnalyserManager.createWordListFixLength(1000, "", 5);

		System.out.println("createRelex");
		elementsFileName = "fran�ais_common.txt";
		resultsFileName = "relex_fran�ais.txt";

		WordAnalyzer relexAnalyzer = new WordAnalyzer();
		relexAnalyzer.setAnalysisFilePath(analysisFileName);
		relexAnalyzer.setElementsFilePath(elementsFileName);
		relexAnalyzer.setResultsFilePath(resultsFileName);
		Relex relex = new Relex(relexAnalyzer);
		relex.createRelex();

		System.out.println("relexifyText");
		String testRelex = "test_relex.txt";
		relexAnalyzer.setElementsFilePath(testRelex);
		relex.relexifyText();
	}
}