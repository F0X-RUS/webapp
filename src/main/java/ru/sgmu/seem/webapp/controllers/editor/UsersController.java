package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.utils.enums.ListTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.configs.EncoderConfig;
import ru.sgmu.seem.webapp.domains.Role;
import ru.sgmu.seem.webapp.domains.Specialization;
import ru.sgmu.seem.webapp.domains.Staff;
import ru.sgmu.seem.webapp.domains.User;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;
import ru.sgmu.seem.webapp.services.RoleService;
import ru.sgmu.seem.webapp.services.SpecializationService;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

import static ru.sgmu.seem.utils.enums.ListTitle.SPEC_LIST;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;

@PreAuthorize("hasRole('ADMIN')")
@Controller("EditorUsersController")
@RequestMapping(value = "/editor/users")
public class UsersController {

    private Path users = Paths.get("editor", "fragments", "users", "users");
    private Path addUsers = Paths.get("editor", "fragments", "users", "action");
    private CustomUserDetailsService userService;
    private CrudService<Specialization> specService;
    private CrudService<Role> roleService;
    private EncoderConfig encoderConfig;

    @Autowired
    public UsersController(CustomUserDetailsService userService,
                           @Qualifier("specializationService") CrudService<Specialization> specService,
                           CrudService<Role> roleService,
                           EncoderConfig encoderConfig) {
        this.userService = userService;
        this.specService = specService;
        this.roleService = roleService;
        this.encoderConfig = encoderConfig;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String staff(Model model) {
        List<User> list = userService.getAll();
        model.addAttribute(CONTENT.name(), users)
                .addAttribute(MENU_OPTION.toString(), MenuOption.USERS.name())
                .addAttribute(ListTitle.USERS_LIST.name(), list);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model,
                      User user) {
        SortedSet<Role> roles = new TreeSet<>(roleService.getAll());
        model.addAttribute(CONTENT.name(), addUsers)
                .addAttribute(MENU_OPTION.toString(), MenuOption.USERS.name())
                .addAttribute(SPEC_LIST.name(), specService.getAll())
                .addAttribute("roles", roles);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
    public String add(Model model,
                      @PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (!model.containsAttribute("user")){
            model.addAttribute("user", user);
        }
        SortedSet<Role> roles = new TreeSet<>(roleService.getAll());
        model.addAttribute(CONTENT.name(), addUsers)
                .addAttribute(MENU_OPTION.toString(), MenuOption.USERS.name())
                .addAttribute(SPEC_LIST.name(), specService.getAll())
                .addAttribute("roles", roles);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid User user,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/editor/users/add";
        }
        userService.add(user);
        return "redirect:/editor/users";
    }

    @RequestMapping(value = "{id}/add", method = RequestMethod.POST)
    public String add(@Valid User user,
                      BindingResult bindingResult,
                      @PathVariable("id") Long user_id,
                      RedirectAttributes redirectAttributes){
        if (bindingResult.getFieldErrors().size() == 1 &&
                bindingResult.getFieldErrors().get(0).getField().equals("password")){
            user.setPassword(userService.getByUsername(user.getUsername()).getPassword());
        } else {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/editor/users/" + user_id + "/edit";
            }
            user.setPassword(encoderConfig.passwordEncoder().encode(user.getPassword()));
        }
        userService.update(user);
        return "redirect:/editor/users";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView newsDelete(@RequestParam("id") Long id) {
        userService.remove(id);
        return new RedirectView("/editor/users", true);
    }
}
