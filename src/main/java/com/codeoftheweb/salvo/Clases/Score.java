package com.codeoftheweb.salvo.Clases;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private LocalDateTime EndDate;

    private float Point;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameSc")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerSc")
    private Player playerSc;

    public Score() {    }

    public Score(LocalDateTime endDate, float point, Game gameSc, Player playerSc) {
        EndDate = endDate;
        Point = point;
        this.game = gameSc;
        this.playerSc = playerSc;
    }

    public Map<String, Object> makeScoreDTO(){
        Map<String, Object> dto= new LinkedHashMap<>();
        dto.put("player", this.getPlayerSc().getId());
        dto.put("game", this.getGame().getId());
        dto.put("score", this.getPoint());
        dto.put("finishDate", this.getEndDate());

        return dto;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        EndDate = endDate;
    }

    public float getPoint() {
        return Point;
    }

    public void setPoint(float point) {
        Point = point;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayerSc() {
        return playerSc;
    }

    public void setPlayerSc(Player playerSc) {
        this.playerSc = playerSc;
    }
}