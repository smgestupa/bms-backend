package com.backend.bms.models.book

import com.backend.bms.models.book_codes.BookCodes
import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

@Entity
@Table( name = "books" )
class BookSummary constructor(
    cover: ByteArray?,
    title: String,
    author: String?
) {

    @Id
    @Column( name = "book_id" )
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    val bookID: Int = 0;

    @Column
    val cover: ByteArray?;

    @Column(
        length = 128,
        nullable = false
    )
    val title: String;

    @Column( length = 64 )
    val author: String?;

    init {
        this.cover = cover;
        this.title = title;
        this.author = author;
    }
}