
package com.codeoftheweb.salvo.Repos;

import com.codeoftheweb.salvo.Clases.Salvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SalvoRepository extends JpaRepository<Salvo, Long> {

}