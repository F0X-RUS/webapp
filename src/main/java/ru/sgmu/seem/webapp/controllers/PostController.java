package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sgmu.seem.webapp.domains.Post;
import ru.sgmu.seem.webapp.domains.User;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;
import ru.sgmu.seem.webapp.services.ThreadService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;

@Controller
@RequestMapping(value = "/forum/{educ_id}/{course_id}/{spec_id}/{disc_id}/{thread_id}")
public class PostController {

    private CrudService<Post> postService;
    private CustomUserDetailsService userService;
    private ThreadService threadService;

    @Autowired
    public PostController(CrudService<Post> postService,
                          CustomUserDetailsService userService,
                          ThreadService threadService) {
        this.postService = postService;
        this.userService = userService;
        this.threadService = threadService;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendPost(@Valid Post post,
                           BindingResult bindingResult,
                           @PathVariable("educ_id") Long educ_id,
                           @PathVariable("course_id") Long course_id,
                           @PathVariable("spec_id") Long spec_id,
                           @PathVariable("disc_id") Long disc_id,
                           @PathVariable("thread_id") Long thread_id,
                           RedirectAttributes redirectAttributes,
                           Principal principal){
        if (bindingResult.hasErrors()){
            for (FieldError fe : bindingResult.getFieldErrors()){
                System.out.println(fe.getField());
                System.out.println(fe.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("post", post);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
            return "redirect:/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id + "/" + thread_id;
        }
        post.setUser(userService.getByUsername(principal.getName()));
        post.setThread(threadService.getById(thread_id));
        post.setDate(getCurrentDate());
        post.setTime(getCurrentTime());
        postService.add(post);
        return "redirect:/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id + "/" + thread_id;
    }

//    @RequestMapping(value = "/{post_id}/edit", method = RequestMethod.GET)
//    public String editPost(){
//
//    }

    @PreAuthorize("principal.username.equals(postService.getById(post_id).user.username) OR hasAnyRole('TEACHER', 'MODER', 'ADMIN')")
    @RequestMapping(value = "/{post_id}/delete", method = RequestMethod.POST)
    public String fileDelete(@PathVariable("post_id") Long post_id,
                             @PathVariable("educ_id") Long educ_id,
                             @PathVariable("course_id") Long course_id,
                             @PathVariable("spec_id") Long spec_id,
                             @PathVariable("disc_id") Long disc_id,
                             @PathVariable("thread_id") Long thread_id) {
        postService.remove(post_id);
        return "redirect:/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id + "/" + thread_id;
    }
}
