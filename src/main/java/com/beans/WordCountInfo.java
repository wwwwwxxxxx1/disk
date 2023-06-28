package com.beans;

/**
 * @Author wuxin
 * @Date 2023/6/28 16:06
 * @Description
 * @Version
 */
public class WordCountInfo {
    private int count;
    private String word;

    public WordCountInfo() {}

    public WordCountInfo(int count, String word) {
        this.count = count;
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
