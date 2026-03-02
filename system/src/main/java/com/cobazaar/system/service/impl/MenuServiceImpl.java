package com.cobazaar.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cobazaar.system.entity.Menu;
import com.cobazaar.system.mapper.MenuMapper;
import com.cobazaar.system.service.MenuService;
import com.cobazaar.system.vo.MenuVO;
import com.cobazaar.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单Service实现类
 * 对应数据库表 sys_menu
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> getMenuTree() {
        // 查询所有菜单
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(Menu::getSort);
        List<Menu> menus = this.list(wrapper);
        // 构建树形结构
        return buildMenuTree(menus, 0L);
    }

    @Override
    public List<Menu> getChildrenByParentId(Long parentId) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Menu::getParentId, parentId)
                .orderByAsc(Menu::getSort);
        return this.list(wrapper);
    }

    @Override
    public List<Menu> getMenusByRoleId(Long roleId) {
        return baseMapper.selectByRoleId(roleId);
    }

    @Override
    public List<Menu> getMenusByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public boolean checkMenuNameExists(String menuName, Long parentId, String menuType, Long id) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Menu::getMenuName, menuName)
                .eq(Menu::getParentId, parentId)
                .eq(Menu::getMenuType, menuType);
        if (id != null) {
            wrapper.ne(Menu::getId, id);
        }
        return this.count(wrapper) > 0;
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setStatus(status);
        return this.updateById(menu);
    }

    @Override
    public String buildMenuPath(Long parentId) {
        if (parentId == 0L) {
            return "0";
        }
        Menu parent = this.getById(parentId);
        if (parent == null) {
            return "0";
        }
        return parent.getMenuPath() + "." + parentId;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        List<Menu> menus = baseMapper.selectByUserId(userId);
        return menus.stream()
                .filter(menu -> menu.getPerms() != null && !menu.getPerms().isEmpty())
                .map(Menu::getPerms)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean save(Menu menu) {
        // 构建菜单路径
        String menuPath = buildMenuPath(menu.getParentId());
        menu.setMenuPath(menuPath);
        return super.save(menu);
    }

    @Override
    public boolean updateById(Menu menu) {
        // 如果父菜单变更，需要重新构建菜单路径
        Menu oldMenu = this.getById(menu.getId());
        if (oldMenu != null && !oldMenu.getParentId().equals(menu.getParentId())) {
            String menuPath = buildMenuPath(menu.getParentId());
            menu.setMenuPath(menuPath);
            // 更新子菜单的路径
            updateChildrenMenuPath(oldMenu.getMenuPath(), menuPath, menu.getId());
        }
        return super.updateById(menu);
    }

    @Override
    public boolean removeById(java.io.Serializable id) {
        // 检查是否有子菜单
        int count = baseMapper.countByParentId((Long) id);
        if (count > 0) {
            log.warn("菜单 {} 存在子菜单，无法删除", id);
            return false;
        }
        return super.removeById(id);
    }

    /**
     * 构建菜单树形结构
     *
     * @param menus    菜单列表
     * @param parentId 父菜单ID
     * @return 菜单树形列表
     */
    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> treeList = new ArrayList<>();
        for (Menu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                List<Menu> children = buildMenuTree(menus, menu.getId());
                // 这里可以通过扩展属性存储子菜单
                treeList.add(menu);
            }
        }
        return treeList;
    }

    /**
     * 更新子菜单的路径
     *
     * @param oldPath 旧路径
     * @param newPath 新路径
     * @param menuId  菜单ID
     */
    private void updateChildrenMenuPath(String oldPath, String newPath, Long menuId) {
        List<Menu> children = baseMapper.selectByMenuPath(oldPath + "." + menuId + "%");
        for (Menu child : children) {
            String childPath = child.getMenuPath();
            String newChildPath = childPath.replace(oldPath, newPath);
            child.setMenuPath(newChildPath);
            this.updateById(child);
        }
    }

    @Override
    public MenuVO convertToVO(Menu menu) {
        if (menu == null) {
            return null;
        }
        MenuVO menuVO = new MenuVO();
        menuVO.setId(menu.getId());
        menuVO.setMenuName(menu.getMenuName());
        menuVO.setParentId(menu.getParentId());
        menuVO.setMenuPath(menu.getMenuPath());
        menuVO.setComponentPath(menu.getComponent());
        menuVO.setMenuType(menu.getMenuType());
        menuVO.setPerms(menu.getPerms());
        menuVO.setIcon(menu.getIcon());
        menuVO.setSort(menu.getSort());
        menuVO.setStatus(menu.getStatus());
        menuVO.setRemark(menu.getRemark());
        menuVO.setCreateTime(menu.getCreateTime());
        menuVO.setUpdateTime(menu.getUpdateTime());
        return menuVO;
    }

    @Override
    public List<MenuVO> convertToVOList(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }
        return menus.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<MenuVO> convertTreeToVOList(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<MenuVO> voList = new ArrayList<>();
        for (Menu menu : menus) {
            MenuVO vo = convertToVO(menu);
            // 递归处理子菜单
            List<Menu> children = getChildrenByParentId(menu.getId());
            if (!children.isEmpty()) {
                vo.setChildren(convertTreeToVOList(children));
            }
            voList.add(vo);
        }
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMenu(com.cobazaar.system.dto.MenuAddDTO menuAddDTO) {
        // 检查菜单名称是否存在
        if (checkMenuNameExists(menuAddDTO.getMenuName(), menuAddDTO.getParentId(), menuAddDTO.getMenuType(), null)) {
            throw new com.cobazaar.common.exception.ServiceException("菜单名称已存在");
        }
        
        Menu menu = new Menu();
        cn.hutool.core.bean.BeanUtil.copyProperties(menuAddDTO, menu);
        
        return save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(com.cobazaar.system.dto.UpdateMenuDTO updateMenuDTO) {
        // 检查菜单名称是否存在
        if (checkMenuNameExists(updateMenuDTO.getMenuName(), updateMenuDTO.getParentId(), updateMenuDTO.getMenuType(), updateMenuDTO.getId())) {
             throw new com.cobazaar.common.exception.ServiceException("菜单名称已存在");
        }
        
        Menu menu = new Menu();
        cn.hutool.core.bean.BeanUtil.copyProperties(updateMenuDTO, menu);
        
        return updateById(menu);
    }
}
