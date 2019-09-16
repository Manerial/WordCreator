package word_analyser;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import utilities.BasicFunctions;
import utilities.WordsFilesManager;

public class WordAnalyserManager {
	private WordAnalyzer analyzer;
	
	public WordAnalyserManager(WordAnalyzer analyzer) {
		setWordAnalyzer(analyzer);
	}

	public void setWordAnalyzer(WordAnalyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * Analysis a file containing words and save the result of the analysis in a file
	 * 
	 * @param elementsFile : The name of the file that contains the words to be analyzed
	 * @param analysisFilePath : The name of the file where the analysis result will be stored
	 */
	public void analysisElementsFileAndRegisterResult() {
		try {
			BufferedReader br = WordsFilesManager.readElementsFile(analyzer);
			
			String word;
			while ((word = br.readLine()) != null) {
				analysisWord(word);
			}
			br.close();
			WordsFilesManager.saveAnalysisFile(analyzer);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}


	/**
	 * Analyze a word : it number of letters and the bigrams used in it
	 * 
	 * @param word : The word to analyze
	 * @throws JSONException : for JSON error
	 */
	public void analysisWord(String word) throws JSONException {
		String characterAtPosA;
		String characterAtPosB;
		String characterAtPosC;
		for (int charPosition = 0; charPosition < word.length(); charPosition++) {
			int nextCharPosition = charPosition + 1;
			int secondCharPosition = charPosition + 2;
			characterAtPosA = BasicFunctions.getCharAtPosition(word, charPosition);
			characterAtPosB = BasicFunctions.getCharAtPosition(word, nextCharPosition);
			characterAtPosC = BasicFunctions.getCharAtPosition(word, secondCharPosition);
			analyzer.incrementTotalLetters();
			saveAnalysis(characterAtPosA, characterAtPosB, characterAtPosC, charPosition);
		}
		analyzer.incrementTotalAnalyzedWords();
	}


	private void saveAnalysis(String characterAtPosA, String characterAtPosB, String characterAtPosC, int characterPosition)
			throws JSONException {
		if (BasicFunctions.isFirstCharacter(characterPosition)) {
			analyzer.saveBigramNextChar(characterAtPosA, characterAtPosB);
		}
		
		if(characterAtPosB != "") {
			String bigramme = characterAtPosA + characterAtPosB;
			analyzer.saveBigramNextChar(bigramme, characterAtPosC);
		}
	}

	/**
	 * Create a certain amount of words using the analysis of a list of words
	 * 
	 * @param numberOfWords : Number of word to create
	 * @param beginning : If not empty, will be the begin of the new word
	 * @param analysisFilePath : The name of the analysis file to use to create words
	 * @param resultFile : The name of the file where the words creation will be stored
	 * @throws JSONException : for JSON error
	 * @throws IOException : for file management errors
	 */
	public void createWordList(int numberOfWords, String beginning) throws JSONException, IOException {
		WordsFilesManager.parseAnalysisFile(analyzer);
		List<String> createdWordList = new ArrayList<>();
		for(int i = 0; i < numberOfWords; i++) {
			String createdWord;
			do {
				createdWord = analyzer.createWord(beginning);
			} while (BasicFunctions.isWordUsed(createdWordList, createdWord));
			createdWordList.add(createdWord);
		}
		WordsFilesManager.printStringListInResultFile(analyzer, createdWordList);
	}

	/**
	 * Create a certain amount of words with a fixed length using the analysis of a list of words
	 * 
	 * @param numberOfWords : Number of word to create
	 * @param beginning : If not empty, will be the begin of the new word
	 * @param analysisFilePath : The name of the analysis file to use to create words
	 * @param resultFile : The name of the file where the words creation will be stored
	 * @param wordLength : The length of the created words
	 * @throws JSONException : for JSON error
	 * @throws IOException : for file management errors
	 */
	public void createWordListFixLength(int numberOfWords, String beginning, int wordLength) throws JSONException, IOException {
		WordsFilesManager.parseAnalysisFile(analyzer);
		List<String> createdWordList = new ArrayList<>();
		for(int i = 0; i < numberOfWords; i++) {
			String createdWord;
			do {
				createdWord = analyzer.createWord(beginning);
			} while (BasicFunctions.isWordUsed(createdWordList, createdWord) || createdWord.length() != wordLength);
			createdWordList.add(createdWord);
		}
		WordsFilesManager.printStringListInResultFile(analyzer, createdWordList);
	}
}