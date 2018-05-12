package com.smx.dto;

import java.io.Serializable;
import java.util.List;

public class DateBillWsDTO implements Serializable {

    private String date;

    List<BillWsDTO> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BillWsDTO> getList() {
        return list;
    }

    public void setList(List<BillWsDTO> list) {
        this.list = list;
    }
}