package com.codeoftheweb.salvo.Clases;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "gameId", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayer;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> score;


    public Game() {
    }


    public Game(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @JsonIgnore
    public List<Player> getPlayer() {
        return gamePlayer.stream().map(sub -> sub.getPlayerId()).collect(Collectors.toList());

    }

    @JsonIgnore
    public List<Player> getPlayerSc() {
        return score.stream().map(sub -> sub.getPlayerSc()).collect(Collectors.toList());

    }





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Object> makeGameDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers", this.getGamePlayer()
                .stream()
                .map(gamePlayer -> gamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("scores", this
                .getGamePlayer()
                .stream()
                .map(gamePlayer1 ->
                        {


            if(gamePlayer1.getScoreGP().isPresent()){


                return gamePlayer1.getScoreGP().get().makeScoreDTO();

            }
            else
            {return "en curso";}
        }
        ));

        return dto;

    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(Set<GamePlayer> gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Set<Score> getScore() {
        return score;
    }

    public void setScore(Set<Score> score) {
        this.score = score;
    }
}