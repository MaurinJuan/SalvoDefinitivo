package com.codeoftheweb.salvo;
import com.codeoftheweb.salvo.Clases.*;
import com.codeoftheweb.salvo.Repos.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController{


    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private SalvoRepository salvoRepository;
    @Autowired
    private WebSecurityConfig webSecurityConfig;
    @Autowired
    private WebSecurityConfiguration webSecurityConfiguration;
    @Autowired
    PasswordEncoder passwordEncoder;



    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

@PostMapping(path="/games")
public  ResponseEntity<Map> createGame(Authentication authentication) {
    LocalDateTime Tiempo = LocalDateTime.now();
    Game newgame = gameRepository.save(new Game(Tiempo));
    GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(newgame,
            this.playerRepository.findByuserName(authentication.getName()), LocalDateTime.now()
    ));
    return new ResponseEntity<>(makeMap("gpid",newGamePlayer.getId()), HttpStatus.CREATED);
}


    @RequestMapping(value = "/game/{gameid}/players",method = {RequestMethod.POST,RequestMethod.GET})
    public  ResponseEntity<Map> joinGameButton(@PathVariable Long gameid,Authentication authentication) {

if(playerRepository.findByuserName(authentication.getName())==null){
    return new ResponseEntity<>(makeMap("error","No esta autorizado"), HttpStatus.UNAUTHORIZED);
}
if(gameRepository.findAll().stream().noneMatch(b->b.getId().equals(gameid))){
    return new ResponseEntity<>(makeMap("error","No existe este juego ;C"), HttpStatus.FORBIDDEN);
}
if( gameRepository.findById(gameid).get().getGamePlayer().size() >=2){
    return new ResponseEntity<>(makeMap("error","Ya hay muchos jugadores, perdon ;c"), HttpStatus.FORBIDDEN);
}
//playerRepository.findByuserName(authentication.getName())
if(gameRepository.findById(gameid).get().getPlayer().stream().anyMatch(b->b.getId().equals(playerRepository.findByuserName(authentication.getName()).getId()))){
            return new ResponseEntity<>(makeMap("error","NO INTENTE ENTRAR A UNA PARTIDA CON VOS MISMO >:c"), HttpStatus.FORBIDDEN);
        }
        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(
                gameRepository.findById(gameid).get(), playerRepository.findByuserName(authentication.getName()), LocalDateTime.now()
        ));
        return new ResponseEntity<>(makeMap("gpid",1), HttpStatus.CREATED);    }







    @RequestMapping(value = "/games/players/{gameplayerid}/ships",method = RequestMethod.POST)
    public  ResponseEntity<Map> PlaceShips(@PathVariable Long gameplayerid
            , @RequestBody List<Ship> barco
            ,Authentication authentication) {
        if(playerRepository.findByuserName(authentication.getName())==null){
            return new ResponseEntity<>(makeMap("error","No esta autorizado"), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayerRepository.findAll().stream().noneMatch(b->b.getId().equals(gameplayerid))){
            return new ResponseEntity<>(makeMap("error","No existe este GP ;C"), HttpStatus.FORBIDDEN);
        }
        if(gamePlayerRepository.findById(gameplayerid).get().getShip().size()>0){
            return new ResponseEntity<>(makeMap("error","ya hay barcos  ;C"), HttpStatus.FORBIDDEN);
        }
        if(playerRepository.findByuserName(authentication.getName()).getGamePlayer().stream().noneMatch(b->b.getId().equals(gameplayerid))){
            return new ResponseEntity<>(makeMap("error","NO ES TU JUGADOR >:C"), HttpStatus.FORBIDDEN);
        }
        for(Ship i:barco ){
            if(i.getType().equals("carrier") && i.getShipLocations().size()!=5){
                System.out.println("no anda el 1");
                return new ResponseEntity<>(makeMap("error","Error en el posicionamiento de barcos!"), HttpStatus.FORBIDDEN);
            }
            if(i.getType().equals("submarine") && i.getShipLocations().size()!=3){
                System.out.println("no anda el 2");
                return new ResponseEntity<>(makeMap("error","Error en el posicionamiento de barcos!"), HttpStatus.FORBIDDEN);
            }
            if(i.getType().equals("battleship") && i.getShipLocations().size()!=4){
                System.out.println("no anda el 3");
                return new ResponseEntity<>(makeMap("error","Error en el posicionamiento de barcos!"), HttpStatus.FORBIDDEN);
            }
            if(i.getType().equals("destroyer") && i.getShipLocations().size()!=3){
                System.out.println("no anda el 4");
                return new ResponseEntity<>(makeMap("error","Error en el posicionamiento de barcos!"), HttpStatus.FORBIDDEN);
            }
            if(i.getType().equals("patrolboat") && i.getShipLocations().size()!=2){
                System.out.println("no anda el 5");
                return new ResponseEntity<>(makeMap("error","Error en el posicionamiento de barcos!"), HttpStatus.FORBIDDEN);
            }
            if(!i.getType().equals("patrolboat")&&
               !i.getType().equals("destroyer")&&
               !i.getType().equals("battleship")&&
               !i.getType().equals("submarine")&&
               !i.getType().equals("carrier")){
                System.out.println("no anda el 6");
                return new ResponseEntity<>(makeMap("error","Error en el nombre de barcos!"), HttpStatus.FORBIDDEN);


            }
        }

//     for(Ship c:barco){
//         c.setGamePlayerID(gamePlayerRepository.getById(gameplayerid));
//         shipRepository.save(c);
//
//     }
        barco.forEach(b->b.setGamePlayerID(gamePlayerRepository.findById(gameplayerid).get()));

        barco.forEach(b->shipRepository.save(b));

        return new ResponseEntity<>(makeMap("OK","Barcos puestos!"), HttpStatus.CREATED);
    }



    @RequestMapping(value = "/games/players/{gameplayerid}/ships",method = RequestMethod.GET)
    public  ResponseEntity<Map> PlaceShips(@PathVariable Long gameplayerid
            ,Authentication authentication) {
        if(gamePlayerRepository.findById(gameplayerid).isPresent()){
                Map<String, Object> dto = new LinkedHashMap<>();
                dto.put("ship",gamePlayerRepository.findById(gameplayerid).get().getShip().stream().map(b -> b.makeShipDTO()).collect(Collectors.toList()));

                return new ResponseEntity<>(dto, HttpStatus.CREATED);             }
        return new ResponseEntity<>(makeMap("error","Este gameplayer no esciste"), HttpStatus.FORBIDDEN);
        }



    @RequestMapping(value = "/games/players/{gameplayerid}/salvoes",method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> guardatiritos(@PathVariable Long gameplayerid
            , @RequestBody Salvo salvoLocations
            , Authentication authentication){

        if(playerRepository.findByuserName(authentication.getName())==null){


            return new ResponseEntity<>(makeMap("Sin permisos",0), HttpStatus.UNAUTHORIZED);}

        if(gamePlayerRepository.findAll().stream().noneMatch(a->a.getId().equals(gameplayerid))){
            return new ResponseEntity<>(makeMap("No disponiblea",0), HttpStatus.FORBIDDEN);

        }

        if(playerRepository.findByuserName(authentication.getName()).getGamePlayer().stream().noneMatch(a->a.getId().equals(gameplayerid))){
            return new ResponseEntity<>(makeMap("No disponibleb",0), HttpStatus.UNAUTHORIZED);
        }
        Player authenticatedPlayer= playerRepository.findByuserName(authentication.getName());

        if(gamePlayerRepository.findById(gameplayerid).get().getPlayerId().getId()!=authenticatedPlayer.getId()){
            return  new ResponseEntity<>(makeMap("error","naciste en argetina"),HttpStatus.UNAUTHORIZED);
        }


        int max = 0;
        if(gamePlayerRepository.findById(gameplayerid).get().getSalvo().size()==0)
                    {max=1;}
        else {
            max = Collections.max(gamePlayerRepository.findById(gameplayerid).get().getSalvo().stream().map(b -> b.getTurn()).collect(Collectors.toList()));
        max=max+1;
        }
        GamePlayer Opp=gamePlayerRepository.findById(gameplayerid).get().getGameId()
                .getGamePlayer().stream().filter(b->!b.getId().equals(gameplayerid)).findFirst().get();
        GamePlayer Jug=gamePlayerRepository.findById(gameplayerid).get();
        int EneSalvo= Opp.getSalvo().size();
        int MiSalvo= Jug.getSalvo().size();
        System.out.println("Jugador Opp:"+ Opp.getPlayerId().getUserName()+Opp.getPlayerId().getId());
        System.out.println("Jugador Yo:"+ Jug.getPlayerId().getUserName()+Jug.getPlayerId().getId());
        if(Opp.getId()<Jug.getId()){
            if(Opp.getSalvo().size()<=Jug.getSalvo().size()){
                return new ResponseEntity<>(makeMap("error","No es tu turno"), HttpStatus.FORBIDDEN);
            }
            else {
                salvoLocations.setTurn(max);
                salvoLocations.setGamePlayerID(Jug);
                salvoRepository.save(salvoLocations);
                return new ResponseEntity<>(makeMap("OK","tiro hecho"), HttpStatus.CREATED);
            }
        }
        if(Opp.getId()>Jug.getId()){
            if(Opp.getSalvo().size()>=Jug.getSalvo().size()){
                salvoLocations.setTurn(max);
                salvoLocations.setGamePlayerID(Jug);
                salvoRepository.save(salvoLocations);
                return new ResponseEntity<>(makeMap("OK","tiro hecho"), HttpStatus.CREATED);

            }
            else {
                return new ResponseEntity<>(makeMap("error","No es tu turno"), HttpStatus.FORBIDDEN);
            }
        }


//Forma mas FACHERA de sacar el turno UwU
//        int turno_actual=(gamePlayerRepository.getById(gameplayerid).getSalvo().size()+1)
//        salvoLocations.setTurn(turno_actual);
//        salvoLocations.setG   amePlayerID(gamePlayerRepository.getById(gameplayerid));
//        salvoRepository.save(salvoLocations);

        return new ResponseEntity<>(makeMap("error","Algo Malio Sal"), HttpStatus.FORBIDDEN);


    }






    @RequestMapping(path="/games",  method = RequestMethod.GET)
        public Map <String, Object> makeGame(Authentication authentication){
            Map<String, Object> dto= new LinkedHashMap<String,Object>();
        if(isGuest(authentication)){
                dto.put("player","Guest");
            }
            else{ dto.put("player",playerRepository.findByuserName(authentication.getName()).makePlayerDTO());
            }
                     dto.put("games", gameRepository.findAll()
                    .stream()
                    .map(game -> game.makeGameDTO())
                    .collect(Collectors.toList()));
            return dto;
        }




    @RequestMapping("/game_view/{nn}")
    public  ResponseEntity<Map> findGamePlayer(@PathVariable Long nn,Authentication authentication) {
        GamePlayer gamePlayerID = gamePlayerRepository.findById(nn).get();
        GamePlayer enemigo=null;
        if(gamePlayerID.getGameId().getGamePlayer().stream().anyMatch(b -> !b.getId().equals(gamePlayerID.getId())) )
        {enemigo =gamePlayerID.getGameId().getGamePlayer().stream().filter(b -> !b.getId().equals(gamePlayerID.getId())).findFirst().get(); }

        if(playerRepository.findByuserName(authentication.getName()).getGamePlayer().stream()
                .anyMatch(b->b.getId().equals(nn))
        ){
            if(enemigo!= null){
            if (enemigo.GameState().equals("WON")) {
                Score Score5= new Score(LocalDateTime.now(),0F,gamePlayerID.getGameId(),gamePlayerID.getPlayerId());
                scoreRepository.save(Score5);
                Score Score6= new Score(LocalDateTime.now(),1F,enemigo.getGameId(),enemigo.getPlayerId());
                scoreRepository.save(Score6);
            }
            if (gamePlayerID.GameState().equals("WON")) {
                Score Score3= new Score(LocalDateTime.now(),1F,gamePlayerID.getGameId(),gamePlayerID.getPlayerId());
                scoreRepository.save(Score3);
                Score Score4= new Score(LocalDateTime.now(),0F,enemigo.getGameId(),enemigo.getPlayerId());
                scoreRepository.save(Score4);
            }
            if (gamePlayerID.GameState().equals("TIE")) {
                Score Score1= new Score(LocalDateTime.now(),0.5F,enemigo.getGameId(),enemigo.getPlayerId());
                scoreRepository.save(Score1);
                Score Score2= new Score(LocalDateTime.now(),0.5F,gamePlayerID.getGameId(),gamePlayerID.getPlayerId());
                scoreRepository.save(Score2);
            }}
            return new ResponseEntity<>(gamePlayerID.makeGameViewDTO(),HttpStatus.ACCEPTED);
        }
        else{ return new ResponseEntity<>(makeMap("NO HAGAS TRAMPAS BIEJO COCHINO",0),HttpStatus.UNAUTHORIZED);}
    }




    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String email,@RequestParam String password) {
        if (email.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByuserName(email);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        }
        Player newplayer = playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        //webSecurityConfiguration.init(User(email, password, AuthorityUtils.createAuthorityList("USER")));
        return new ResponseEntity<>(makeMap("id", newplayer.getId()), HttpStatus.CREATED);
    }








    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }


}
