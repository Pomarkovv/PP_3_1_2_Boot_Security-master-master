package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "allUsers";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable(name = "id") int id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", List.of(new Role("ROLE_ADMIN"), new Role("ROLE_USER")));
        return "editUser";
    }

    @PatchMapping("/{id}/edit")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") int id,
                         @ModelAttribute("roles") Role role) {
        Role newRole = new Role(role.getName());
        List<Role> userRoles = new ArrayList<>(user.getRoles());
        for(Role rl : userRoles) {
            if (rl.getName().equals(role.getName())) {
                userService.update(id, user);
            } else {
                user.getRoles().add(newRole);
                userService.update(id, user);
            }
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/edit")
    public String delete(@PathVariable("id") int id){
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/addUser")
    public String newPerson(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", List.of(new Role("ROLE_ADMIN"), new Role("ROLE_USER")));
        return "addUser";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") User user, @ModelAttribute("roles") Role role) {
        Role newRole = new Role(role.getName());
        roleService.save(newRole);
        List<Role> roles = new ArrayList<>();
        roles.add(newRole);
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }
}
