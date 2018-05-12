package com.smx.dto;


import java.util.List;

public class MessageListRespWsDTO {

    private int code;
    private String msg;
    List<MessageWsDTO> data;

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

    public List<MessageWsDTO> getData() {
        return data;
    }

    public void setData(List<MessageWsDTO> data) {
        this.data = data;
    }
}
