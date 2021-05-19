package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤器
 * 使用前缀树
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //要替换成什么符号
    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //关于流的知识要更深入地学习
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyWord;
            while ((keyWord = reader.readLine())!=null){
                //添加到前缀树
                this.addKeyWord(keyWord);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败"+e.getMessage());
        }
    }

    /**
     * 将一个关键字添加到前缀树
     * @param keyWord
     */
    private void addKeyWord(String keyWord){
        TrieNode tempNode = this.rootNode;
        for(int i = 0; i < keyWord.length(); ++i){
            char c = keyWord.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                //初始化subNode
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            //指针下移
            tempNode = subNode;
            if(i == keyWord.length() - 1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    public String filter(String text){
        TrieNode tempNode = this.rootNode;
        int begin = 0;
        int position = 0;
        StringBuilder sb = new StringBuilder();
        //用最快的指针判断是否已经到达末尾
        while (position < text.length()){
            char c = text.charAt(position);
            //首先跳过特殊字符
            if(isSymbol(c)){
                //若前缀树的指针位于根节点，那么直接跳过
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = this.rootNode;
            }
            else if(tempNode.isKeyWordEnd()){
                //发现敏感词，将begin~position的字符串替换掉
                sb.append(REPLACEMENT);
                begin = ++position;
                tempNode = rootNode;
            }
            else {
                //检查下一个字符
                position ++;
            }
        }
        //将最后一批字符计入sb
        sb.append(text.substring(begin));
        return sb.toString();
    }

    // 判断是否为特殊符号
    private boolean isSymbol(Character c){
        //0x2E80~0x9FFF是东亚文字范围
        return CharUtils.isAsciiAlphanumeric(c) &&(c<0x2E80 ||c>0x9FFF);
    }

    /**
     * 前缀树的节点数据结构
     */
    private class TrieNode {
        // 是否是敏感词的末尾
        private boolean isKeyWordEnd = false;
        // 使用map记录所有的子节点，key是字符，value是前缀树节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
