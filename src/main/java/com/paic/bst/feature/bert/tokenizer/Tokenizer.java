package com.paic.bst.feature.bert.tokenizer;

import java.util.List;
/**
 * description: Tokenizer
 * date: 2020/11/24 2:30 下午
 * author: gallup
 * version: 1.0
 */
public interface Tokenizer {
    public List<String> tokenize(String text);
}
