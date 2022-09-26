package com.backend.bms.controllers.books.methods

import com.backend.bms.models.book.Book
import com.backend.bms.models.book_codes.BookCodes
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.commons.io.IOUtils
import org.apache.commons.text.WordUtils
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

class Books {

    fun recognize( image: MultipartFile ): String? {
        val randomName: String = Integer.toHexString( Math.random().toRawBits().toInt() );
        val recognizeImage: File = File( "tmp-imgs/$randomName.jpg" );
        recognizeImage.writeBytes( image.bytes );

        val cmd: Array<String> = arrayOf( "python3", "bms-utils/extract_codes/main.py", recognizeImage.absolutePath );
        var code: String? = null;
        Runtime.getRuntime()
            .exec( cmd ).let {
                it.waitFor();
                val text: String = BufferedReader( InputStreamReader( it.inputStream ) ).readText().replace( "\n", "" );
                if ( text.isNotEmpty() ) code = text;
                recognizeImage.delete();
                it.destroy();
            };

        return code;
    }

    fun lookInOpenLibrary( isbn: String ): Array<Any>? {
        val client: HttpClient = HttpClient.newBuilder().build();
        val request: HttpRequest = HttpRequest.newBuilder()
            .uri( URI.create( "https://openlibrary.org/api/books?bibkeys=ISBN:$isbn&jscmd=details&format=json" ) )
            .GET()
            .header( "Accept", "application/json" )
            .build();

        try {
            val response: HttpResponse<String?> = client.send( request, HttpResponse.BodyHandlers.ofString() );
            val jsonString: String? = response.body();

            val jsonISBN: JsonObject = Gson().fromJson( jsonString, JsonObject::class.java )
                .get( "ISBN:$isbn" ).asJsonObject;
            val jsonDetails: JsonObject = jsonISBN.get( "details" ).asJsonObject;

            val image: ByteArray? =
                if ( jsonISBN.has( "thumbnail_url" ) )
                    getImage( jsonISBN.get( "thumbnail_url" ).asString.replace( "([-]\\w.jpg)".toRegex(), "-L.jpg" ) )
                else null;

            var description: String =
                if ( jsonDetails.has( "description" ) )
                    jsonDetails.get( "description" ).asString
                else "This book doesn't have description yet.";

            if ( description.length > 1024 )
                description = description.substring( 0, 1020 ) + "...";

            val author: String? =
                if ( jsonDetails.has( "authors" ) )
                    WordUtils.capitalize( jsonDetails.get( "authors" ).asJsonArray.get( 0 ).asJsonObject.get( "name" ).asString, '-', ' ' );
                else null;

            val publishers: String =
                if ( jsonDetails.has( "publishers" ) ) {
                    val stringedArray: String = Gson().fromJson( jsonDetails.getAsJsonArray("publishers"), Array<String>::class.java )
                        .toList()
                        .sorted()
                        .joinToString();
                    WordUtils.capitalize( stringedArray, '-', ' ' );
                } else "This book doesn't have an author yet.";

            val pages: Int =
                if ( jsonDetails.has( "number_of_pages" ) )
                    jsonDetails.get( "number_of_pages" ).asInt;
                else 0;

            val isbn10: String? =
                if ( jsonDetails.has( "isbn_10" ) ) jsonDetails.getAsJsonArray( "isbn_10" ).get( 0 ).asString
                else null;

            val isbn13: String? =
                if ( jsonDetails.has( "isbn_13" ) ) jsonDetails.getAsJsonArray( "isbn_13" ).get( 0 ).asString
                else null;

            val book: Book =
                Book(
                    image,
                    WordUtils.capitalize( jsonDetails.get( "title" ).asString, '-', ' ' ),
                    author,
                    description,
                    publishers,
                    pages,
                    jsonDetails.get( "publish_date" ).asString,
                    ( 0..5 ).random()
                );

            return arrayOf( book, BookCodes( isbn10, isbn13 ) );
        } catch ( e: Exception ) {
            System.err.println( "Could not access API: ${ e.message }" );
        }

        return null;
    }

    private fun getImage( url: String ): ByteArray? {
        try {
            return IOUtils.toByteArray( URL( url ) );
        } catch ( e: Exception ) {
            System.err.println( "Something went wrong with downloading the cover: ${ e.message }" );
        }

        return null;
    }
}