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
                .username("nicolaspalacio")
                .email("nicolas@gmail.com")
                .password("secure126")
                .enabled(false)
                .build();
    }

    @DisplayName("Test to save an user")
    @Test
    void saveUserTest(){
        //give
        User userTest=User.builder()
                .username("nicolaspalacio")
                .email("nicolas@gmail.com")
                .password("secure126")
                .enabled(false)
                .build();

        //when
        User savedUser=userRepository.save(userTest);

        //then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
        assertThat(savedUser.getEnabled()).isFalse();

    }

    @DisplayName("Test to list users")
    @Test
    void listUsersTest(){
        //give
        User userTest=User.builder()
                .username("eze001")
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

    @DisplayName("Test to find an user by ID")
    @Test
    void findUserByIdTest(){
        //give
        userRepository.save(user);

        //when
       User savedUser=userRepository.findById(user.getId()).get();

        //then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(user.getId());

    }

}
