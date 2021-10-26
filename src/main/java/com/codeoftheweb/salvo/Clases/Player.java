package com.codeoftheweb.salvo.Clases;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
        private String userName;

        private String Password;

    @OneToMany(mappedBy="playerId", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayer;

    @OneToMany(mappedBy="playerSc", fetch=FetchType.EAGER)
    private Set<Score> score;

    public Player() { }

    public Map<String, Object> makePlayerDTO(){
        Map<String, Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("email", this.getUserName());

        return dto;
    }

    public Optional<Score> getScorePl(Game juego){
        return this
                .getScore()
                .stream()
                .filter(b->b.getGame().getId().equals(juego.getId())).findFirst();
    }


    @JsonIgnore
    public List<Game> getGame() {
        return gamePlayer.stream().map(sub -> sub.getGameId()).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Game> getGameSc() {
        return score.stream().map(sub -> sub.getGame()).collect(Collectors.toList());
    }

    public Player(String userName, String password) {
        this.userName = userName;
        Password = password;
    }

    public String getPassword() {return Password;}

    public void setPassword(String password) {Password = password;}

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<GamePlayer> getGamePlayer() {
        return gamePlayer;
    }
    public void setGamePlayer(Set<GamePlayer> gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Set<Score> getScore() {
        return score;
    }

    public void setScore(Set<Score> score) {
        this.score = score;
    }
}

