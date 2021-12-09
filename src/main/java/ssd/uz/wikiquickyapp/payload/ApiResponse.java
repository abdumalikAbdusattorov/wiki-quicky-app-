package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.awt.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private boolean success;

    @Enumerated(value = EnumType.STRING)
    private TrayIcon.MessageType messageType;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
