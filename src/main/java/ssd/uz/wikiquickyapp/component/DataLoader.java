package ssd.uz.wikiquickyapp.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.repository.RoleRepository;
import ssd.uz.wikiquickyapp.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (initialMode.equals("always")) {
            userRepository.save(new Users(
                    "John",
                    "Doe",
                    "1",
                    passwordEncoder.encode("1"),
                    true,
                    roleRepository.findAllByName(RoleName.ROLE_SUPER_ADMIN)
            ));
        }
        //   System.out.println("koinot");
    }
}
