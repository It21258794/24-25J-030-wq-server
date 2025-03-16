package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginationDto<T> {

    private List<T> content;
    private Page pagination = null;

    public PaginationDto(List<T> content, org.springframework.data.domain.Page page){
        this.content = content;
        Page p = new Page();
        p.setLast(page.isLast());
        p.setPage(page.getNumber());
        p.setTotalElements(page.getTotalElements());
        p.setTotalPages(page.getTotalPages());
        p.setSize(page.getSize());
        this.pagination = p;
    }

    @Getter
    @Setter
    public static class Page {
        private int size;
        private int page;
        private long totalElements;
        private long totalPages;
        private boolean isLast;
    }
}
