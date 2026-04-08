package com.kakarot.mall.user.controller;

import com.kakarot.mall.common.result.Result;
import com.kakarot.mall.infra.context.UserContext;
import com.kakarot.mall.user.dto.AddressAddDTO;
import com.kakarot.mall.user.dto.AddressDeleteDTO;
import com.kakarot.mall.user.dto.AddressSetDefaultDTO;
import com.kakarot.mall.user.dto.AddressUpdateDTO;
import com.kakarot.mall.user.service.AddressService;
import com.kakarot.mall.user.vo.AddressVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/app/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/list")
    public Result<List<AddressVO>> list() {
        return Result.success(addressService.listAddress(UserContext.getUserId()));
    }

    @PostMapping("/add")
    public Result<Void> add(@Valid @RequestBody AddressAddDTO dto) {
        addressService.addAddress(UserContext.getUserId(), dto);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<Void> update(@Valid @RequestBody AddressUpdateDTO dto) {
        addressService.updateAddress(UserContext.getUserId(), dto);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@Valid @RequestBody AddressDeleteDTO dto) {
        addressService.deleteAddress(UserContext.getUserId(), dto.getId());
        return Result.success();
    }

    @PostMapping("/setDefault")
    public Result<Void> setDefault(@Valid @RequestBody AddressSetDefaultDTO dto) {
        addressService.setDefault(UserContext.getUserId(), dto.getId());
        return Result.success();
    }
}
