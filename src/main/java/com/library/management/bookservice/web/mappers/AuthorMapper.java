package com.library.management.bookservice.web.mappers;

import com.library.management.bookservice.domain.Author;
import com.library.management.bookservice.model.AuthorDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(uses = {DateMapper.class})
@DecoratedWith(AuthorMapperDecorator.class)
public interface AuthorMapper {

    AuthorDto authorToAuthorDto(Author author);

    Author authorDtoToAuthor(AuthorDto dto);

    void authorDtoToAuthor(AuthorDto authorDto, @MappingTarget Author authorFound);
}
