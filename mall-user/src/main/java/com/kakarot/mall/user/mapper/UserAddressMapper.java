package com.kakarot.mall.user.mapper;

import com.kakarot.mall.user.entity.UserAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAddressMapper {

    List<UserAddress> selectByUserId(@Param("userId") Long userId);

    int countByUserId(@Param("userId") Long userId);

    UserAddress selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    void insert(UserAddress address);

    int update(UserAddress address);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int clearDefault(@Param("userId") Long userId);

    int setDefault(@Param("id") Long id, @Param("userId") Long userId);
}
