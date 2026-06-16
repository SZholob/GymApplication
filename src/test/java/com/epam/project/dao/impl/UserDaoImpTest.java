package com.epam.project.dao.impl;

import com.epam.project.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Import(UserDaoImp.class)
class UserDaoImpTest {

    @Autowired
    private UserDaoImp userDao;

    @Test
    void testSaveAndFindByUsername() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("Test.User");
        user.setPassword("secretHash");
        user.setIsActive(true);

        User savedUser = userDao.save(user);

        assertNotNull(savedUser.getId(), "User ID should be generated after save");

        Optional<User> foundUser = userDao.findByUsername("Test.User");

        assertTrue(foundUser.isPresent(), "User should be found in the database");
        assertEquals("Test", foundUser.get().getFirstName());
        assertEquals("secretHash", foundUser.get().getPassword());
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<User> foundUser = userDao.findByUsername("Ghost.User");

        assertFalse(foundUser.isPresent(), "User should not be found");
    }
}