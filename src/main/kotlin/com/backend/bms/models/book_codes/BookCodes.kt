package com.backend.bms.models.book_codes

import com.backend.bms.models.book.Book
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table( name = "book_codes" )
class BookCodes constructor(
    isbn10: String?,
    isbn13: String?
) {

    @Id
    @Column( name = "book_id" )
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @JsonIgnore
    val bookID: Int = 0;

    @Column(
        length = 10,
        nullable = true
    )
    val isbn10: String?;

    @Column(
        length = 13,
        nullable = true
    )
    val isbn13: String?;

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "book_id",
        insertable = false,
        updatable = false
    )
    @JsonBackReference
    lateinit var book: Book;

    init {
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
    }
}