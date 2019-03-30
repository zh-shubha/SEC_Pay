package com.example.shubha.myapplication;

public class CustomClass {

    String Name;
    String Reg;
    String Trx;
    String Money;
    String Date;

    public CustomClass(String name, String reg, String trx, String money, String date) {
        Name = name;
        Reg = reg;
        Trx = trx;
        Money = money;
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getReg() {
        return Reg;
    }

    public void setReg(String reg) {
        Reg = reg;
    }

    public String getTrx() {
        return Trx;
    }

    public void setTrx(String trx) {
        Trx = trx;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
