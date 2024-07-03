package org.choongang.pokemon.game.entities;

import lombok.Builder;
import lombok.Data;
import org.choongang.pokemon.game.constants.GameResult;

import java.time.LocalDateTime;

@Data
@Builder
public class GameLog {
    private long seq;
    private long userNo;
    private long userSeq;
    private long userScore;
    private long comSeq;
    private long comScore;
    private GameResult gameResult;
    private LocalDateTime regDt;
}
