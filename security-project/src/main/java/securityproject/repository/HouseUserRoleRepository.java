package securityproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.home.Device;
import securityproject.model.home.HouseUserRole;

import java.util.List;

@Repository
public interface HouseUserRoleRepository extends JpaRepository<HouseUserRole, Long> {

    List<HouseUserRole> getHouseUserRolesByUserIdAndRoleAndIsActive(Long userId, String role, Boolean isActive);
//    List<HouseUserRole> getHouseUserRolesByUserIdAndHouseIdAndIsActive(Long houseId, Boolean isActive);
    HouseUserRole getHouseUserRoleByHouseIdAndRoleAndIsActive(Long houseId, String role, Boolean isActive);
}
