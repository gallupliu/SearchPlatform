package com.paic.bst.util;


import com.paic.bst.util.analyzer.HanlpAnalyzerUtils;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * description: HanlpAnalyzerUtilsTest
 * date: 2021/1/5 9:26 下午
 * author: gallup
 * version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class HanlpAnalyzerUtilsTest {
    @Autowired
    HanlpAnalyzerUtils hanlpAnalyzerUtils;

    @Test
    public void testSave(){

        hanlpAnalyzerUtils.getTokens("中国平安技术（深圳）责任有限公司");
    }
}
