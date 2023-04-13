package com.vattenfall.bookstore

import com.vattenfall.bookstore.application.BookDto
import com.vattenfall.bookstore.infrastructure.persistence.BookEntity
import com.vattenfall.bookstore.utils.BaseIntegration
import com.vattenfall.bookstore.utils.BooksHelper
import org.junit.jupiter.api.Assertions
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class BookRestControllerIT extends BaseIntegration {

	def cleanup() {
		bookGeneratedRepo.deleteAll()
	}

	def "should return collection of stored books, when GET request call on /books"() {
		given:
			List<BookEntity> entities = [generateBookEntity(), generateBookEntity()]
			bookGeneratedRepo.saveAll(entities)
		when:
			def action = mvc.perform(get("/books"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
			String contentAsString = action.getResponse().getContentAsString()
			List<BookDto> booksResponse = objectMapper.readValue(contentAsString, List<BookDto>.class)
		then:
			 entities.forEach {
				entity -> {
					Assertions.assertTrue(
							booksResponse.any { response ->
							[entity.title, entity.authorId, entity.isbn] ==
									[response.title, response.authorId, response.isbn]
						}
					)}
				}

	}

	def "should not store book, when POST request call on /books without stored author"() {
		when:
			BookEntity toSave = generateBookEntity()
		then:
			mvc.perform(
				post("/books")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(toSave))
			).andExpect(MockMvcResultMatchers.status().isBadRequest())
	}

	private def generateBookEntity() {
		new BooksHelper(faker).buildBookEntity()
	}

}
