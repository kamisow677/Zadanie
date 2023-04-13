package com.vattenfall.bookstore.utils

import com.github.javafaker.Faker
import com.vattenfall.bookstore.application.BookDto
import com.vattenfall.bookstore.infrastructure.persistence.BookEntity

class BooksHelper {
    private static final Integer VALID_BOOK_ISBN_LENGTH = 13

    private String id = 0
    private String isbn = ""
    private String title = ""
    private Integer authorId = 0

    BooksHelper(Faker faker) {
        this.isbn = faker.number().digits(VALID_BOOK_ISBN_LENGTH).toString()
        this.title = faker.book().title().toString()
        this.authorId = faker.number().digit().toInteger()
        this.id = faker.number().digit().toInteger()
    }

    BooksHelper setId(Long id) {
        this.id = id
        this
    }

    BooksHelper setIsbn(String isbn) {
        this.isbn = isbn
        this
    }

    BooksHelper setTitle(String title) {
        this.title = title
        this
    }

    BooksHelper setAuthorId(Integer authorId) {
        this.authorId = authorId
        this
    }

     def buildBookEntity() {
        new BookEntity().with {
            it.isbn = this.isbn
            it.title = this.title
            it.authorId = this.authorId
            it
        }
    }

     def buildBookDto() {
        BookDto entity = new BookDto(
                this.isbn,
                this.title,
                this.authorId
        )
        entity
    }

}
