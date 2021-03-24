package com.example.cli.controller;

import com.example.cli.domain.add.AddUserRole;
import com.example.cli.domain.common.ResponseBean;
import com.example.cli.domain.search.UserSearch;
import com.example.cli.entity.User;
import com.example.cli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author liwei
 * @title: UserPermissionController
 * @projectName cli
 * @description: TODO
 * @date 2019-10-21 16:47
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseBean getInfo() {
        return new ResponseBean(userService.getInfo());
    }

    @GetMapping("/nav")
    public ResponseBean getCurrentUserNav() {
        return new ResponseBean(userService.getCurrentUserNav());
    }


    /**
     * 获取用户列表
     *
     * @param search
     * @return
     */
    @GetMapping
    public ResponseBean getUserList(UserSearch search) {
        return new ResponseBean(userService.getAll(search));
    }

    @DeleteMapping
    public ResponseBean delUser(@RequestParam("id") Integer id) {
        userService.deleteUser(id);
        return new ResponseBean("success");
    }

    @PutMapping("/enableUser")
    public ResponseBean enableUser(@RequestParam("id") Integer id) {
        userService.enableUser(id);
        return new ResponseBean("success");
    }

    @PutMapping("/disableUser")
    public ResponseBean disableUser(Integer id) {
        userService.disableUser(id);
        return new ResponseBean("success");
    }

    @PutMapping("/addUserRole")
    public ResponseBean addUserRole(@RequestBody AddUserRole addUserRole) {
        userService.addUserRole(addUserRole);
        return new ResponseBean("success");
    }

    /**
     * 新增或编辑用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/saveUser")
    public ResponseBean saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseBean("success");
    }

    /**
     * 判断手机是否被绑定
     *
     * @param phone
     * @return
     */
    @GetMapping("/findPhoneNum/{phone}")
    public ResponseBean findPhoneNum(@PathVariable("phone") String phone) {
        return new ResponseBean(userService.findPhoneNum(phone));
    }

    /**
     * 判断账号是否被占用
     *
     * @param name
     * @return
     */
    @GetMapping("/findNameNum")
    public ResponseBean findNameNum(@NotBlank(message = "账号不能为空") String name) {
        return new ResponseBean(userService.findNameNum(name));
    }
}
