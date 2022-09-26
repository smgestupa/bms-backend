package com.backend.bms.repositories.book

import com.backend.bms.models.book.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Int> {

    @Query( value = "SELECT EXISTS( SELECT * FROM favourited_books WHERE user_code = :userCode AND book_id = :bookID )", nativeQuery = true )
    fun isFavourite( @Param( "userCode" ) userCode: String, @Param( "bookID" ) bookID: Int ): Int;

    @Query( value = "INSERT INTO favourited_books VALUES ( :userCode, :bookID )", nativeQuery = true )
    fun favouriteBook( @Param( "userCode" ) userCode: String, @Param( "bookID" ) bookID: Int );

    @Query( value = "DELETE FROM favourited_books WHERE user_code = :userCode AND book_id = :bookID", nativeQuery = true )
    fun removeFavourite( @Param( "userCode" ) userCode: String, @Param( "bookID" ) bookID: Int );
}