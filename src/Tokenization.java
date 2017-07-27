import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Tokenization 
{   
    static TreeMap<String, Integer> token = new TreeMap<String, Integer>();
    static TreeMap<String, Integer> stem = new TreeMap<String, Integer>();
	
    static int countFile=0;
	static int countToken=0;
    static int countStem=0;
    static int avgTokenInFile=0;
    static int avgStemInFile= 0;
	private static Scanner currentFile;
    
	// -------- Tokenization ----------
	
	private static void tokenize(File file) throws FileNotFoundException 
	{
		currentFile = new Scanner(file);
		StringTokenizer st;
		String current, modifiedCurrent;
		TreeMap<String, Integer> tokenInFile = new TreeMap<String, Integer>();
        TreeMap<String, Integer> stemInFile= new TreeMap<String, Integer>();
        
		while(currentFile.hasNextLine())
		{
			String line = currentFile.nextLine();
			if(!(line.contains("<")) && !(line.contains(">")) && line != null)
			{
				line = line.replaceAll("[-]", " ");
				st = new StringTokenizer(line);
                while(st.hasMoreTokens())
                {
                	current = st.nextToken().toLowerCase();
                    modifiedCurrent = current.replaceAll("[^a-zA-Z0-9]", "");
                    
                    Stemmer stemmer= new Stemmer();
                    char charStem[] = modifiedCurrent.toCharArray();
                    stemmer.add(charStem, charStem.length);
                    stemmer.stem();
                    
                    if(modifiedCurrent.equals(""))
                        continue;
                    else
                    {
                    	// -------- Putting token into TreeMap ----------
                        countToken++;
                        if(token.get(modifiedCurrent) == null)
                            token.put(modifiedCurrent, 1);
                        else
                            token.put(modifiedCurrent, token.get(modifiedCurrent) + 1);
                        
                      // -------- Putting stem into TreeMap ----------
                        countStem++;
                        if(stem.get(stemmer.toString()) == null)
                            stem.put(stemmer.toString(), 1);
                        else
                            stem.put(stemmer.toString(), stem.get(stemmer.toString()) + 1);
                        
                        // -------- Distinct Tokens ----------
                        if(tokenInFile.get(modifiedCurrent) == null)
                            tokenInFile.put(modifiedCurrent, 1);
                        else
                            tokenInFile.put(modifiedCurrent, tokenInFile.get(modifiedCurrent) + 1);

                        // -------- Distinct Tokens ----------
                        if(stemInFile.get(stemmer.toString()) == null)
                            stemInFile.put(stemmer.toString(), 1);
                        else
                            stemInFile.put(stemmer.toString(), stemInFile.get(stemmer.toString()) + 1);
                    	 
                    }
                }
			}	
		}
		
		avgTokenInFile = avgTokenInFile + tokenInFile.size();
		avgStemInFile = avgStemInFile + stemInFile.size();
		tokenInFile.clear();
		stemInFile.clear();
		
	}
	
	// -------- Sorting according to frequency ----------
	
	public static TreeMap<String, Integer> freqSort(final TreeMap<String, Integer> token)
    {
		Comparator<String> sc = new Comparator<String>()
        {
            public int compare(String string1, String string2) 
            {
                if (token.get(string2).compareTo(token.get(string1)) == 0)
                    return 1;
                else
                    return token.get(string2).compareTo(token.get(string1));
            }
        };

        TreeMap<String, Integer> freqSortToken = new TreeMap<String, Integer>(sc);        
        freqSortToken.putAll(token);
        return freqSortToken;
    }

	// -------- Main Method ----------
	
	public static void main(String args[]) throws FileNotFoundException
	{
		
		String fPath = args[0].toString();
		//String fPath = "/Users/karanmotani/Desktop/Cranfield";
		double startTime = Calendar.getInstance().getTimeInMillis();
		
		File folder = new File(fPath);
		File[] listOfFiles = folder.listFiles(); 

		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if (listOfFiles[i].isFile())  
			{
				countFile++;
				tokenize(listOfFiles[i]);
			}
		}
		
		System.out.println("\t" + "Problem 1: Tokenization");
		System.out.println();
		System.out.println("1. The number of tokens in the Cranfield text collection: " + countToken);
		System.out.println();
		System.out.println("2. The number of unique words in the Cranfield text collection: " + token.size());
		System.out.println();
		
		int wordCount=0;
		Iterator<Map.Entry<String, Integer>> wordCountIterator = token.entrySet().iterator();
        while(wordCountIterator.hasNext())
        {
            Map.Entry<String, Integer> entry = wordCountIterator.next();
            if(entry.getValue()==1)
                wordCount++;
        }
		
		System.out.println("3. The number of words that occur only once in the Cranfield text collection: " + wordCount);
		System.out.println();
		System.out.println("4. The 30 most frequent words in the Cranfield text collection and respective frequency information: ");
		System.out.println();
		System.out.println("#" + "\t" + "Token" + "\t  " + "Frequency");
		
		TreeMap<String, Integer> freqSortToken= freqSort(token);
        Iterator<Map.Entry<String, Integer>> freqSortTokenIterator = freqSortToken.entrySet().iterator();
        
        for(int counter=1; counter<=30; counter++)
        {
            Map.Entry<String, Integer> entry = freqSortTokenIterator.next();
            System.out.println(counter + "\t" + entry.getKey() + "\t    " + entry.getValue());
        }
		
        System.out.println();
		System.out.println("5. The average number of word tokens per document: " + (countToken/countFile));
		System.out.println();
		System.out.println("6. Time taken to acquire the text characteristics: " + (Calendar.getInstance().getTimeInMillis()-startTime) + "ms");
		System.out.println();
		
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("\t" + "Problem 2: Stemming");
		System.out.println();
		System.out.println("1. The number of distinct stems in the Cranfield text collection: " + stem.size());
		System.out.println();
		
		int stemCount=0;
		Iterator<Map.Entry<String, Integer>> stemCountIterator = stem.entrySet().iterator();
        while(stemCountIterator.hasNext())
        {
            Map.Entry<String, Integer> entry = stemCountIterator.next();
            if(entry.getValue()==1)
                stemCount++;
        }
		System.out.println("2. The number of stems that occur only once in the Cranfield text collection: " + stemCount);
		System.out.println();
		System.out.println("3. The 30 most frequent stems in the Cranfield text collection and respective frequency information: ");
		System.out.println();
		System.out.println("#" + "\t" + "Stem" + "\t  " + "Frequency");
		
		TreeMap<String, Integer> freqSortStem= freqSort(stem);
        Iterator<Map.Entry<String, Integer>> freqSortStemIterator = freqSortStem.entrySet().iterator();
        
        for(int counter=1; counter<=30; counter++)
        {
            Map.Entry<String, Integer> entry = freqSortStemIterator.next();
            System.out.println(counter + "\t" + entry.getKey() + "\t    " + entry.getValue());
        }
		
		System.out.println();
		System.out.println("4. The average number of word stems per document: " + countStem/countFile);
		System.out.println();
		
	}
}
