package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAllUsers(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("current", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "allUsers";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable(name = "id") int id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "editUser";
    }

    @PostMapping("/{id}/edit")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") int id,
                         @ModelAttribute("roles") List<Integer> rolesId) {
        if (!rolesId.isEmpty()) {
            List<Role> roles = new ArrayList<>();
            for (int roleId : rolesId) {
                roles.add(roleService.getRoleById(roleId));
           }
            user.setRoles(roles);
        }
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/user-delete/{id}")
    public String delete(@PathVariable("id") int id){
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/addUser")
    public String newPerson(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "addUser";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") User user, @ModelAttribute("roles") List<Integer> rolesId) {
        List<Role> roles = new ArrayList<>();
        for (int roleId : rolesId) {
            roles.add(roleService.getRoleById(roleId));
        }
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }
}
