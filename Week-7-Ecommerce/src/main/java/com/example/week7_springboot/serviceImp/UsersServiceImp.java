package com.example.week7_springboot.serviceImp;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.week7_springboot.dtos.PasswordDto;
import com.example.week7_springboot.models.Users;
import com.example.week7_springboot.repositories.UsersRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

//repository/interface-finduserbyusername>userserviceimp-findUsersbyusername>UsersController-loginUser

@Service
public class UsersServiceImp {

    private UsersRepositories usersRepositories;

    @Autowired
    public UsersServiceImp(UsersRepositories usersRepositories) {
        this.usersRepositories = usersRepositories;
    }

    public Function<String, Users> findUsersByUsername = (username)->
            usersRepositories.findByUsername(username)
                    .orElseThrow(()->new NullPointerException("User not found!"));
    public Function<Long, Users> findUsersById = (id)->
            usersRepositories.findById(id)
                    .orElseThrow(()->new NullPointerException("User not found!"));

    public Function<Users, Users> saveUser = (user)->usersRepositories.save(user);

    public Function<PasswordDto, Boolean> verifyUserPassword = passwordDTO -> BCrypt.verifyer()
            .verify(passwordDTO.getPassword().toCharArray(),
                    passwordDTO.getHashPassword().toCharArray())
            .verified;
}
