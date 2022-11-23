package top.desq.dto;

import javax.validation.constraints.NotBlank;

public record ResponseDto(@NotBlank(message = "cannot be null or blank") String result,
                          @NotBlank(message = "cannot be null or blank") String message) {
}
