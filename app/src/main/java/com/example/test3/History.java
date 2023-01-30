package com.example.test3;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "kc")
    private String kc;

    @ColumnInfo(name = "time")
    private String time;

    public History(String kc, String time) {
        this.kc = kc;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKc() {
        return kc;
    }

    public void setKc(String kc) {
        this.kc = kc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
