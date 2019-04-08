package launcher;
import java.io.*;
import org.json.*;

import relexifier.Relex;
import word_analyser.WordAnalyserManager;

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
		String inputAnalysis = "français_all.txt";
		String outputAnalysis = "analysis_français.txt";
		WordAnalyserManager.analysisElementsFileAndRegisterResult(inputAnalysis, outputAnalysis);

		String output1 = "new_français.txt";
		WordAnalyserManager.createWordList(1000, "", outputAnalysis, output1);
		
		String output2 = "new_français_length_5.txt";
		WordAnalyserManager.createWordListFixLength(1000, "", outputAnalysis, output2, 5);
		
		String inputRelex = "français_common.txt";
		String outputRelex = "relex_français.txt";
		Relex.createRelex(inputRelex, outputAnalysis, outputRelex);
	}
}