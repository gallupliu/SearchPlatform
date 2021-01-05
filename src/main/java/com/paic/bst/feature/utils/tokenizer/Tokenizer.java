package com.paic.bst.feature.utils.tokenizer;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.paic.bst.util.analyzer.HanlpAnalyzerUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * description: Tokenizer
 * date: 2020/12/31 11:26 上午
 * author: gallup
 * version: 1.0
 */
@Slf4j
public class Tokenizer {

    public static List<Word> segment(String sentence) {
        List<Word> results = new ArrayList<>();

        // HanLP
//        List<Term> termList = HanLP.segment(sentence);
//        results.addAll(termList
//                .stream()
//                .map(term -> new Word(term.word, term.nature.name()))
//                .collect(Collectors.toList())
//        );
        List<Term> termList = HanLP.segment(sentence);
        results.addAll(termList
                .stream()
                .map(term -> new Word(term.word, term.nature.toString()))
                .collect(Collectors.toList())
        );

        return results;
    }



    public static void fileSegment(String inputFilePath, String outputFilePath) {
        fileSegment(HanLP.newSegment(), inputFilePath, outputFilePath);
    }

    public static void fileSegment(Segment segment, String inputFilePath, String outputFilePath) {
        try {
            WordFreqStatistics.statistics(segment, inputFilePath);
            BufferedReader reader = IOUtil.newBufferedReader(inputFilePath);
            long allCount = 0;
            long lexCount = 0;
            long start = System.currentTimeMillis();
            String outPath = inputFilePath.replace(".txt", "") + "-Segment-Result.txt";
            if (outputFilePath != null && outputFilePath.trim().length() > 0) outPath = outputFilePath;
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            String temp;
            while ((temp = reader.readLine()) != null) {
                List<Term> parse = segment.seg(temp);
                StringBuilder sb = new StringBuilder();
                for (Term term : parse) {
                    sb.append(term.toString() + "\t");
                    if (term.word.trim().length() > 0) {
                        allCount += term.length();
                        lexCount += 1;
                    }
                }
                fos.write(sb.toString().trim().getBytes());
                fos.write("\n".getBytes());
            }

            fos.flush();
            fos.close();
            reader.close();
            long end = System.currentTimeMillis();
            System.out.println("segment result save：" + outPath);
            System.out.println("total " + allCount + " chars, " + lexCount + " words, spend" + (end - start) + "ms ");
        } catch (IOException e) {
            log.error("IO error: " + e.getLocalizedMessage());
        }
    }
}
