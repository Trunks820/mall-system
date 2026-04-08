package com.kakarot.mall.user.service;

import com.kakarot.mall.user.dto.AddressAddDTO;
import com.kakarot.mall.user.dto.AddressUpdateDTO;
import com.kakarot.mall.user.vo.AddressVO;

import java.util.List;

public interface AddressService {

    List<AddressVO> listAddress(Long userId);

    void addAddress(Long userId, AddressAddDTO dto);

    void updateAddress(Long userId, AddressUpdateDTO dto);

    void deleteAddress(Long userId, Long addressId);

    void setDefault(Long userId, Long addressId);
}
