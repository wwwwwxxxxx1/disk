package com.beans;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Author wuxin
 * @Date 2023/6/27 16:07
 * @Description chepingping    上海	车平平	35	女
 * @Version
 */
public class ScoreInfo implements WritableComparable<ScoreInfo> {
    //
    public  ScoreInfo() {
    }

    //有参构造
    public ScoreInfo(String idCard, String address, String name, int score, String gender) {
        this.idCard = idCard;
        this.address = address;
        this.name = name;
        this.score = score;
        this.gender = gender;
    }

    //id
    private String idCard;

    //所属区域 (只有四个取值,北京,上海,广州,深圳)
    private String address;

    //名字
    private String name;

    //积分
    private int score;

    //性别
    private String gender;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    //序列化
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.idCard);
        out.writeUTF(this.address);
        out.writeUTF(this.name);
        out.writeInt(this.score);
        out.writeUTF(this.gender);
    }

    //反序列化
    public void readFields(DataInput in) throws IOException {
        this.idCard=in.readUTF();
        this.address=in.readUTF();
        this.name=in.readUTF();
        this.score=in.readInt();
        this.gender=in.readUTF();
    }

    //可以用这个方法进行对象的比较
    public int compareTo(ScoreInfo o) {
        if(this.score==o.score) {
            return 0;
        }

        else if(this.score>o.score) {
            return 1;
        }
        else {
            return -1;
        }
    }

    //不要忘了重写toString() 方法
    public String toString() {
        return this.address+"\t" +this.name+"\t"+this.score+"\t"+this.gender;
    }
}
