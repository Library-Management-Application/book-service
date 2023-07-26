package com.library.management.bookservice.web.mappers;

import com.library.management.bookservice.domain.Book;
import com.library.management.bookservice.model.BookDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(uses = {DateMapper.class})
@DecoratedWith(BookMapperDecorator.class)
public interface BookMapper {

    BookDto bookToBookDto(Book book);

    Book bookDtoToBook(BookDto dto);

    void bookDtoToBook(BookDto bookDto, @MappingTarget Book bookFound);
}