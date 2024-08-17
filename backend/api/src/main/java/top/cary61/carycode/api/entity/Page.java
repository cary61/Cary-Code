package top.cary61.carycode.api.entity;

import lombok.Data;

@Data
public class Page<T> {

    private Integer pageId;

    private Integer pagesTotal;

    private T content;
}
