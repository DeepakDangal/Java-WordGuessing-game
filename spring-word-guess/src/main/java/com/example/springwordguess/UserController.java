package com.example.springwordguess;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String index() {
        return "form";
    }

    @PostMapping("/login-verification")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "form";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "dashboard";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }


    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

   @PostMapping("/signup-verification")
   public String signup(@RequestParam String name,
                        @RequestParam String email,
                        @RequestParam String password,
                        Model model) {

// Validate inputs (add more validation as needed)
       if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
           model.addAttribute("error", "All fields must be filled");
           return "signup";
       }

// Check if the email is already registered
       if (userRepository.findByEmail(email) != null) {
           model.addAttribute("error", "Email is already registered");
           return "signup";
       }

// Create a new user
       User newUser = new User();
       newUser.setName(name);
       newUser.setEmail(email);
       newUser.setPassword(password);

// Save the user to the database
       userRepository.save(newUser);

       return "redirect:/form"; // Redirect to the home page or login page
   }



    @GetMapping("/setting")
    public String setting(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "setting";
        } else {
            return "redirect:/";
        }
    }
}



