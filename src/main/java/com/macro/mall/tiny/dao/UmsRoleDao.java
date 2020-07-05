package com.macro.mall.tiny.dao;

import com.macro.mall.tiny.mbg.model.UmsMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义后台角色管理Dao
 */
public interface UmsRoleDao {

    /**
     * 根据后台用户ID获取菜单
     */
    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);

}
