
package com.codeoftheweb.salvo.Repos;

import com.codeoftheweb.salvo.Clases.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {

    public Player   findByuserName(String userName);

}