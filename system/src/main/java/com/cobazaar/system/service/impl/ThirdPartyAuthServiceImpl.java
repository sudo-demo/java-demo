package com.cobazaar.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cobazaar.system.entity.ThirdPartyAuth;
import com.cobazaar.system.mapper.ThirdPartyAuthMapper;
import com.cobazaar.system.service.ThirdPartyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 第三方认证服务实现类
 * 实现第三方认证相关的业务逻辑操作
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Service
public class ThirdPartyAuthServiceImpl extends ServiceImpl<ThirdPartyAuthMapper, ThirdPartyAuth> implements ThirdPartyAuthService {

    @Autowired
    private ThirdPartyAuthMapper thirdPartyAuthMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindThirdPartyAuth(com.cobazaar.system.dto.ThirdPartyAuthBindDTO bindDTO) {
        // 检查是否已绑定
        if (isBound(bindDTO.getPlatformType(), bindDTO.getPlatformUserId())) {
            throw new com.cobazaar.common.exception.ServiceException("该第三方账号已绑定");
        }
        
        ThirdPartyAuth thirdPartyAuth = new ThirdPartyAuth();
        cn.hutool.core.bean.BeanUtil.copyProperties(bindDTO, thirdPartyAuth);
        thirdPartyAuth.setBindTime(LocalDateTime.now());
        thirdPartyAuth.setStatus(1); // 绑定状态
        return save(thirdPartyAuth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindThirdPartyAuth(Long id) {
        ThirdPartyAuth thirdPartyAuth = getById(id);
        if (thirdPartyAuth == null) {
            throw new com.cobazaar.common.exception.ServiceException("第三方认证信息不存在");
        }
        thirdPartyAuth.setStatus(0); // 解绑状态
        updateById(thirdPartyAuth);
    }

    @Override
    public ThirdPartyAuth getByPlatformUserId(Integer platformType, String platformUserId) {
        LambdaQueryWrapper<ThirdPartyAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThirdPartyAuth::getPlatformType, platformType);
        queryWrapper.eq(ThirdPartyAuth::getPlatformUserId, platformUserId);
        queryWrapper.eq(ThirdPartyAuth::getStatus, 1); // 只查询绑定状态的
        return getOne(queryWrapper);
    }

    @Override
    public List<com.cobazaar.system.vo.ThirdPartyAuthVO> getByUserId(Long userId) {
        LambdaQueryWrapper<ThirdPartyAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThirdPartyAuth::getUserId, userId);
        queryWrapper.eq(ThirdPartyAuth::getStatus, 1); // 只查询绑定状态的
        List<ThirdPartyAuth> list = list(queryWrapper);
        return convertToVOList(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshToken(Long id, String newAccessToken, String newRefreshToken, LocalDateTime expireTime) {
        ThirdPartyAuth thirdPartyAuth = getById(id);
        if (thirdPartyAuth == null) {
            throw new com.cobazaar.common.exception.ServiceException("第三方认证信息不存在");
        }
        thirdPartyAuth.setAccessToken(newAccessToken);
        thirdPartyAuth.setRefreshToken(newRefreshToken);
        thirdPartyAuth.setTokenExpireTime(expireTime);
        updateById(thirdPartyAuth);
    }

    private com.cobazaar.system.vo.ThirdPartyAuthVO convertToVO(ThirdPartyAuth thirdPartyAuth) {
        if (thirdPartyAuth == null) {
            return null;
        }
        com.cobazaar.system.vo.ThirdPartyAuthVO vo = new com.cobazaar.system.vo.ThirdPartyAuthVO();
        cn.hutool.core.bean.BeanUtil.copyProperties(thirdPartyAuth, vo);
        return vo;
    }

    private List<com.cobazaar.system.vo.ThirdPartyAuthVO> convertToVOList(List<ThirdPartyAuth> list) {
        if (list == null || list.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return list.stream().map(this::convertToVO).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean isBound(Integer platformType, String platformUserId) {
        LambdaQueryWrapper<ThirdPartyAuth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThirdPartyAuth::getPlatformType, platformType);
        queryWrapper.eq(ThirdPartyAuth::getPlatformUserId, platformUserId);
        queryWrapper.eq(ThirdPartyAuth::getStatus, 1); // 只查询绑定状态的
        return count(queryWrapper) > 0;
    }
}
