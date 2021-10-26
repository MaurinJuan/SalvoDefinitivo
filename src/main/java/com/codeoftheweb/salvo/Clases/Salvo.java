package com.codeoftheweb.salvo.Clases;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class  Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private int turn;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayerID")
    private GamePlayer gamePlayerID;

    @ElementCollection
    @Column(name="salvoLocation")
    private List<String> salvoLocations = new ArrayList<>();



    public Salvo() { }

    public Salvo(int turn, GamePlayer gamePlayerID, List<String> salvoLocations) {
        this.turn = turn;
        this.gamePlayerID = gamePlayerID;
        this.salvoLocations = salvoLocations;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public int getTurn() {return turn;}

    public void setTurn(int turn) {
        this.turn = turn;}

    public GamePlayer getGamePlayerID() {return gamePlayerID;}

    public void setGamePlayerID(GamePlayer gamePlayerID) {this.gamePlayerID = gamePlayerID;}

    public List<String> getSalvoLocations() {return salvoLocations;}

    public void setSalvoLocations(List<String> salvoLocations) {this.salvoLocations = salvoLocations;}

    public Map<String,Object> makeSalvoDTO(){

        Map<String,Object> dto= new LinkedHashMap<>();

        dto.put("player", this.getGamePlayerID().getPlayerId().getId());
//        dto.put("player", this.getGamePlayerID().getGameId().getGamePlayer());
        dto.put("turn",this.getTurn());
        dto.put("locations",this.getSalvoLocations());

        return dto;
    }






}
