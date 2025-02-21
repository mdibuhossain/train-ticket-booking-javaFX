package com.example.hellofx.models;

public class Train {
    private int train_id;
    private String train_name;

    public Train(int train_id, String train_name) {
        this.train_id = train_id;
        this.train_name = train_name;
    }

    public Train(String train_name) {
        this.train_name = train_name;
    }

    public Train(Train tmpTrain) {
        this.train_id = tmpTrain.train_id;
        this.train_name = tmpTrain.train_name;
    }

    public int getTrain_id() {
        return train_id;
    }

    public void setTrain_id(int train_id) {
        this.train_id = train_id;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }
}
