    package com.codeoftheweb.salvo.Clases;
    
    import com.codeoftheweb.salvo.Repos.PlayerRepository;
    import com.codeoftheweb.salvo.Repos.ScoreRepository;
    import org.hibernate.annotations.GenericGenerator;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;


    import javax.persistence.*;
    import java.time.LocalDateTime;
    import java.util.*;
    import java.util.stream.Collectors;
    
        @Entity
        public class GamePlayer {


            @Id
            @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
            @GenericGenerator(name = "native", strategy = "native")
            private Long id;
            private LocalDateTime joinDate;
    
            @OneToMany(mappedBy="gamePlayerID", fetch=FetchType.EAGER)
            private Set<Ship> ships;
    
            @OneToMany(mappedBy="gamePlayerID", fetch=FetchType.EAGER)
            private Set<Salvo> salvo;
    
            @ElementCollection
            @Column(name="Self")
            private List<String> Self = new ArrayList<>();
    
            @ElementCollection
            @Column(name="Oponent")
            private List<String> Oponent = new ArrayList<>();
    
    
            @ManyToOne(fetch = FetchType.EAGER)
            @JoinColumn(name="gameId")
            private Game gameId;
    
            @ManyToOne(fetch = FetchType.EAGER)
            @JoinColumn(name="playerId")
            private Player playerId;
    
            public GamePlayer() { }

            private boolean barcosHundidos(GamePlayer gpBarcos, GamePlayer gpSalvos) {

                if (!gpBarcos.getShip().isEmpty() && !gpSalvos.getSalvo().isEmpty()) {
                    return  gpSalvos.getSalvo().stream().flatMap(salvo -> salvo.getSalvoLocations()
                                    .stream()).collect(Collectors.toList())
                            .containsAll(gpBarcos.getShip()
                                    .stream().flatMap(ship -> ship.getShipLocations().stream())
                                    .collect(Collectors.toList()));
                }
                return false;
            }

            public String GameState() {

                if (this.getGameId().getGamePlayer().size() != 2) {
                    return "WAITINGFOROPP";
                }
                GamePlayer enemigo = this.getGameId().getGamePlayer().stream().filter(b -> !b.getId().equals(this.getId())).findFirst().get();

                if (this.getShip().size() != 5) {
                    return "PLACESHIPS";
                }

                if (enemigo.getShip().size() != 5) {
                    return "WAITINGFOROPP";
                }

                if ((this.getSalvo().size()>2 && enemigo.getSalvo().size()>2 )&&(this.getSalvo().size()== enemigo.getSalvo().size() )){
                String jug_gana="";
                String ene_gana="";
//daasdasdsadad

                    if (barcosHundidos(this,enemigo)) {
                        ene_gana = "Gano";
                    }
                    if (barcosHundidos(enemigo,this)) {
                        jug_gana = "Gano";
                    }
                if (jug_gana.equals("Gano") && ene_gana.equals("Gano")) {
                    return "TIE";
                }
                if (jug_gana.equals("Gano")) {
                    return "WON";
                }
                if (ene_gana.equals("Gano")) {
                    return "LOST";
                }
            }
                if(enemigo.getId()<this.getId()){
                    if(enemigo.getSalvo().size()<=this.getSalvo().size()){return "WAIT";}
                    else {return "PLAY";}
                }
                if(enemigo.getId()>this.getId()){
                    if(enemigo.getSalvo().size()>=this.getSalvo().size()){return "PLAY";}
                    else {return "WAIT";}
                }

                return "UNDEFINED";            }


                public Optional<Score> getScoreGP(){
    
            return  this.
                    getPlayerId().getScorePl(this.gameId);
    
            }
    
            int cuenta_carrier=0;
            int cuenta_submarine=0;
            int cuenta_battleship=0;
            int cuenta_destroyer=0;
            int cuenta_patrolboat=0;
            public Map<String,Object> makeDamagesDTO(int turnitos, GamePlayer enemigo,int funcion){
                ArrayList<String> hits_pegados = new ArrayList<String>();
                Map<String,Object> dto= new LinkedHashMap<>();
    
                int carrier_pre=0;
                int submarine_pre=0;
                int battleship_pre=0;
                int destroyer_pre=0;
                int patrolboat_pre=0;
                Map<String, Object> DTO_turno= new LinkedHashMap<>();
                Map<String, Object> DTO_danios  = new LinkedHashMap<>();


                int finalTurnitos = turnitos;
                Salvo Salvos_importen;
                if( enemigo.getSalvo()
                        .stream()
                        .filter(b->b.getTurn() == finalTurnitos)
                        .findFirst().isPresent()){
                 Salvos_importen = enemigo.getSalvo()
                        .stream()
                        .filter(b->b.getTurn() == finalTurnitos)
                        .findFirst()
                        .get();}
                else { Salvos_importen=enemigo.getSalvo()
                        .stream()
                        .filter(b->b.getTurn() == finalTurnitos-1)
                        .findFirst()
                        .get();
                }
    //    System.out.println("hola, estoy andandOwO" + finalTurnitos);
    
                List<String> Salvos_turno= Salvos_importen.getSalvoLocations();
    
                for (String c : this.getShip().stream().map(Ship::getType).collect(Collectors.toList())) {

    
                    List<String> loca_barcos = (this.getShip().stream().filter(b ->b.getType().equals(c)).findFirst().get()).getShipLocations();

                    for (String b : loca_barcos) {
                        DTO_turno.put("turn",finalTurnitos);
                        if (Salvos_turno.contains(b)) {
                            hits_pegados.add(b);
                            if (c.equals("carrier")) {
                                cuenta_carrier++;
                                carrier_pre++;
                            }
                            if (c.equals("submarine")) {
                                submarine_pre++;
                                cuenta_submarine++;
                            }
                            if (c.equals("battleship")) {
                                cuenta_battleship++;
                                battleship_pre++;
                            }
                            if (c.equals("destroyer")) {
                                destroyer_pre++;
                                cuenta_destroyer++;
    
    
                            }
                            if (c.equals("patrolboat")) {
                                patrolboat_pre++;
                                cuenta_patrolboat++;

                            }


                        }

    
                    }
                    DTO_turno.put("hitLocations",hits_pegados);
                    DTO_danios.put("patrolboatHits",patrolboat_pre);
                    DTO_danios.put("destroyerHits",destroyer_pre);
                    DTO_danios.put("carrierHits",carrier_pre);
                    DTO_danios.put("submarineHits",submarine_pre);
                    DTO_danios.put("battleshipHits",battleship_pre);
                    //                        DTO_danios.put("BARRITA DE SEPARACION FACHERA",".");
                    DTO_danios.put("carrier",cuenta_carrier);
                    DTO_danios.put("submarine",cuenta_submarine);
                    DTO_danios.put("battleship",cuenta_battleship);
                    DTO_danios.put("destroyer",cuenta_destroyer);
                    DTO_danios.put("patrolboat",cuenta_patrolboat);
                    DTO_turno.put("damages",DTO_danios);

                    DTO_turno.put("missed",Salvos_turno.size()-hits_pegados.size());

                }
                Ship carrier = this.getShip().stream().filter(sh -> sh.getType().equals("carrier")).findFirst().get();
                List <String> carrierLocations = carrier.getShipLocations();
                if(funcion==1){return DTO_danios;}
                else{return DTO_turno;}
            }
    
            public List<Object> Golpesitos(){
                 cuenta_carrier=0;
                 cuenta_submarine=0;
                 cuenta_battleship=0;
                 cuenta_destroyer=0;
                 cuenta_patrolboat=0;
    
                GamePlayer enemigo=this.getGameId().getGamePlayer().stream().filter(b-> !b.getId().equals(this.getId())).findFirst().get();


                ArrayList<Object> dto = new ArrayList<Object>();
                int turnitos=0;
    for(turnitos=1;turnitos<=enemigo.getSalvo().size();turnitos++) {
        dto.add(this.makeDamagesDTO(turnitos,enemigo,0));
    }
    
                return dto;
    
            }
    
    
            public GamePlayer(Game gameId, Player playerId, LocalDateTime joinDate ) {
                this.joinDate = joinDate;
                this.gameId = gameId;
                this.playerId = playerId;
            }
    
            public Map<String, Object>makeGamePlayerDTO(){
                Map<String, Object> dto= new LinkedHashMap<>();
                dto.put("id", this.getId());
                dto.put("player", this.getPlayerId().makePlayerDTO());
    
                return dto;
            }
    
            public Map<String, Object>makeUserDTO(){
                Map<String, Object> dto= new LinkedHashMap<>();
                dto.put("id", this.playerId.getId());
                dto.put("name", this.playerId.getUserName());
    
                return dto;
            }
    
            public Long getId() {
                return id;
            }
            public void setId(Long id) {
                this.id = id;
            }
    
    
            public LocalDateTime getJoinDate() {return joinDate;}
            public void setJoinDate(LocalDateTime joinDate) {this.joinDate = joinDate;}
    
            public Game getGameId() {
                return gameId;
            }
            public void setGameId(Game gameId) {
                this.gameId = gameId;
            }
    
    
            public List<String> getSelf() {return Self;}
            public void setSelf(List<String> self) {Self = self;}
    
            public List<String> getOponent() {return Oponent;}
            public void setOponent(List<String> oponent) {Oponent = oponent;}
    
            public Player getPlayerId() {return playerId;}
            public void setPlayerId(Player playerId) {this.playerId = playerId;}
    
            public Set<Ship> getShip() {
                return ships;
            }
            public void setShip(Set<Ship> ship) {
                this.ships = ship;
            }
    
            public Set<Salvo> getSalvo() {
                return salvo;
            }
            public void setSalvo(Set<Salvo> salvo) {
                this.salvo = salvo;
            }
    
                public Map<String, Object>makeGameViewDTO(){
                    Map<String, Object> dto= new LinkedHashMap<>();
                    dto.put("id", this.getGameId().getId());
                    dto.put("created", this.getGameId().getCreationDate());
                    dto.put("gameState", this.GameState());
                    dto.put("gamePlayers", this.getGameId().getGamePlayer()
                            .stream()
                            .map(gamePlayer-> gamePlayer.makeGamePlayerDTO())
                            .collect(Collectors.toList()));
                    dto.put("ships",this.getShip()
                            .stream()
                            .map(ship -> ship.makeShipDTO())
                            .collect(Collectors.toList()));
    
                    dto.put("salvoes",this
                            .getGameId()
                            .getGamePlayer()
                            .stream()
                            .flatMap(gamePlayer-> gamePlayer.getSalvo()
                                    .stream()
                                    .map(b->b.makeSalvoDTO())
                            ));




                    dto.put("hits",this.makeHitsDTO());
                    return dto;
            }
            public Map<String, Object>makeHitsDTO(){
                Map<String, Object> dto= new LinkedHashMap<>();
                List<String> Self_Hits;

                GamePlayer enemigo=null;
                if(this.getGameId().getGamePlayer().stream().anyMatch(b -> !b.getId().equals(this.getId())) )
                {enemigo =this.getGameId().getGamePlayer().stream().filter(b -> !b.getId().equals(this.getId())).findFirst().get(); }
    //
    //            List<List<String>> lista2_MiBarco = this.getSalvo().stream().map(b->b.getSalvoLocations()).collect(Collectors.toList());
    //            List<String> flatList_MiSalvo = lista2_MiBarco.stream()
    //                    .flatMap(List::stream)
    //                    .collect(Collectors.toList());
    //
    //            List<List<String>> lista = enemigo.getSalvo().s   tream().map(b->b.getSalvoLocations()).collect(Collectors.toList());
    //            List<String> flatList_SuSalvo = lista.stream()
    //                    .flatMap(List::stream)
    //                    .collect(Collectors.toList());
                ArrayList<Object> error1 = new ArrayList<Object>();
                ArrayList<Object> error2 = new ArrayList<Object>();
                if(enemigo==null){dto.put("self",error1);
                    dto.put("opponent",error2);}
                else{
                dto.put("self",this.Golpesitos());
                dto.put("opponent", enemigo.Golpesitos());}
    
    
                return dto;}
    
    
    }