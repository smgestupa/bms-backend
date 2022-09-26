package com.backend.bms.repositories.book_codes

import com.backend.bms.models.book_codes.BookCodes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookCodesRepository : JpaRepository<BookCodes, Int> {

    fun existsByIsbn10( isbn: String ): Boolean;
    fun existsByIsbn13( isbn: String ): Boolean;
    fun findByIsbn10( isbn: String ): Optional<BookCodes>;
    fun findByIsbn13( isbn: String ): Optional<BookCodes>;
}