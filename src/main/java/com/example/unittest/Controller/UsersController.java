package com.example.unittest.Controller;

import com.example.unittest.Entity.Users;
import com.example.unittest.Repository.UsersRepository;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.PostRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;
    @PostMapping("/new")
    public Users newUsers(@RequestBody Users users){
        return usersRepository.save(users);
    }
    @GetMapping("/all")
    public List<Users> getAllUsers(){
        return usersRepository.findAll();
    }
    @GetMapping("/{id}")
    public Users getUsersById(@PathVariable Long id){
        return usersRepository.getReferenceById(id);
    }
    @PutMapping("/update/{id}")
    public Users updateUsers(@PathVariable Long id, @RequestBody Users users){
        Users userUpsdate = usersRepository.findById(id).orElse(null);
        if (userUpsdate != null){
            userUpsdate.setId(users.getId());
            userUpsdate.setNome(users.getNome());
            userUpsdate.setCognome(users.getCognome());
            userUpsdate.setEmail(users.getEmail());
        }
        return usersRepository.save(userUpsdate);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteUsersById (@PathVariable Long id){
        usersRepository.deleteById(id);
    }
}
