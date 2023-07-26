package com.library.management.bookservice.web.mappers;

import com.library.management.bookservice.domain.Book;
import com.library.management.bookservice.model.BookDto;
import com.library.management.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class BookMapperDecorator implements BookMapper {
    private BookMapper mapper;

    @Autowired
    public void setMapper(BookMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BookDto bookToBookDto(Book book) {
       return mapper.bookToBookDto(book);
    }

    @Override
    public Book bookDtoToBook(BookDto bookDto) {
        return mapper.bookDtoToBook(bookDto);
    }
}
