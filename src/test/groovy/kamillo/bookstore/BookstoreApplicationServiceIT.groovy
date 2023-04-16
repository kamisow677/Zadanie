package com.vattenfall.bookstore

import com.vattenfall.bookstore.application.AuthorDoesNotExistsException
import com.vattenfall.bookstore.application.BookstoreApplicationService
import com.vattenfall.bookstore.application.InvalidBookDataException
import com.vattenfall.bookstore.infrastructure.persistence.AuthorDao
import com.vattenfall.bookstore.infrastructure.persistence.AuthorEntity
import com.vattenfall.bookstore.infrastructure.persistence.BookEntity
import com.vattenfall.bookstore.utils.BaseIntegration
import com.vattenfall.bookstore.utils.BooksHelper
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired

class BookstoreApplicationServiceIT extends BaseIntegration {

	private static final String INVALID_BOOK_TITLE = ""
	private static final Integer INVALID_BOOK_ISBN_LENGTH = 10

    @Autowired
    private AuthorDao authorGeneratedRepo

    @Autowired
    private BookstoreApplicationService service

    def cleanup() {
        authorGeneratedRepo.deleteAll()
        bookGeneratedRepo.deleteAll()
    }

    def "should return collection of stored books"() {
        given:
			List<BookEntity> entities = [generateBookEntity(), generateBookEntity()]
			bookGeneratedRepo.saveAll(entities)
        when:
        	def booksResponse = service.findBooks()
        then:
			entities.forEach {
				entity ->
					{
						Assertions.assertTrue(
								booksResponse.any { response ->
									[entity.title, entity.authorId, entity.isbn] ==
											[response.title, response.authorId, response.isbn]
								}
						)
					}
			}

    }

    def "should throw AuthorDoesNotExistsException when add book"() {
        when:
        	service.addBook(generateBookDto())
        then:
        	thrown(AuthorDoesNotExistsException.class)
    }

    def "should throw InvalidBookDataException when invalid isbn book"() {
        given:
			def toSave = generateInvalidISBNBookDto()
			saveAuthor(toSave.authorId)
        when:
        	service.addBook(toSave)
        then:
        	thrown(InvalidBookDataException.class)
    }

    def "should throw InvalidBookDataException when invalid title book"() {
        given:
			def toSave = generateInvalidTitleBookDto()
			saveAuthor(toSave.authorId)
        when:
        	service.addBook(toSave)
        then:
        	thrown(InvalidBookDataException.class)
    }

    def "should add book"() {
        given:
			def toSave = generateBookDto()
			saveAuthor(toSave.authorId)
        when:
			service.addBook(toSave)
        then:
			def savedBook = bookGeneratedRepo.findAll().get(0)
			verifyAll {
				savedBook.isbn == toSave.isbn
				savedBook.title == toSave.title
				savedBook.authorId == toSave.authorId
			}
    }

    private void saveAuthor(Integer authorId) {
        def author = new AuthorEntity()
        author.setAuthorId(authorId)
        authorGeneratedRepo.save(author)
    }

    private def generateBookEntity() {
        new BooksHelper(faker).buildBookEntity()
    }

    private def generateBookDto() {
        new BooksHelper(faker).buildBookDto()
    }

    private def generateInvalidISBNBookDto() {
        new BooksHelper(faker)
                .setIsbn(faker.number().digits(INVALID_BOOK_ISBN_LENGTH).toString())
                .buildBookDto()
    }

    private def generateInvalidTitleBookDto() {
        new BooksHelper(faker)
                .setTitle(INVALID_BOOK_TITLE)
                .buildBookDto()
    }

}
