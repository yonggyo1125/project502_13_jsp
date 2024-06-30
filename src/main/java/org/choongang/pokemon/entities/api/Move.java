package org.choongang.pokemon.entities.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Move {
    private Item move;
    private List<VersionGroupDetail> version_group_details;
    private String name;
    private int order;
    private Item species;

}
