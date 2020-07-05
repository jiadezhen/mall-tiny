package com.macro.mall.tiny.service.impl;

import com.macro.mall.tiny.dao.UmsRoleDao;
import com.macro.mall.tiny.mbg.model.UmsMenu;
import com.macro.mall.tiny.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsRoleServiceImpl implements UmsRoleService {

    @Autowired
    private UmsRoleDao roleDao;

    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return roleDao.getMenuList(adminId);
    }

}
