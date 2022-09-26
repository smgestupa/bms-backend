package com.backend.bms.controllers.books

import com.backend.bms.controllers.books.methods.Books
import com.backend.bms.models.book.Book
import com.backend.bms.models.book_codes.BookCodes
import com.backend.bms.payloads.requests.GetRequest
import com.backend.bms.payloads.responses.MessageResponse
import com.backend.bms.repositories.book.BookRepository
import com.backend.bms.repositories.book.BookSummaryRepository
import com.backend.bms.repositories.book_codes.BookCodesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URLDecoder
import java.util.*

@CrossOrigin( origins = [ "*" ], maxAge = 3600 )
@RestController
@RequestMapping( "/books" )
class BooksController @Autowired constructor(
    private val bookRepository: BookRepository,
    private val bookSummaryRepository: BookSummaryRepository,
    private val bookCodesRepository: BookCodesRepository
) {

    @PreAuthorize( "hasRole('USER')" )
    @PostMapping(
        value = [ "/recognize" ],
        consumes = [ "multipart/form-data" ],
        produces = [ "application/json" ]
    )
    @ResponseBody
    @Throws( Exception::class )
    fun readImage( @RequestPart( "image" ) image: MultipartFile ): ResponseEntity<Any> {
        val isbn: String? = Books().recognize( image );
        if ( isbn == null || isbn == "-1" )
            return ResponseEntity( MessageResponse( "Sorry! Could not find anything" ), HttpStatus.BAD_REQUEST );

        if ( bookCodesRepository.existsByIsbn10( isbn ) )
            bookCodesRepository.findByIsbn10( isbn ).let {
                return ResponseEntity( MessageResponse( it.get().bookID.toString() ), HttpStatus.OK );
            }

        if ( bookCodesRepository.existsByIsbn13( isbn ) )
            bookCodesRepository.findByIsbn13( isbn ).let {
                return ResponseEntity( MessageResponse( it.get().bookID.toString() ), HttpStatus.OK );
            }

        Books().lookInOpenLibrary( isbn )?.let {
            val book: Book = it[ 0 ] as Book;
            val bookCodes: BookCodes = it[ 1 ] as BookCodes;

            book.bookCodes = bookCodes;
            bookCodes.book = book;

            bookRepository.save( book );
            return ResponseEntity( MessageResponse( book.bookID.toString() ), HttpStatus.OK );
        };

        return ResponseEntity( MessageResponse( "Cannot find the book you are looking for" ), HttpStatus.NOT_FOUND );
    }

    @PreAuthorize( "hasRole('USER')" )
    @GetMapping(
        value = [ "/{isbn}" ],
        produces = [ "application/json" ]
    )
    @Throws( Exception::class )
    fun readISBN( @PathVariable isbn: String ): ResponseEntity<Any> {
        if ( bookCodesRepository.existsByIsbn10( isbn ) )
            bookCodesRepository.findByIsbn10( isbn ).get().let {
                return ResponseEntity( MessageResponse( it.bookID.toString() ), HttpStatus.OK );
            }

        if ( bookCodesRepository.existsByIsbn13( isbn ) )
            bookCodesRepository.findByIsbn13( isbn ).get().let {
                return ResponseEntity( MessageResponse( it.bookID.toString() ), HttpStatus.OK );
            }

        Books().lookInOpenLibrary( isbn )?.let {
            val book: Book = it[ 0 ] as Book;
            val bookCodes: BookCodes = it[ 1 ] as BookCodes;

            book.bookCodes = bookCodes;
            bookCodes.book = book;

            bookRepository.save( book );
            return ResponseEntity( MessageResponse( book.bookID.toString() ), HttpStatus.OK );
        };

        return ResponseEntity( MessageResponse( "Cannot find the book you are looking for" ), HttpStatus.NOT_FOUND );
    }

    @PreAuthorize( "hasRole('USER')" )
    @GetMapping(
        value = [ "/random" ],
        produces = [ "application/json" ]
    )
    @Throws( Exception::class )
    fun getRandomBooks( @RequestParam( "n" ) amount: String ): ResponseEntity<Any> {
        bookSummaryRepository.getRandomBooks( amount.toInt() ).let {
            if ( it.isEmpty() ) return ResponseEntity( MessageResponse( "Cannot find random books" ), HttpStatus.NOT_FOUND );
            return ResponseEntity( it, HttpStatus.OK );
        }
    }

    @PreAuthorize( "hasRole('USER')" )
    @GetMapping(
        value = [ "/favourites" ],
        produces = [ "application/json" ]
    )
    @Throws( Exception::class )
    fun getFavouriteBooks( @RequestParam userCode: String ): ResponseEntity<Any> {
        bookSummaryRepository.getFavouriteBooks( userCode ).let {
            if ( it.isEmpty() ) return ResponseEntity( MessageResponse( "Cannot find favourite books" ), HttpStatus.NOT_FOUND );
            return ResponseEntity( it, HttpStatus.OK );
        }
    }

    @PreAuthorize( "hasRole('USER')" )
    @GetMapping(
        value = [ "/search" ],
        produces = [ "application/json" ]
    )
    @Throws( Exception::class )
    fun findBooks( @RequestParam( "t" ) term: String ): ResponseEntity<Any> {
        bookSummaryRepository.findByTitleLike( "%${ URLDecoder.decode( term, Charsets.UTF_8 ) }%" ).let {
            if ( it.isEmpty() ) return ResponseEntity( MessageResponse( "Cannot find random books" ), HttpStatus.NOT_FOUND );
            return ResponseEntity( it, HttpStatus.OK );
        }
    }

    @PreAuthorize( "hasRole('USER')" )
    @PostMapping(
        value = [ "/get" ],
        consumes = [ "application/json" ],
        produces = [ "application/json" ]
    )
    @ResponseBody
    @Throws( Exception::class )
    fun getBook( @RequestBody get: GetRequest ): ResponseEntity<Any> {
        bookRepository.findById( get.bookID ).get().let {
            if ( bookRepository.isFavourite( get.userCode, get.bookID ) == 1 )
                it.favourite = true;
            return ResponseEntity( it, HttpStatus.OK );
        }
    }

    @PreAuthorize( "hasRole('USER')" )
    @PostMapping(
        value = [ "/fav" ],
        consumes = [ "application/json" ],
        produces = [ "application/json" ]
    )
    @ResponseBody
    @Throws( Exception::class )
    fun favouriteBook( @RequestBody get: GetRequest ): ResponseEntity<Any> {
        if ( bookRepository.isFavourite( get.userCode, get.bookID ) == 1 ) {
            bookRepository.removeFavourite( get.userCode, get.bookID );
            return ResponseEntity( MessageResponse( "Removed from your favourites" ), HttpStatus.OK );
        }

        bookRepository.favouriteBook( get.userCode, get.bookID );
        return ResponseEntity( MessageResponse( "Added the book as a favourite" ), HttpStatus.OK );
    }
}