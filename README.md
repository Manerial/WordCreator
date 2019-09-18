# WordCreator
Creates words with a certain consonance that you can customize

## How to use it ?
First, get an IDE (Eclipse)

1. Download Eclipse (The last version)
2. Install Eclipse
3. Launch Eclipse

Next, clone / download this project

Go to Eclipse and follow the procedure :

File -> Open project from File system -> Directory -> [go to the project] -> Select the project with the checkbox -> Finish

Then, you should have access to the project.

To launch it, you can :
1. click the run button
2. ctrl + f11
3. use the menu : Run -> Run

To change it : open the project from the Eclipse window -> open the src folder -> open the launcher package (folder) -> double click on Launcher.java

## RESOURCES
This project uses different resources files.

These files are stored in the resources folder of the WordCreator project.

You can update them as much as you want (adding/removing lines).

## How does it work ?
This project uses files of words to do some analysis and use them to create new words. Basically, it will look for all the bigrams (couple of letters) of a word and save their frequency of appearing. It will also save the frequency of all the next letters that will appear after a bigram.

It works like a decision tree. When you have your first bigram (like "be" in this example), the program will randomly choose one of the next letters that can fit. We won't find abberations like finding "mlfpxct" thanks to this method, but only words that could have the most chance to appears.

## Architecture
You will find only 3 javaclass :

First, the Launcher.java class : It's the entry point (the Main class) that's allow you to use different functions of the program.

Then, you have the WordAnalyzerManager.java class : The interface that will read files and use them to create analysis (mostly read, write and process the analyzer).

And last, the WordAnalyzer.java class : the heart of this program. It will parse a word and analyze it structure to get the most precise analysis it can.
