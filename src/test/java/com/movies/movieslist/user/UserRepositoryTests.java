package com.movies.movieslist.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User user;
    @BeforeEach
    void setup(){
        this.user=User.builder()
                .firstname("Nicolas")
                .lastname("Palacio")
                .email("nicolas@gmail.com")
                .password("secure126")
                .build();
    }

    @DisplayName("Test to save an user")
    @Test
    void saveUserTest(){
        //give
        User userTest=User.builder()
                .firstname("Nicolas")
                .lastname("Palacio")
                .email("nicolas@gmail.com")
                .password("secure126")
                .build();

        //when
        User savedUser=userRepository.save(userTest);

        //then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @DisplayName("Test to list users")
    @Test
    void listUsersTest(){
        //give
        User userTest=User.builder()
                .firstname("Nicolas")
                .lastname("Palacio")
                .email("nicolas@gmail.com")
                .password("secure126")
                .build();

        userRepository.saveAll(List.of(user,userTest));

        //when
        List<User> users=userRepository.findAll();

        //then
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(2);


    }


}
