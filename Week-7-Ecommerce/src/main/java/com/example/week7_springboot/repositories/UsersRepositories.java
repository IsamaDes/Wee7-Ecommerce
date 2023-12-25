package com.example.week7_springboot.repositories;

import com.example.week7_springboot.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

//repository/interface-finduserbyusername>userserviceimp-findUsersbyusername>UsersController-loginUser

public interface UsersRepositories extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

}
