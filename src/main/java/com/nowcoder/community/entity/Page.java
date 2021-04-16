package com.nowcoder.community.entity;

import lombok.Getter;
@Getter
public class Page {
    //当前的页码
    private int current = 1;
//    显示上限
    private int limit = 10;
//    数据总数（用于计算总页数）
    private int rows;
//    查询路径（用于复用分页链接）
    private String path;

    public void setCurrent(int current) {
        if(current >= 1) this.current = current;
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <= 100)this.limit = limit;
    }

    public void setRows(int rows) {
        if(rows > 0)this.rows = rows;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     *  获取当前页的起始行，页面的第一行
     * @return
     */
    public int getOffset(){
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal(){
        if(rows % limit == 0){
            return rows % limit;
        }
        return rows % limit + 1;
    }

    /**
     * 获取页脚显示页码的第一个
     * @return
     */
    public int getFrom(){
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     *  获取页脚显示页码的最后一个
     * @return
     */
    public int getTo(){
        int to = current + 2;
        return to > getTotal() ? getTotal() : to;
    }

}
