package com.example.cli.service;


import com.example.cli.constant.CommonConstant;
import com.example.cli.constant.DeletedEnum;
import com.example.cli.constant.StatusEnum;
import com.example.cli.domain.add.AddUserRole;
import com.example.cli.domain.common.ActionEntrySet;
import com.example.cli.domain.common.PageInfo;
import com.example.cli.domain.common.Permission;
import com.example.cli.domain.common.RouterMenu;
import com.example.cli.domain.search.UserSearch;
import com.example.cli.entity.Menu;
import com.example.cli.entity.Role;
import com.example.cli.entity.User;
import com.example.cli.exception.BaseException;
import com.example.cli.repository.RoleRepository;
import com.example.cli.repository.UserRepository;
import com.example.cli.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wjw
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    /**
     * 默认密码
     */
    private final static String DEFAULT_PASSWORDS = "wjw123456";

    public PageInfo<User> getAll(UserSearch baseSearch) {
        Pageable pageable = PageRequest.of(baseSearch.getPageNo() - 1, baseSearch.getPageSize());
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(baseSearch.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + baseSearch.getName() + "%"));
            }
            if (!StringUtils.isEmpty(baseSearch.getEmail())) {
                predicates.add(criteriaBuilder.equal(root.get("email"), baseSearch.getEmail()));
            }
            if (null != baseSearch.getStatus()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), baseSearch.getStatus()));
            }
            if (StringUtils.hasLength(baseSearch.getPhone())) {
                predicates.add(criteriaBuilder.equal(root.get("phone"), baseSearch.getPhone()));
            }
            if (StringUtils.hasLength(baseSearch.getUserName())) {
                predicates.add(criteriaBuilder.like(root.get("userName"), baseSearch.getUserName()));
            }
            predicates.add(criteriaBuilder.equal(root.get("deleted"), DeletedEnum.NOT_DELETE));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };

        Page<User> userPage = userRepository.findAll(specification, pageable);

        for (User user : userPage.getContent()) {
            if (null != user.getRole()) {
                user.setRoleId(user.getRole().getId());
            }
            if (null != user.getCreateUser()) {
                user.setCreateUserName(user.getCreateUser().getName());
            }
        }

        return PageUtils.getPageInfo(userPage, User.class);
    }

    public User getInfo() {
        User user = RequestUserHolder.getUser();
        Role role = user.getRole();
        if (role == null) {
            return user;
        }
        List<Menu> menus = role.getMenus();
        //获取树形结构的menu信息
        List<Menu> treeMenus = TreeUtils.getMenuTreeList(menus);
        List<Permission> permissions = new ArrayList<>();
        getPermission(treeMenus, permissions, role.getRoleId());
        role.setPermissions(permissions);
        return user;
    }

    public void getPermission(List<Menu> menus, List<Permission> permissions, String roleId) {
        if (menus == null || menus.size() == 0) {
            return;
        }
        if (menus.get(0).getType().equals(2)) {
            return;
        }
        for (Menu menu : menus) {
            List<Menu> child = menu.getChildren();
            Permission permission = new Permission();
            permission.setRoleId(roleId);
            permission.setPermissionId(menu.getPermissionId());
            permission.setPermissionName(menu.getPermissionName());
            if (child != null && child.size() != 0) {
                if (child.get(0).getType().equals(2)) {
                    //功能的话
                    permission.setActionEntrySet(getAction(menu.getChildren()));
                } else {
                    //菜单
                    getPermission(menu.getChildren(), permissions, roleId);
                }
            }
            permissions.add(permission);
        }

    }

    @Modifying(clearAutomatically = true)
    public List<RouterMenu> getCurrentUserNav() {
        User user = RequestUserHolder.getUser();
        Role role = user.getRole();
        if (role == null) {
            return null;
        }

        List<Menu> menus = role.getMenus();

        //筛选菜单
        List<RouterMenu> routerMenus = menus.stream().filter(menu -> menu.getType().equals(1) && !menu.getId().equals(1))
                .map(menu -> {
                    RouterMenu routerMenu = new RouterMenu(menu);
                    return routerMenu;
                })
                .collect(Collectors.toList());
        Collections.sort(routerMenus, Comparator.comparing(RouterMenu::getSort));
        return routerMenus;
    }


    public User getUserById(Integer id) {
        return userRepository.getOne(id);
    }

    public void saveUser(User user) {
        User useUser = RequestUserHolder.getUser();
        Role role = new Role();

        if (null != user.getRoleId()) {
           role = roleRepository.getOne(user.getRoleId());
        }

        user.setRole(role);

        if (StringUtils.isEmpty(user.getId())) {
            if (StringUtils.isEmpty(user.getPassword())) {
                user.setPassword(MD5Utils.stringToMD5(DEFAULT_PASSWORDS));
            } else {
                user.setPassword(MD5Utils.stringToMD5(user.getPassword()));
            }

            user.setCreateTime(new Date());
            user.setCreateUser(useUser);
            user.setDeleted(DeletedEnum.NOT_DELETE);
            user.setStatus(StatusEnum.USED);
            userRepository.saveAndFlush(user);
        } else {
            User old = userRepository.getOne(user.getId());
            MyBeanUtils.copyProperties(user, old);
            userRepository.saveAndFlush(old);
        }

    }

    public void deleteUser(Integer id) {
        User user = userRepository.getOne(id);
        user.setDeleted(DeletedEnum.DELETE);
        userRepository.saveAndFlush(user);
    }

    public void disableUser(Integer id) {
        User user = userRepository.getOne(id);
        user.setStatus(StatusEnum.UNUSED);
        userRepository.saveAndFlush(user);

    }

    public void enableUser(Integer id) {
        User user = userRepository.getOne(id);
        user.setStatus(StatusEnum.USED);
        userRepository.saveAndFlush(user);

    }


    public void addUserRole(AddUserRole addUserRole) {
        User user = userRepository.getOne(addUserRole.getUserId());
        Role role = roleRepository.getOne(addUserRole.getRoleId());
        user.setRole(role);
        userRepository.saveAndFlush(user);
    }

    private List<ActionEntrySet> getAction(List<Menu> menus) {
        List<ActionEntrySet> actionEntrySets = new ArrayList<>();
        for (Menu menu : menus) {
            actionEntrySets.add(ActionEntrySet.builder()
                    .id(menu.getId())
                    .action(menu.getPermissionId())
                    .describe(menu.getPermissionName())
                    .defaultCheck(menu.getDefaultCheck()).build());
        }
        return actionEntrySets;
    }

    /**
     * 查询手机号码是否绑定用户
     *
     * @param phone
     * @return
     */
    public Integer findPhoneNum(String phone) {
        return userRepository.findPhoneNum(phone);
    }

    /**
     * 查询账号是否存在
     *
     * @param name
     * @return
     */
    public Integer findNameNum(String name) {
        return userRepository.findNameNum(name);
    }

    /**
     * 为业务员充值
     * @param rechage
     * @param user
     */
    void updateActualPrice(BigDecimal rechage, User user) {
        User useUser = RequestUserHolder.getUser();

        int ret = userRepository.updateActualPrice(rechage, user.getId());
        if (ret <= 0) {
            throw new BaseException(CommonConstant.EXCEPTION, "充值失败");
        }

        log.info("{}，{}（{}）为账号{}（{}）充值：{}", DateUtils.getNowDate(),
                useUser.getName(), useUser.getId(), user.getName(), user.getId(), rechage.toString());
    }
}
