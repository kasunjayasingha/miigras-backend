package com.kasunjay.miigrasbackend.common.mapper;


import com.kasunjay.miigrasbackend.entity.User;
import com.kasunjay.miigrasbackend.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
   @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User userModelToUser(UserModel userModel);

    List<UserModel> usersToUserModels(List<User> users);
}
