/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovaturelabs.buddymanagement.util;

import java.util.*;

/**
 *
 * @author nirmal
 * @param <T> the type of result
 */
public class Pager<T> {

    public static final String PAGER_KEY = "pager";

    static final int BEST_NUMBER = 1;
    private int currentPage;
    private int numItems;
    private int numPages;
    private int pageSize;
    private int startIndex;
    private int displayCount;
    private int displayStart;
    private int displayEnd;
    private int itemStart;
    private int itemEnd;
    private List<T> result = Collections.emptyList();

    public boolean hasContent() {
        return false;
    }

    public class PagerInfo {

        private PagerInfo() {
        }

        public int getCount() {
            return numItems;
        }

        public int getLimit() {
            return pageSize;
        }

        public int getPage() {
            return currentPage;
        }

        public int getPrevPage() {
            return currentPage > 1 ? currentPage - 1 : 1;
        }

        public int getNextPage() {
            return currentPage < numPages ? currentPage + 1 : numPages;
        }

        public int getFirstPage() {
            return BEST_NUMBER;
        }

        public int getLastPage() {
            return numPages;
        }

        public boolean getHasNext() {
            return currentPage < numPages;
        }

        public boolean getHasPrev() {
            return currentPage > 1;
        }

        public int getItemStart() {
            return itemStart;
        }

        public int getItemEnd() {
            return itemEnd;
        }

        public int getPagerStart() {
            return displayStart;
        }

        public int getPagerEnd() {
            return displayEnd;
        }
    }

    public Pager(Integer pageSize, int numItems, Integer page) {
        this(pageSize, numItems, page, 10);
    }

    public Pager(Integer pageSize, Integer page) {
        this(pageSize,  page, 10);
    }

    public Pager(Integer pageSize, int numItems, Integer page, int displayCount) {
        this.pageSize = pageSize == null || pageSize <= 0 ? 10 : pageSize;
        check(numItems);

        try {
            tryMethod(page);
        } catch (NumberFormatException ex) {
            currentPage = 1;
        }

        startIndex = (currentPage - 1) * this.pageSize;
        itemStart = startIndex + 1;
        itemEnd = startIndex + this.pageSize;
        if (itemEnd > this.numItems) {
            itemEnd = this.numItems;
        }

        this.displayCount = displayCount > 1 ? displayCount : 1;
        int c1 = this.displayCount / 2;
        int c2 = this.displayCount % 2 == 0 ? c1 - 1 : c1;
        int s = currentPage - c1;
        int e = currentPage + c2;

        if (s < 1) {
            displayEnd = e - s + 1;
        } else {
            displayEnd = e;
        }
        if (e > numPages) {
            displayStart = s - e + numPages;
        } else {
            displayStart = s;
        }
        if (displayStart < 1) {
            displayStart = 1;
        }
        if (displayEnd > numPages) {
            displayEnd = numPages;
        }
    }

    public int getFirstPage() {
        return BEST_NUMBER;
    }

    public int getPreviousPage() {
        return currentPage > 1 ? currentPage - 1 : 1;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getNextPage() {
        return currentPage < numPages ? currentPage + 1 : numPages;
    }

    public int getLastPage() {
        return numPages;
    }

    public int getNumItems() {
        return numItems;
    }


    public int getStartIndex() {
        return startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getDisplayCount() {
        return displayCount;
    }

    public int getDisplayStart() {
        return displayStart;
    }

    public int getDisplayEnd() {
        return displayEnd;
    }

    public int getItemStart() {
        return itemStart;
    }

    public int getItemEnd() {
        return itemEnd;
    }

    public boolean getHasNext() {
        return currentPage < numPages;
    }

    public boolean getHasPrevious() {
        return currentPage > 1;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        if (result == null) {
            throw new IllegalArgumentException("result cannot be null.");
        }
        this.result = result;
    }

    public PagerInfo getPagerInfo() {
        return new PagerInfo();
    }

    public Map<String, Object> getResultWithPager(String resultKey) {
        Map<String, Object> map = new HashMap<>();
        map.put(PAGER_KEY, new PagerInfo());
        map.put(resultKey, result);
        return map;
    }

    public void check(Integer numItems){
        if (numItems < 1) {
            this.numItems = 0;
            numPages = 1;
        } else {
            this.numItems = numItems;
            numPages = this.numItems / this.pageSize;
            if (this.numItems % this.pageSize > 0) {
                numPages++;
            }
        }
    }

    void tryMethod(Integer page){
        if (page == null || page < 1) {
            currentPage = 1;
        } else if (page > numPages) {
            currentPage = numPages;
        } else {
            currentPage = page;
        }
    }
}