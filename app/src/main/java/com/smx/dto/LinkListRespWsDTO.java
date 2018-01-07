package com.smx.dto;

/**
 * Created by wangh on 2017/10/23.
 */
public class LinkListRespWsDTO {

    private int code;
    private String msg;
    private LinkListWsDTO data;

    public LinkListRespWsDTO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public LinkListRespWsDTO(int code, String msg, LinkListWsDTO data) {
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

    public LinkListWsDTO getData() {
        return data;
    }

    public void setData(LinkListWsDTO data) {
        this.data = data;
    }
}
