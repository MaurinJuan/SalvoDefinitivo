package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Clases.*;
import com.codeoftheweb.salvo.Repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class 	SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

		@Bean
		public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository
				, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository
				, SalvoRepository salvoRepository
				, ScoreRepository scoreRepository
										  ) {
			return (args) -> {
				Game Game1 = new Game(LocalDateTime.now());
				Game Game2 = new Game(LocalDateTime.now().minusHours(1));
				Game Game3 = new Game(LocalDateTime.now().minusHours(2));
//				Game Game4 = new Game(LocalDateTime.now());
//				Game Game5 = new Game(LocalDateTime.now().minusHours(1));
//				Game Game6 = new Game(LocalDateTime.now().minusHours(2));
				Player player1 = new Player("c.obrian@ctu.gov",passwordEncoder().encode("123"));
				Player player2 = new Player("j.bauer@ctu.gov",passwordEncoder().encode("123"));
				Player player3 = new Player("t.almeida@ctu.gov",passwordEncoder().encode("123"));
				Player player4 = new Player("d.palmer@whitehouse.gov",passwordEncoder().encode("123"));

//ssswassssssss2qzsssssssssssss33ssssssw
				playerRepository.save(player1);
				playerRepository.save(player2);
				playerRepository.save(player3);
				playerRepository.save(player4);
				gameRepository.save(Game1);
				gameRepository.save(Game2);
				gameRepository.save(Game3);
//				gameRepository.save(Game4);
//				gameRepository.save(Game5);
//				gameRepository.save(Game6);

				GamePlayer GamePlayer1 = new GamePlayer(Game1, player1,LocalDateTime.now());
				GamePlayer GamePlayer2 = new GamePlayer(Game1, player2,LocalDateTime.now());
				GamePlayer GamePlayer3 = new GamePlayer(Game2, player2,LocalDateTime.now());
				GamePlayer GamePlayer4 = new GamePlayer(Game3, player1,LocalDateTime.now());
				GamePlayer GamePlayer5 = new GamePlayer(Game3, player3,LocalDateTime.now());

//				GamePlayer GamePlayer6 = new GamePlayer(Game4, player1,LocalDateTime.now());
//				GamePlayer GamePlayer7 = new GamePlayer(Game4, player2,LocalDateTime.now());
//				GamePlayer GamePlayer8 = new GamePlayer(Game5, player2,LocalDateTime.now());
//				GamePlayer GamePlayer9 = new GamePlayer(Game5, player3,LocalDateTime.now());
//				GamePlayer GamePlayer10 = new GamePlayer(Game6, player4,LocalDateTime.now());

				gamePlayerRepository.save(GamePlayer1);
				gamePlayerRepository.save(GamePlayer2);
				gamePlayerRepository.save(GamePlayer3);
				gamePlayerRepository.save(GamePlayer4);
				gamePlayerRepository.save(GamePlayer5);
//				gamePlayerRepository.save(GamePlayer6);
//				gamePlayerRepository.save(GamePlayer7);
//				gamePlayerRepository.save(GamePlayer8);
//				gamePlayerRepository.save(GamePlayer9);
//				gamePlayerRepository.save(GamePlayer10);

				Ship ship1 = new Ship( "carrier",GamePlayer1, Arrays.asList("A1","A2","A3","A4","A5"));
				shipRepository.save(ship1);
				Ship ship3 = new Ship("submarine",GamePlayer1,Arrays.asList("C1","C2","C3"));
				shipRepository.save(ship3);
				Ship ship2 = new Ship("battleship",GamePlayer1, Arrays.asList("B1","B2","B3","B4"));
				shipRepository.save(ship2);
				Ship ship4 = new Ship("destroyer",GamePlayer1,Arrays.asList("D1","D2","D3"));
				shipRepository.save(ship4);
				Ship ship5 = new Ship("patrolboat",GamePlayer1,Arrays.asList("E1","E2"));
				shipRepository.save(ship5);

				Ship ship6 = new Ship("carrier",GamePlayer2, Arrays.asList("A1","A2","A3","A4","A5"));
				shipRepository.save(ship6);
				Ship ship7 = new Ship("submarine",GamePlayer2,Arrays.asList("C1","C2","C3"));
				shipRepository.save(ship7);
				Ship ship8 = new Ship("battleship",GamePlayer2, Arrays.asList("B1","B2","B3","B4"));
				shipRepository.save(ship8);
				Ship ship9 = new Ship("destroyer",GamePlayer2,Arrays.asList("D1","D2","D3"));
				shipRepository.save(ship9);
				Ship ship10 = new Ship("patrolboat",GamePlayer2,Arrays.asList("E1","E2"));
				shipRepository.save(ship10);




				Score Score3= new Score(LocalDateTime.now(),1F,Game2,player1);
				scoreRepository.save(Score3);
				Score Score4= new Score(LocalDateTime.now(),1F,Game2,player2);
				scoreRepository.save(Score4);



				Salvo salvo1 = new Salvo(1,GamePlayer2 ,Arrays.asList("A1","A2","A3","A4","A5"));
				salvoRepository.save(salvo1);
				Salvo salvo2 = new Salvo(1,GamePlayer1	,Arrays.asList("A1","A2","A3","A4","A5"));
				salvoRepository.save(salvo2);
				Salvo salvo3 = new Salvo(2,GamePlayer1	,Arrays.asList("B1","B2","B3","B4"));
				salvoRepository.save(salvo3);
				Salvo salvo4 = new Salvo(2,GamePlayer2 ,Arrays.asList("B1","B2","B3","B4"));
				salvoRepository.save(salvo4);




			};
		}
	@Bean
	public PasswordEncoder passwordEncoder() {return PasswordEncoderFactories.createDelegatingPasswordEncoder(); }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player Name = playerRepository.findByuserName(inputName);
			if (Name != null ) {
				return new User(Name.getUserName(), Name.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});


}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/api/login").permitAll()
//				.antMatchers("/api/players").permitAll()
//				.antMatchers("/login").permitAll()
//				.antMatchers("/web/**").permitAll()
//				.antMatchers("/api/games").permitAll()
////
//				.antMatchers("**").hasAuthority("USER")
//
		             .antMatchers("/api/players").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/web/**").permitAll()
                .antMatchers("/api/games").permitAll()
				.antMatchers("**").hasAuthority("USER")
				.antMatchers("/h2-console/**").permitAll()
				.and().headers().frameOptions().disable()
				.and().csrf().ignoringAntMatchers("/h2-console/**")
				.and()
				.cors().disable()

				;

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");



		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}





}