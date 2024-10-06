package com.gegunov.mapper;

import com.gegunov.config.KeycloakUser;
import com.gegunov.openapi.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    com.gegunov.jpa.User userDtoToUserEntity(User source);

    User userEntityToUserDto(com.gegunov.jpa.User source);

    KeycloakUser userDtoToKeyCloackUser(User source);

    User keyCloackUserToUserDto(KeycloakUser source);

    List<User> listUserEntitiesToUserDtos(List<com.gegunov.jpa.User>source);
}