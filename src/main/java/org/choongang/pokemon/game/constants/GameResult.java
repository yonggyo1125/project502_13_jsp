package org.choongang.pokemon.game.constants;

public enum GameResult {
    WIN("사용자 WIN"), // 사용자 승
    LOSE("컴퓨터 WIN"), // 컴퓨터 승
    DRAW("무승부"); // 무승부

    private final String title;

    GameResult(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
