package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping("/")
    public String index(Model model){
        // Grab all the directors from the Database and display them here
        model.addAttribute("directors", directorRepository.findAll());
        return "index";
    }

    @GetMapping("/addDirector")
    public String addDirector(Model model){
        model.addAttribute("director", new Director());
        return "directorForm";
    }

    @RequestMapping("/updateDirector/{id}")
    public String updateDirector(@PathVariable("id") long id, Model model){
        model.addAttribute("director",directorRepository.findById(id).get());
        return "directorForm";
    }

    @RequestMapping("/deleteDirector/{id}")
    public String deleteDirector(@PathVariable("id") long id){
        directorRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/processDirector")
    public String processDirector(@ModelAttribute Director director){
        directorRepository.save(director);
        return "redirect:/";
    }

    @GetMapping("/addMovie")
    public String addMovie(Model model){
        model.addAttribute("movie", new Movie());
        model.addAttribute("directors", directorRepository.findAll());
        return "movieForm";
    }

    @RequestMapping("/updateMovie/{id}")
    public String updateMovie(@PathVariable("id")long id, Model model){
        model.addAttribute("movie", movieRepository.findById(id).get());
        model.addAttribute("directors", directorRepository.findAll());
        return "movieForm";
    }

    @RequestMapping("/deleteMovie/{id}")
    public String deleteMovie(@PathVariable("id") long id){
        movieRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/processMovie")
    public String processMovie(@ModelAttribute Movie movie,
                               @RequestParam(name = "moviePicture")MultipartFile file){


        if (file.isEmpty() && (movie.getPhoto() == null || movie.getPhoto().isEmpty())){
            movie.setPhoto("https://res.cloudinary.com/playrey/image/upload/v1628705378/Transparent_flag_with_question_mark_q6fawu.png");
        } else if (!file.isEmpty()){
            try {
                Map uploadResult = cloudc.upload(file.getBytes(),
                        ObjectUtils.asMap("resourcetype", "auto"));
                movie.setPhoto(uploadResult.get("url").toString());
            } catch (IOException e){
                e.printStackTrace();
                return "redirect:/addMovie";
            }
        }
        movieRepository.save(movie);
        return "redirect:/";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    @RequestMapping("/admin")
    public String admin () {
        return "admin";
    }

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model){
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        return "secure";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user,
                                          BindingResult result, Model model){
        if (result.hasErrors()){
            user.clearPassword();
            model.addAttribute("user", user);
            return "register";
        }
        else {
            model.addAttribute("user", user);
            model.addAttribute("message", "New user account created");
            user.setEnabled(true);
            userRepository.save(user);

            Role role = new Role(user.getUsername(), "ROLE_USER");
            roleRepository.save(role);
        }
        return "index";
    }

}
