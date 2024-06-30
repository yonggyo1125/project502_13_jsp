package org.choongang.pokemon.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.entities.api.Pokemon;
import org.choongang.pokemon.mappers.PokemonMapper;

@Service
@RequiredArgsConstructor
public class PokemonSaveService {
    private final PokemonMapper mapper;

    public boolean save(Pokemon data) {

        String type1 = data.getTypes().get(0) != null ? data.getTypes().get(0).getType().getName() : "";
        String type2 = data.getTypes().get(1) != null ? data.getTypes().get(1).getType().getName() : "";
        PokemonDetail detail = PokemonDetail.builder()
                .seq(data.getId())
                .name(data.getName())
                .height(data.getHeight())
                .weight(data.getWeight())
                .baseExperience(data.getBase_experience())
                .frontImage(data.getSprites().getOther().getOfficial_artwork().getFront_default())
                .backImage(data.getSprites().getBack_default())
                .rawData(data.getRawData())
                .nameKr(data.getNameKr())
                .description(data.getDescription())
                .type1(type1)
                .type2(type2)
                .build();
        int result = mapper.register(detail);

        return result > 0;
    }
}
