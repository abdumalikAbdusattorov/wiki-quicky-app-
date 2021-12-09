package ssd.uz.wikiquickyapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ssd.uz.wikiquickyapp.entity.Role;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.projection.CustomRole;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "role", collectionResourceRel = "list", excerptProjection = CustomRole.class)
public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findAllByName(RoleName name);

    Optional<Role> findByName(RoleName name);

    List<Role> findAllByNameIn(List<RoleName> names);

    @Query(value = "delete from user_role where user_id=:id",nativeQuery = true)
    int deleteRole(@Param("id") Long id);


}
