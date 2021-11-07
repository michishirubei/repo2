package com.bjpowernode.vo;

//分页数据类
public class Page {
    //页号
    private Integer pageNo;
    //每页数据大小
    private Integer pageSize;
    //总页数
    private Integer totalPage;
    //总记录数量
    private Integer totalRecord;

    public Page() {
        this.pageNo = 1;
        this.pageSize = 9;
        this.totalPage = 0;
        this.totalRecord = 0;
    }

    public Page(Integer pageNo, Integer pageSize, Integer totalRecord) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Integer getTotalPage() {
        //计算出来的
        if( totalRecord % pageSize == 0 ){
            totalPage = totalRecord / pageSize;
        } else {
            totalPage = totalRecord / pageSize +1;
        }
        return totalPage;
    }

}
