package com.kasunjay.miigrasbackend.common.mapper;


import com.kasunjay.miigrasbackend.entity.User;
import com.kasunjay.miigrasbackend.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
   @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User userModelToUser(UserModel userModel);
}
