package uz.jl.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.jl.library.domains.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "from Book t where lower(t.name) like %:query% or lower(t.description) like %:query%")
    Page<Book> findAll(@Param("query") String searchParam, Pageable pageable);

}
