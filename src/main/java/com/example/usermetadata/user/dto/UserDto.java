package com.example.usermetadata.user.dto;

import jakarta.annotation.Nullable;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Nullable
    private Long userId;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String phone;
}
