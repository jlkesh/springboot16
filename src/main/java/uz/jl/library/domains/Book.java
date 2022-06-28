package uz.jl.library.domains;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String title;
    private String author;
    private String genre;
    private String publisher;
}
