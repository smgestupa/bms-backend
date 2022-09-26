package com.backend.bms.repositories.book

import com.backend.bms.models.book.BookSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BookSummaryRepository : JpaRepository<BookSummary, Int> {

    @Query( "SELECT * FROM books ORDER BY RAND() LIMIT :amount", nativeQuery = true )
    fun getRandomBooks( @Param( "amount" ) amount: Int ): List<BookSummary>;

    @Query( "SELECT * FROM favourited_books f JOIN books b USING (book_id) WHERE user_code = :userCode", nativeQuery = true )
    fun getFavouriteBooks( @Param( "userCode" ) userCode: String ): List<BookSummary>;

    fun findByTitleLike( title: String ): List<BookSummary>;
}