package com.example.common.bean;

import java.util.List;

public class ResponseData <T>{
    public int current;
    public int size;
    public int total;
    public List<T> records;
}
