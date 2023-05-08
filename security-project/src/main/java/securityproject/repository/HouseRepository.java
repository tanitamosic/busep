package securityproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.home.House;
import securityproject.model.user.StandardUser;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    List<House> findHousesByRenter(StandardUser renter);
    List<House> findHousesByOwner(StandardUser owner);
}
