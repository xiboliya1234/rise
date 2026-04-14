package com.rise.util;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ansj {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String text = "20个左边的卡罗拉倒车镜! ";
        String analysisedText = NlpAnalysis.parse(text).toStringWithOutNature();
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("nlp分词: " + analysisedText + "(" + time + "ms)");
    }


    public static String nlpfc(String word){
        //long startTime = System.currentTimeMillis();
       // String text = "20个左边的卡罗拉倒车镜! ";
        String analysisedText = NlpAnalysis.parse(word).toStringWithOutNature();
       // long endTime = System.currentTimeMillis();
       // long time = endTime - startTime;
        return  analysisedText;

    }
}
