package csci2040u.bytecouncil.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//A class that holds all the movies as entities
@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

}
