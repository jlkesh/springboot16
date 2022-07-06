package uz.jl.library.dto;


import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookUpdateDTO {
    private Long id;
    private String name;
    private String description;
    private int downloadCount;
}
