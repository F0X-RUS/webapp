package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sgmu.seem.utils.DateManager;
import ru.sgmu.seem.webapp.domains.Contact;
import ru.sgmu.seem.webapp.services.ContactService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sgmu.seem.utils.DateManager.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.CURRENT_PAGE;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;
import static ru.sgmu.seem.utils.enums.ListTitle.ADDRESS_LIST;
import static ru.sgmu.seem.utils.enums.ListTitle.PHONE_LIST;
import static ru.sgmu.seem.utils.enums.MenuOption.CONTACTS;

@Controller
@RequestMapping(value = "/contacts")
public class ContactsController {

    private static final String DEFAULT_PHONE = "+7 845 266-97-33";
    private static final String DEFAULT_ADDRESS = "Большая Садовая ул., 137, Саратов, Саратовская обл., Россия, 410000";
    private final static String CONTACTS_TITLE = "Кафедра СТ и НМ - Контакты";
    private final static String ORGANIZATION_TITLE = "Кафедра Симуляционных Технологий и Неотложной Медицины СГМУ им. В.И. Разумовского.";

    private Path contactsFragmentPath = Paths.get("fragments", "contacts");

    private final ContactService contactService;

    @Autowired
    public ContactsController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String contacts(Model model) {
        if (contactService.getAll().size() == 0) {
            initContacts();
        }
        List<Contact> list = contactService.getAll();
        List<Contact> phones = list.stream()
                .filter(e -> e.getType().equals("Телефон"))
                .sorted(Comparator.comparing(Contact::getDate))
                .collect(Collectors.toList());
        List<Contact> addresses = list.stream()
                .filter(e -> e.getType().equals("Адрес"))
                .sorted(Comparator.comparing(Contact::getDate))
                .collect(Collectors.toList());
        Contact first_phone = phones.get(0);
        Contact first_address = addresses.get(0);
        phones.remove(0);
        addresses.remove(0);
        model.addAttribute(CONTENT.name(), contactsFragmentPath)
                .addAttribute(TITLE.name(), CONTACTS_TITLE)
                .addAttribute(CURRENT_PAGE.name(), CONTACTS.name())
                .addAttribute("header", ORGANIZATION_TITLE)
                .addAttribute("first_address", first_address)
                .addAttribute("first_phone", first_phone)
                .addAttribute(PHONE_LIST.name(), phones)
                .addAttribute(ADDRESS_LIST.name(), addresses);
        return "layouts/main";
    }

    private void initContacts() {
        Contact phone = new Contact();
        phone.setType("Телефон");
        phone.setContent(DEFAULT_PHONE);
        phone.setDate(getCurrentDate());
        phone.setTime(getCurrentTime());
        phone.setUpdatedBy("admin");
        contactService.add(phone);
        Contact address = new Contact();
        address.setType("Адрес");
        address.setContent(DEFAULT_ADDRESS);
        address.setDate(getCurrentDate());
        address.setTime(getCurrentTime());
        address.setUpdatedBy("admin");
        contactService.add(address);
    }
}
