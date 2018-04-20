package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.utils.enums.ListTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.Contact;
import ru.sgmu.seem.webapp.domains.ContactType;
import ru.sgmu.seem.webapp.domains.EntityDetails;
import ru.sgmu.seem.webapp.domains.User;
import ru.sgmu.seem.webapp.services.ContactService;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static ru.sgmu.seem.utils.DateManager.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;

@PreAuthorize("hasRole('ADMIN')")
@Controller("EditorContactsController")
@RequestMapping(value = "/editor/contacts")
public class ContactsController {

    private Path contactsFragmentPath = Paths.get("editor", "fragments", "contacts", "contacts");
    private Path typesList = Paths.get("editor", "fragments", "contacts", "types");
    private Path addContactsFragmentPath = Paths.get("editor", "fragments", "contacts", "add");
    private Path addContactType = Paths.get("editor", "fragments", "contacts", "type-add");

    private ContactService contactService;
    private CrudService<ContactType> contTypeService;
    private CustomUserDetailsService userService;

    @Autowired
    public ContactsController(ContactService contactService,
                              CustomUserDetailsService userService,
                              @Qualifier("contactTypeService") CrudService<ContactType> contTypeService) {
        this.contactService = contactService;
        this.userService = userService;
        this.contTypeService = contTypeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model/*,
                        Contact contact*/) {
        List<Contact> list = contactService.getAll();
        list.sort(Comparator.comparing(EntityDetails::getDate).thenComparing(EntityDetails::getTime).reversed());
        model.addAttribute(CONTENT.name(), contactsFragmentPath)
                .addAttribute(MENU_OPTION.toString(), MenuOption.CONTACTS.name())
                .addAttribute(ListTitle.CONTACTS_LIST.name(), list);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public String index(Model model,
                        ContactType type) {
        List<ContactType> list = contTypeService.getAll();
        list.sort(Comparator.comparing(ContactType::getId));
        model.addAttribute(CONTENT.name(), typesList)
                .addAttribute(MENU_OPTION.toString(), MenuOption.CONTACTS.name())
                .addAttribute(ListTitle.CONTACTSTYPES_LIST.name(), list);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid Contact contact,
                         BindingResult bindingResult,
                         Principal principal,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contact", bindingResult);
            redirectAttributes.addFlashAttribute("contact", contact);
            return "redirect:/editor/contacts";
        }
        contact.setDate(getCurrentDate());
        contact.setTime(getCurrentTime());
        User user = userService.getByUsername(principal.getName());
        contact.setUser(user);
        contactService.update(contact);
        return "redirect:/editor/contacts";
    }

    @RequestMapping(value = "/types/update", method = RequestMethod.POST)
    public String update(@Valid ContactType type,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactType", bindingResult);
            redirectAttributes.addFlashAttribute("contactType", type);
            return "redirect:/editor/contacts/types";
        }
        contTypeService.update(type);
        return "redirect:/editor/contacts/types";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addContact(Model model) {
        if (!model.containsAttribute("contact")){
            Contact contact = new Contact();
            model.addAttribute("contact", contact);
        }
        Set<ContactType> types = new TreeSet<>(contTypeService.getAll());
        model.addAttribute(CONTENT.name(), addContactsFragmentPath)
                .addAttribute(MENU_OPTION.name(), MenuOption.CONTACTS.name())
                .addAttribute("types", types);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/types/add", method = RequestMethod.GET)
    public String add(Model model) {
        ContactType type = new ContactType();
        if (!model.containsAttribute("contactType")){
            model.addAttribute("contactType", type);
        }
        model.addAttribute(CONTENT.name(), addContactType)
                .addAttribute(MENU_OPTION.name(), MenuOption.CONTACTS.name());
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String add(@Valid Contact contact,
                      BindingResult bindingResult,
                      Principal principal,
                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contact", bindingResult);
            redirectAttributes.addFlashAttribute("contact", contact);
            return "redirect:/editor/contacts/add";
        }
        contact.setTime(getCurrentTime());
        contact.setDate(getCurrentDate());
        User user = userService.getByUsername(principal.getName());
        contact.setUser(user);
        contactService.add(contact);
        return "redirect:/editor/contacts";
    }

    @RequestMapping(value = "/types/save", method = RequestMethod.POST)
    public String add(@Valid ContactType type,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactType", bindingResult);
            redirectAttributes.addFlashAttribute("contactType", type);
            return "redirect:/editor/contacts/types/add";
        }
        contTypeService.add(type);
        return "redirect:/editor/contacts/types";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView contactDelete(@RequestParam("id") Long id) {
        contactService.remove(id);
        return new RedirectView("/editor/contacts", true);
    }

    @RequestMapping(value = "/types/delete", method = RequestMethod.POST)
    public RedirectView typeDelete(@RequestParam("id") Long id) {
        contTypeService.remove(id);
        return new RedirectView("/editor/contacts/types", true);
    }

}
