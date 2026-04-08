package com.kakarot.mall.user.service.impl;

import com.kakarot.mall.common.exception.BizException;
import com.kakarot.mall.user.dto.AddressAddDTO;
import com.kakarot.mall.user.dto.AddressUpdateDTO;
import com.kakarot.mall.user.entity.UserAddress;
import com.kakarot.mall.user.mapper.UserAddressMapper;
import com.kakarot.mall.user.service.AddressService;
import com.kakarot.mall.user.vo.AddressVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final UserAddressMapper userAddressMapper;

    @Override
    public List<AddressVO> listAddress(Long userId) {
        List<UserAddress> list = userAddressMapper.selectByUserId(userId);
        List<AddressVO> result = new ArrayList<>(list.size());
        for (UserAddress addr : list) {
            result.add(toVO(addr));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAddress(Long userId, AddressAddDTO dto) {
        int count = userAddressMapper.countByUserId(userId);
        int isDefault = (dto.getIsDefault() != null && dto.getIsDefault() == 1) ? 1 : 0;

        // 首条地址自动设为默认
        if (count == 0) {
            isDefault = 1;
        }

        if (isDefault == 1 && count > 0) {
            userAddressMapper.clearDefault(userId);
        }

        UserAddress address = new UserAddress();
        address.setUserId(userId);
        address.setReceiverName(dto.getReceiverName());
        address.setReceiverPhone(dto.getReceiverPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetailAddress(dto.getDetailAddress());
        address.setIsDefault(isDefault);
        userAddressMapper.insert(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(Long userId, AddressUpdateDTO dto) {
        UserAddress existing = userAddressMapper.selectByIdAndUserId(dto.getId(), userId);
        if (existing == null) {
            throw new BizException("地址不存在");
        }

        int isDefault = (dto.getIsDefault() != null && dto.getIsDefault() == 1) ? 1 : 0;
        if (isDefault == 1 && existing.getIsDefault() != 1) {
            userAddressMapper.clearDefault(userId);
        }

        UserAddress address = new UserAddress();
        address.setId(dto.getId());
        address.setUserId(userId);
        address.setReceiverName(dto.getReceiverName());
        address.setReceiverPhone(dto.getReceiverPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetailAddress(dto.getDetailAddress());
        address.setIsDefault(isDefault);

        int rows = userAddressMapper.update(address);
        if (rows == 0) {
            throw new BizException("地址更新失败");
        }
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        int rows = userAddressMapper.deleteByIdAndUserId(addressId, userId);
        if (rows == 0) {
            throw new BizException("地址不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long userId, Long addressId) {
        UserAddress existing = userAddressMapper.selectByIdAndUserId(addressId, userId);
        if (existing == null) {
            throw new BizException("地址不存在");
        }

        userAddressMapper.clearDefault(userId);

        int rows = userAddressMapper.setDefault(addressId, userId);
        if (rows == 0) {
            throw new BizException("设置默认地址失败");
        }
    }

    private AddressVO toVO(UserAddress addr) {
        AddressVO vo = new AddressVO();
        vo.setId(addr.getId());
        vo.setReceiverName(addr.getReceiverName());
        vo.setReceiverPhone(addr.getReceiverPhone());
        vo.setProvince(addr.getProvince());
        vo.setCity(addr.getCity());
        vo.setDistrict(addr.getDistrict());
        vo.setDetailAddress(addr.getDetailAddress());
        vo.setIsDefault(addr.getIsDefault());
        return vo;
    }
}
