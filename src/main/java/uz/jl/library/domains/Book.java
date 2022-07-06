package uz.jl.library.domains;


import lombok.*;
import uz.jl.library.enums.Language;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String author;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Enumerated(EnumType.STRING)
    private Language language;
    private int pageCount;
    private int downloadCount;

    @OneToOne
    private Uploads cover;

    @OneToOne
    private Uploads file;


    @AllArgsConstructor
    @Getter
    public enum Genre {
        CRIME("Crime"),
        HORROR("Horror"),
        CI_FI("Ci-fi"),
        DRAMA("Drama"),
        ROMANCE("Romance"),
        ROMANCE_DRAMA("Romance Drama"),
        FANTASY("Fantasy");
        private final String key;

        public String getKey() {
            return key;
        }
    }
}
