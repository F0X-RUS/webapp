package ru.sgmu.seem.utils;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageWrapper<T> {

    private Page<T> page;
    private int maxPageItemsDisplay;
    private List<PageItem> items;
    private int number;
    private String url;

    public PageWrapper(Page<T> page, String url, int maxPageItemsDisplay) {
        this.page = page;
        this.url = url;
        this.maxPageItemsDisplay = maxPageItemsDisplay;
        items = new ArrayList<>();
        number = page.getNumber() + 1;

        int start;
        int size;
        if (page.getTotalPages() <= maxPageItemsDisplay){
            start = 1;
            size = page.getTotalPages();
        } else {
            if (number <= maxPageItemsDisplay - maxPageItemsDisplay/2){
                start = 1;
                size = maxPageItemsDisplay;
            } else if (number >= page.getTotalPages() - maxPageItemsDisplay/2){
                start = page.getTotalPages() - maxPageItemsDisplay + 1;
                size = maxPageItemsDisplay;
            } else {
                start = number - maxPageItemsDisplay/2;
                size = maxPageItemsDisplay;
            }
        }

        for (int i = 0; i<size; i++){
            items.add(new PageItem(start+i, (start+i)==number));
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMaxPageItemsDisplay() {
        return maxPageItemsDisplay;
    }

    public List<PageItem> getItems() {
        return items;
    }

    public int getNumber() {
        return number;
    }

    public List<T> getContent(){
        return page.getContent();
    }

    public int getSize(){
        return page.getSize();
    }

    public int getTotalPages(){
        return page.getTotalPages();
    }

    public long getTotalElements() {return page.getTotalElements();}

    public boolean isFirstPage(){
        return page.isFirst();
    }

    public boolean isLastPage(){
        return page.isLast();
    }

    public boolean isHasPreviousPage(){
        return page.hasPrevious();
    }

    public boolean isHasNextPage(){
        return page.hasNext();
    }

    public class PageItem {
        private int number;
        private boolean enable;

        public PageItem(int number, boolean enable) {
            this.number = number;
            this.enable = enable;
        }

        public int getNumber() {
            return number;
        }

        public boolean isEnable() {
            return enable;
        }
    }
}
