package org.choongang.pokemon.entities;

import lombok.Data;

import java.util.List;

@Data
public class ApiResult<T> {
    private long count;
    private String next;
    private String previous;
    private List<T> results;
}
