package com.smx.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangh on 2017/10/23.
 */
public class DateBillListRespWsDTO implements Serializable {

    private int code;
    private String msg;
    private List<DateBillWsDTO> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DateBillWsDTO> getData() {
        return data;
    }

    public void setData(List<DateBillWsDTO> data) {
        this.data = data;
    }
}
