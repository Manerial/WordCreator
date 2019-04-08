package relexifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import utilities.WordsFilesManager;
import word_analyser.WordAnalyzer;

public class Relex {

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
		WordsFilesManager.parseAnalysisFile(analysisFile, analyzer);
		List<String> sourceWordList = WordsFilesManager.parseElementsFileInList(elementsFile);
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
		WordsFilesManager.printWordListInResultFile(relexFile, createdWordList);
	}

}
