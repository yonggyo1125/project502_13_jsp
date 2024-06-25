package org.choongang.pokemon.entities.api;

import lombok.Data;

import java.util.List;

@Data
public class ApiResult<T> {
    private long count;
    private String next;
    private String previous;
    private List<T> results;
}
