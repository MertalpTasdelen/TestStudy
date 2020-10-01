package com.company;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class ThreadDemo extends Thread {

    //For sorting the map
    public static boolean ASC = true;
    public static boolean DESC = false;

    static int countSentence = 0;

    static int counter = 0;
    static String[] sentenceTMP;

    static String[] sentences = null;

    static HashMap<String, Integer> wordBook = new HashMap<String, Integer>();


    public static int countWordsUsingStringTokenizer(String sentence) {
        if (sentence == null || sentence.isEmpty()) { return 0; }

        StringTokenizer tokens = new StringTokenizer(sentence);
        return tokens.countTokens();
    }

    public static void main(String args[]) {

        String filePath;
        int threadCount;

        int avearageWordCount = 0;//This will contain median of word count Can be used?
        int wordCount = 0;

        Scanner input = new Scanner(System.in);

        System.out.println("Enter the full file path: ");
        filePath = input.nextLine();
        System.out.println("Enter the thread count: ");
        threadCount = input.nextInt();

        if (threadCount < 5){
            System.out.println("Please enter a value more than 5 or default will be 5");
            threadCount = input.nextInt();
            if (threadCount < 5){
                System.out.println("Thread count set as 5");
                threadCount = 4;
            }
        }

//        src/com/company/text.txt
        sentenceCounter(filePath);
        //We have the sentences!!

        for (int i=0;i<countSentence;i++)
        {
            int tmpCount = countWordsUsingStringTokenizer(sentences[i]);
            wordCount += tmpCount;
        }

        avearageWordCount = wordCount / sentences.length;
        System.out.println("Sentence count: " + sentences.length);
        System.out.println("Avg. Word count: " + avearageWordCount);

        for(int i =0; i<threadCount; i++){
//            sentences[counter].split("\\s+")
            ThreadDemo t = new ThreadDemo(sentences[i].split("\\s+"));
            t.start();


        }

        //TODO: Needs to be ordered
        Map<String, Integer> sortedMapDesc = sortByValue(wordBook, DESC);
        System.out.println(sortedMapDesc.toString());

    }

    public ThreadDemo( int count) {
        counter = count;
    }

    public ThreadDemo( ) {
    }

    public ThreadDemo( String[] tmpSentence) {
        sentenceTMP = tmpSentence;
    }

    public void run() {}



    public void start () {

        //TODO: Need a queue structure.
        String[] words = sentenceTMP;

        System.out.println("Thread " + getId()  + "is started");

        for(int i = 0; i < words.length; i++) {

            if (!words[i].isEmpty())
            {
                synchronized(ThreadDemo.class){

                    if(!wordBook.containsKey(words[i]))
                        wordBook.put(words[i], 1);
                    else
                        wordBook.replace(words[i], wordBook.get(words[i]) + 1 );
                }

            }

        }

    }


    public static void sentenceCounter(String fileName) {

        File file = new File(fileName);
        BufferedReader br;
        String line = "";

        try {

            br = new BufferedReader(new FileReader(file));
            line = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        sentences = line.split("\\.|\\?");

        countSentence = sentences.length;

    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean order)
    {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    private static void printMap(Map<String, Integer> map)
    {
        map.forEach((key, value) -> System.out.println("Key : " + key + " Value : " + value));
    }


}
