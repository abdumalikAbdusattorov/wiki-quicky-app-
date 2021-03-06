package ssd.uz.wikiquickyapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ssd.uz.wikiquickyapp.entity.Users;

import java.util.Optional;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
public class AuditingConfig {

    @Bean
    public SpringSecurityAuditAwareImpl auditorProvider() {
        return new SpringSecurityAuditAwareImpl();
    }
}

class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals("" + authentication.getPrincipal()))) {
            return Optional.of(((Users) authentication.getPrincipal()).getId());
        }
        return Optional.empty();
    }
}
