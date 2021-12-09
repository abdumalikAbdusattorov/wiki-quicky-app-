package ssd.uz.wikiquickyapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseModel extends ApiResponse {
    private boolean success;
    private String message;
    private Object data;

    public ApiResponseModel(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
