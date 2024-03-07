package com.ludigi.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/")
    public String index(Model model) {
        ClientRegistration springRegistration = this.clientRegistrationRepository.findByRegistrationId("client1");
        String clientId = springRegistration.getClientId();
        model.addAttribute("clientId", clientId);
        return "home";
    }
}
