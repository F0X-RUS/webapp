package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.utils.DateManager;
import ru.sgmu.seem.utils.enums.ListTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.Contact;
import ru.sgmu.seem.webapp.services.ContactService;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;
import static ru.sgmu.seem.utils.enums.PageTitle.ADD_CONTACTS;
import static ru.sgmu.seem.utils.enums.PageTitle.CONTACTS;

@Controller("EditorContactsController")
@RequestMapping(value = "/editor/contacts")
public class ContactsController {

    private Path contactsFragmentPath = Paths.get("editor", "fragments", "contacts", "contacts");
    private Path addContactsFragmentPath = Paths.get("editor", "fragments", "contacts", "add");

    private final ContactService contactService;

    @Autowired
    public ContactsController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model,
                        Contact contact) {
        List<Contact> list = contactService.getAll();
        list.sort((Contact e1, Contact e2) -> e1.getDate().compareTo(e2.getDate()) * -1);
        model.addAttribute(CONTENT.name(), contactsFragmentPath)
                .addAttribute(MENU_OPTION.toString(), MenuOption.CONTACTS.name())
                .addAttribute(TITLE.name(), CONTACTS.getText())
                .addAttribute(ListTitle.CONTACTS_LIST.name(), list);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Model model,
                         @Valid Contact contact,
                         BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            contact.setDate(Date.valueOf(DateManager.getCurrentDate()));
            contact.setTime(Time.valueOf(DateManager.getCurrentTime()));
            //contact.setUpdatedBy("");
            contactService.update(contact);
        }
        model.addAttribute(CONTENT.name(), contactsFragmentPath)
                .addAttribute(MENU_OPTION.name(), MenuOption.CONTACTS.name())
                .addAttribute(TITLE.name(), CONTACTS.getText())
                .addAttribute(ListTitle.CONTACTS_LIST.name(), contactService.getAll());
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model,
                      Contact contact) {
        model.addAttribute(CONTENT.name(), addContactsFragmentPath)
                .addAttribute(MENU_OPTION.name(), MenuOption.CONTACTS.name())
                .addAttribute(TITLE.name(), ADD_CONTACTS.getText());
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String add(Model model,
                      @Valid Contact contact,
                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            model.addAttribute(CONTENT.name(), addContactsFragmentPath)
                    .addAttribute(MENU_OPTION.name(), MenuOption.CONTACTS.name())
                    .addAttribute(TITLE.name(), ADD_CONTACTS.getText());
            return "editor/layouts/index";
        }
        contact.setTime(Time.valueOf(DateManager.getCurrentTime()));
        contact.setDate(Date.valueOf(DateManager.getCurrentDate()));
        //contact.setUpdatedBy();
        contactService.add(contact);
        List<Contact> list = contactService.getAll();
        list.sort((Contact e1, Contact e2) -> e1.getDate().compareTo(e2.getDate()) * -1);
        model.addAttribute(CONTENT.name(), contactsFragmentPath)
                .addAttribute(MENU_OPTION.name(), MenuOption.CONTACTS.name())
                .addAttribute(TITLE.name(), CONTACTS.getText())
                .addAttribute(ListTitle.CONTACTS_LIST.name(), list);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView newsDelete(@RequestParam("id") Long id) {
        contactService.remove(id);
        return new RedirectView("/editor/contacts", true);
    }

}
