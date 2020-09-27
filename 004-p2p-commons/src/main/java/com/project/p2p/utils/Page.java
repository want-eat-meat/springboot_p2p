package com.project.p2p.utils;

import java.io.Serializable;

public class Page implements Serializable {
    /**
     * 第一项下标
     */
    private Integer start;
    /**
     * 每页个数
     */
    private Integer count = 9;
    /**
     * 总个数
     */
    private Integer total;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 最后一页第一项下标
     */
    private Integer lastIndex;
    /**
     * 是否有下一页
     */
    private boolean isHasNext;
    /**
     * 是否有上一页
     */
    private boolean isHasPre;
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 携带的数据
     */
    private Object data;

    /**
     * 获取页面数量
     */
    private void setTotalPage(){
        if(total % count == 0){
            totalPage =  total / count;
        }else{
            totalPage =  total / count + 1;
        }
    }

    /**
     * 获取最后一页开始值
     */
    private void setLastIndex(){
        if(total % count == 0){
            lastIndex = total - count - 1;
        }else{
            lastIndex =  total - total % count;
        }
    }

    /**
     * 是否有下一页
     */
    private void setIsHasNext(){
        isHasNext =  !start.equals(getLastIndex());
    }

    /**
     * 是否有上一页
     */
    private void setIsHasPre(){
        isHasPre = !start.equals(0);
    }

    /**
     * 当前是多少页
     */
    private void setCurrentPage() {
       currentPage = start / count + 1;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
        setTotalPage();
        setLastIndex();
        setIsHasNext();
        setIsHasPre();
        setCurrentPage();
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getLastIndex() {
        return lastIndex;
    }

    public boolean isHasNext() {
        return isHasNext;
    }

    public boolean isHasPre() {
        return isHasPre;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }
}
