package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResSdkNotify {
    private int error;
    private String error_note;

}
