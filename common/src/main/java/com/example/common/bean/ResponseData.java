package com.example.common.bean;

import java.util.List;

public class ResponseData <T>{
    int current;
    int size;
    int total;
    List<T> records;
}
