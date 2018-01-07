package com.smx.dto;

import java.util.List;

public class LinkListWsDTO {

    private int pageNum;
    private int pageSize;
    private long total;
    private int pages;
    List<LinkWsDTO> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<LinkWsDTO> getList() {
        return list;
    }

    public void setList(List<LinkWsDTO> list) {
        this.list = list;
    }
}