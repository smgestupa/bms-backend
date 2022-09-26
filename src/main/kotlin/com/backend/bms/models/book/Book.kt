package com.backend.bms.models.book

import com.backend.bms.models.book_codes.BookCodes
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*
import kotlin.jvm.Transient
import kotlin.math.max

@Entity
@Table( name = "books" )
class Book constructor(
    cover: ByteArray?,
    title: String,
    author: String?,
    description: String,
    publisher: String,
    pages: Int,
    publishDate: String,
    rating: Int
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

    @Column(
        length = 1024,
        nullable = false
    )
    val description: String;

    @Column(
        length = 64,
        nullable = false
    )
    val publisher: String;

    @Column( nullable = false )
    val pages: Int;

    @Column(
        name = "publish_date",
        length = 18,
        nullable = false
    )
    val publishDate: String;

    @Column(
        length = 1,
        nullable = false
    )
    val rating: Int;

    @Transient
    var favourite: Boolean = false;

    @OneToOne(
        cascade = [ CascadeType.ALL ],
        fetch = FetchType.LAZY,
        mappedBy = "book"
    )
    @JsonManagedReference
    lateinit var bookCodes: BookCodes;

    init {
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publisher = publisher;
        this.pages = pages;
        this.publishDate = publishDate;
        this.rating = rating;
    }
}