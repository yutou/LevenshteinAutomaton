/**
 * LevenshteinAutomaton is a fast and comprehensive Java library capable
 * of performing automaton and non-automaton based Levenshtein distance
 * determination and neighbor calculations.
 * 
 *  Copyright (C) 2012 Kevin Lawson <Klawson88@gmail.com>
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.BoxOfC.LevenshteinAutomatonTest;


import com.BoxOfC.LevenshteinAutomaton.LevenshteinAutomaton;
import com.BoxOfC.MDAG.MDAG;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



/**
 *
 * @author Kevin
 */
public class LevenshteinAutomatonTest 
{
    ArrayList<String> wordArrayList = new ArrayList<String>();
    MDAG mdag = null;
    int maxEditDistanceToTest = 3;
 
    @BeforeClass
    public void initializer() throws IOException
    {
        BufferedReader breader = new BufferedReader(new FileReader(new File("C:\\Users\\Kevin\\Documents\\NetBeansProjects\\MDAGTest\\words.txt")));
        String currentWord;
        
        while((currentWord = breader.readLine()) != null)
            wordArrayList.add(currentWord);
        
        mdag = new MDAG(wordArrayList);
        //mdag.simplify();
    }
    
    
     public static int computeDistance(String s1, String s2) {
        //s1 = s1.toLowerCase();
        //s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
        int lastValue = i;
        for (int j = 0; j <= s2.length(); j++) {
            if (i == 0)
            costs[j] = j;
            else {
            if (j > 0) {
                int newValue = costs[j - 1];
                if (s1.charAt(i - 1) != s2.charAt(j - 1))
                newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                costs[j - 1] = lastValue;
                lastValue = newValue;
            }
            }
        }
        if (i > 0)
            costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
     
     
     
     @DataProvider(name = "automatonWordDataProvider")
     public Object[][] automatonWordDataProvider()
     {
         int testCount = 15;
         Object[][] argArrayContainerArray = new Object[testCount][];
         
         int wordCount = wordArrayList.size();
         
         for(int i = 0; i < testCount; i++)
         {
             int randomMaxEditDistance =  (int)(Math.random() * maxEditDistanceToTest) + 1;
             int randomIndex = (int)(Math.random() * wordCount);
             argArrayContainerArray[i] = new Object[]{randomMaxEditDistance, wordArrayList.get(randomIndex)};
         }
         
         return argArrayContainerArray;
     }
     
     
     @Test(enabled = false, dataProvider = "automatonWordDataProvider")
     public void tableFuzzyProfiler(int maxEditDistance, String str)
     {
         HashSet<String> resultHashSet2 = new HashSet<String>(LevenshteinAutomaton.tableFuzzySearch(maxEditDistance, str, mdag));
     }
     
     
     @Test(enabled = false, dataProvider = "automatonWordDataProvider")
     public void tableFuzzySearchTest(int maxEditDistance, String str)
     {
         HashSet<String> resultHashSet1 = new HashSet<String>();
         
         for(String currentWord : wordArrayList)
         {
             if(computeDistance(str, currentWord) <= maxEditDistance)
                 resultHashSet1.add(currentWord); 
         }
         
         
         HashSet<String> resultHashSet2 = new HashSet<String>(LevenshteinAutomaton.tableFuzzySearch(maxEditDistance, str, mdag));
         assert(resultHashSet1.equals(resultHashSet2));
     }
     
     @Test(enabled = false, dataProvider = "automatonWordDataProvider")
     public void iterativeFuzzySearchTest(int maxEditDistance, String str)
     {
         HashSet<String> resultHashSet1 = new HashSet<String>();
         
         for(String currentWord : wordArrayList)
         {
             if(computeDistance(str, currentWord) <= maxEditDistance)
                 resultHashSet1.add(currentWord); 
         }
         
         
         HashSet<String> resultHashSet2 = new HashSet<String>(LevenshteinAutomaton.iterativeFuzzySearch(maxEditDistance, str, mdag));
         assert(resultHashSet1.equals(resultHashSet2));
     }
     
     @Test(dataProvider = "automatonWordDataProvider")
     public void isWithinEditDistance(int maxEditDistance, String str)
     {
         HashSet<String> resultHashSet1 = new HashSet<String>();
         
         LevenshteinAutomaton.isWithinEditDistance(3, "OKs", "Aachen");
         
         for(String currentWord : wordArrayList)
             assert (computeDistance(str, currentWord) <= maxEditDistance) == LevenshteinAutomaton.isWithinEditDistance(maxEditDistance, str, currentWord);
         
         HashSet<String> resultHashSet2 = new HashSet<String>();
         assert(resultHashSet1.equals(resultHashSet2));
     }
}
