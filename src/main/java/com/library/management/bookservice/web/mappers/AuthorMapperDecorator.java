package com.library.management.bookservice.web.mappers;

import com.library.management.bookservice.domain.Author;
import com.library.management.bookservice.model.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AuthorMapperDecorator implements AuthorMapper {
    private AuthorMapper mapper;

    @Autowired
    public void setMapper(AuthorMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public AuthorDto authorToAuthorDto(Author author) {
        return mapper.authorToAuthorDto(author);
    }

    @Override
    public Author authorDtoToAuthor(AuthorDto authorDto) {
        return mapper.authorDtoToAuthor(authorDto);
    }
}

