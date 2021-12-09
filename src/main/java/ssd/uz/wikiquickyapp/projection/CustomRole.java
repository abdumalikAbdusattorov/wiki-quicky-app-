package ssd.uz.wikiquickyapp.projection;

import org.springframework.data.rest.core.config.Projection;
import ssd.uz.wikiquickyapp.entity.Role;

@Projection(name = "customRole", types = {Role.class})
public interface CustomRole {
    Integer getId();

    String getName();

}
