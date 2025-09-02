package br.com.ocauamotta.PetLar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalDTO {

    @NotNull(message = "Falha ao receber ID")
    private String id;

    @Size(min = 3, max = 100)
    private String name;

    private Integer age;

    private String type;

    @Size(min = 3, max = 100)
    private String breed;

    private String sex;

    private Integer weight;

    private String size;

    private LocalDate registrationDate;

    private String status;

    @Size(min = 3, max = 250)
    private String description;

    private String urlImage;
}
