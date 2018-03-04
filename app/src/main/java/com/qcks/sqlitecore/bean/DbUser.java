package com.qcks.sqlitecore.bean;


import com.qcks.sqlitecore.annotation.DbFields;
import com.qcks.sqlitecore.annotation.DbTable;
import com.qcks.sqlitecore.db.DataBaseConfig;

/**
 * User
 * 全部使用小写命名
 * 每张表必须有不为空的主键
 * @author qckiss
 * @date 2017-12-24
 */

@DbTable(tableName = "all_daba_type_table",dbName = DataBaseConfig.MAIN_DB,parentTableName = "",foreignKey = "")
public class DbUser {
    @DbFields(columnName = "Str_Str",defaultValue = "1") public String string;
    @DbFields(columnName = "") public byte aByte;
    @DbFields(columnName = "") public short aShort;
    @DbFields(columnName = "tb_intPk",isPrimaryKey = true,isNull = false) public int intPk;
    @DbFields(columnName = "") public long aLong;

    @DbFields(columnName = "testFk",isNull = false,isRelevantFk = true) public long testFk;

    @DbFields(columnName = "") public float aFloat;
    @DbFields(columnName = "") public double aDouble;

    @DbFields(columnName = "") public char aChar;
    @DbFields(columnName = "") public boolean aBoolean;

    @DbFields(columnName = "") public Byte bByte;
    @DbFields(columnName = "") public Short bShort;
    @DbFields(columnName = "",defaultValue = "100") public Integer bInt;
    @DbFields(columnName = "") public Long bLong;
    @DbFields(columnName = "") public Float bFloat;
    @DbFields(columnName = "") public Double bDouble;
    @DbFields(columnName = "") public Character bChar;
    @DbFields(columnName = "") public Boolean bBoolean;

    /**
     * version==2
     */
    @DbFields(columnName = "student_name",defaultValue = "3") public String student;

    public DbUser() {
    }
    public DbUser(int aInt) {
        this.aByte = 1;
        this.aShort = 11;
        this.intPk = aInt;
        this.aLong = 111111;
        this.aFloat = 1.111f;
        this.aDouble = 11.0232322;
        this.aChar = 'a';
        this.aBoolean = true;
        this.bByte = 2;
        this.bShort = 22;
//        this.bInt = 222;
        this.bLong = 2222222L;
        this.bFloat = 2.222342F;
        this.bDouble = 22.023616615;
        this.bChar = 'A';
        this.bBoolean = false;
        this.string = "个";
        this.student = "甲乙丙";
    }

    public DbUser(byte aByte, short aShort, int aInt, long aLong, float aFloat, double aDouble, char aChar, boolean aBoolean, Byte bByte, Short bShort, Integer bInt, Long bLong, Float bFloat, Double bDouble, Character bChar, Boolean bBoolean) {
        this.aByte = aByte;
        this.aShort = aShort;
        this.intPk = aInt;
        this.aLong = aLong;
        this.aFloat = aFloat;
        this.aDouble = aDouble;
        this.aChar = aChar;
        this.aBoolean = aBoolean;
        this.bByte = bByte;
        this.bShort = bShort;
        this.bInt = bInt;
        this.bLong = bLong;
        this.bFloat = bFloat;
        this.bDouble = bDouble;
        this.bChar = bChar;
        this.bBoolean = bBoolean;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public byte getaByte() {
        return aByte;
    }

    public void setaByte(byte aByte) {
        this.aByte = aByte;
    }

    public short getaShort() {
        return aShort;
    }

    public void setaShort(short aShort) {
        this.aShort = aShort;
    }

    public int getIntPk() {
        return intPk;
    }

    public void setIntPk(int intPk) {
        this.intPk = intPk;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public char getaChar() {
        return aChar;
    }

    public void setaChar(char aChar) {
        this.aChar = aChar;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Byte getbByte() {
        return bByte;
    }

    public void setbByte(Byte bByte) {
        this.bByte = bByte;
    }

    public Short getbShort() {
        return bShort;
    }

    public void setbShort(Short bShort) {
        this.bShort = bShort;
    }

    public Integer getbInt() {
        return bInt;
    }

    public void setbInt(Integer bInt) {
        this.bInt = bInt;
    }

    public Long getbLong() {
        return bLong;
    }

    public void setbLong(Long bLong) {
        this.bLong = bLong;
    }

    public Float getbFloat() {
        return bFloat;
    }

    public void setbFloat(Float bFloat) {
        this.bFloat = bFloat;
    }

    public Double getbDouble() {
        return bDouble;
    }

    public void setbDouble(Double bDouble) {
        this.bDouble = bDouble;
    }

    public Character getbChar() {
        return bChar;
    }

    public void setbChar(Character bChar) {
        this.bChar = bChar;
    }

    public Boolean getbBoolean() {
        return bBoolean;
    }

    public void setbBoolean(Boolean bBoolean) {
        this.bBoolean = bBoolean;
    }
}
