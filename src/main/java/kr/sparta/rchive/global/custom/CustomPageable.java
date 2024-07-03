package kr.sparta.rchive.global.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomPageable implements Pageable {

    private int page;
    private int size;
    private Sort sort;

    public CustomPageable(int page, int size, Sort sort) {
        this.page = page - 1;
        this.size = size;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return (long) page * size;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new CustomPageable(page + 2, size, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new CustomPageable(page, size, sort) : first();
    }

    @Override
    public Pageable first() {
        return new CustomPageable(1, size, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new CustomPageable(pageNumber + 1, size, sort);
    }

    @Override
    public boolean hasPrevious() {
        return page > 0;
    }
}
