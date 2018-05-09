package com.smx.dto;

import java.util.List;

/**
 * Created by wangh on 2017/10/23.
 */
public class BillListRespWsDTO {

    private int code;
    private String msg;
    private List<BillListWsDTO> data;

    public BillListRespWsDTO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public BillListRespWsDTO(int code, String msg, List<BillListWsDTO> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

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

    public List<BillListWsDTO> getData() {
        return data;
    }

    public void setData(List<BillListWsDTO> data) {
        this.data = data;
    }
}
