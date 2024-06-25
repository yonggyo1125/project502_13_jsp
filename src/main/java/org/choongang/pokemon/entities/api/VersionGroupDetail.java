package org.choongang.pokemon.entities.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionGroupDetail {
    private int level_learned_at;
    private Item move_learn_method;
    private Item version_group;
}
