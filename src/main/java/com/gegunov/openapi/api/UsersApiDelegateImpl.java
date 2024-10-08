package com.gegunov.openapi.api;

import com.gegunov.jpa.UserRepository;
import com.gegunov.mapper.UserMapper;
import com.gegunov.openapi.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersApiDelegateImpl implements UsersApiDelegate {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Long> createUser(User userDto) {
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        com.gegunov.jpa.User user = mapper.userDtoToUserEntity(userDto);
        com.gegunov.jpa.User saved = userRepository.save(user);
        return ResponseEntity.ok(saved.getId());
    }

    @Override
    public ResponseEntity<User> deleteUserById(Long userId) {
        userRepository.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<User>> listUsers() {
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        List<User> users = mapper.listUserEntitiesToUserDtos(userRepository.findAll());
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<User> showUserById(Long userId) {
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        User user = mapper.userEntityToUserDto(
                userRepository.findById(userId).orElseThrow(EntityNotFoundException::new));
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<Void> updateUser(Long userId, User user) {
        com.gegunov.jpa.User userFromDb = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        userFromDb.setUsername(user.getUsername());
        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setPhone(user.getPhone());

        userRepository.save(userFromDb);
        return ResponseEntity.ok().build();
    }
}
