package com.sign.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author MM
 */
public class PageData<M> {

    private Integer page = 1;

    private Integer size = 10;

    private Integer total;

    private Integer pageTotal;

    private Integer startPage = 1;

    private Integer endPage = 10;

    /**
     * 扩展参数
     */
    private Map<String, Object> attach = new HashMap<String, Object>();

    private List<M> data = new ArrayList<M>();

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page == null || page <= 0) {
            return;
        }
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if (size == null || size <= 0) {
            return;
        }
        this.size = size;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        if (total == null) {
            total = 0;
        }
        this.total = total;
    }

    public Integer getPageTotal() {
        this.pageTotal = total % size == 0 ? total / size : (total / size) + 1;
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Integer getStartPage() {
        if (getEndPage() < 10) {
            return startPage;
        }
        return (getEndPage() + 1) - 10;
    }

    public Integer getEndPage() {
        if (getPageTotal() <= endPage) {
            return getPageTotal();
        }
        if (getPage() >= getPageTotal()) {
            return getPageTotal();
        }
        int i = 3 - (endPage - getPage());
        if (i > 0) {
            if ((page + 3) >= getPageTotal()) {
                return getPageTotal();
            } else {
                return page + 3;
            }
        }
        return endPage;
    }

    public List<M> getData() {
        return data;
    }

    public void setData(List<M> data) {
        this.data = data;
    }

    public Map<String, Object> getAttach() {
        return attach;
    }

    public void setAttach(Map<String, Object> attach) {
        this.attach = attach;
    }

    @Override
    public String toString() {
        return "PageData{" +
                "page=" + page +
                ", size=" + size +
                ", total=" + total +
                ", pageTotal=" + pageTotal +
                ", startPage=" + startPage +
                ", endPage=" + endPage +
                ", attach=" + attach +
                ", data=" + data +
                '}';
    }
}
