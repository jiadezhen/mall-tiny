package com.macro.mall.tiny.service;

import com.macro.mall.tiny.mbg.model.UmsMenu;

import java.util.List;

public interface UmsRoleService {
    /**
     * 根据管理员ID获取对应菜单
     */
    List<UmsMenu> getMenuList(Long adminId);
}
