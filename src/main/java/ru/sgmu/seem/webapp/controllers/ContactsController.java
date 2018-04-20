package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sgmu.seem.utils.DateManager;
import ru.sgmu.seem.webapp.domains.Contact;
import ru.sgmu.seem.webapp.domains.ContactType;
import ru.sgmu.seem.webapp.domains.EntityDetails;
import ru.sgmu.seem.webapp.services.ContactService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sgmu.seem.utils.DateManager.*;
import static ru.sgmu.seem.utils.enums.ListTitle.CONTACTS_LIST;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;
import static ru.sgmu.seem.utils.enums.MenuOption.CONTACTS;

@Controller
@RequestMapping(value = "/contacts")
public class ContactsController {

    private Path contactsFragmentPath = Paths.get("fragments", "contacts");

    private final ContactService contactService;

    @Autowired
    public ContactsController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String contacts(Model model) {
        SortedMap<ContactType, List<Contact>> contacts = new TreeMap<>(contactService.getAll().stream()
                .sorted(Comparator.comparing(EntityDetails::getDate).thenComparing(EntityDetails::getTime))
                .collect(Collectors.groupingBy(Contact::getType)));
        model.addAttribute(CONTENT.name(), contactsFragmentPath)
                .addAttribute(MENU_OPTION.name(), CONTACTS.name())
                .addAttribute(CONTACTS_LIST.name(), contacts);
        return "layouts/main";
    }
}
