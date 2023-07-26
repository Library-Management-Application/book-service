package com.library.management.bookservice.service;

import com.library.management.bookservice.domain.Author;
import com.library.management.bookservice.model.AuthorDto;
import com.library.management.bookservice.repo.AuthorRepository;
import com.library.management.bookservice.web.mappers.AuthorMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    // Get author details
    public AuthorDto findAuthor(Long authorId)
    {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(authorId.toString()));
        return authorMapper.authorToAuthorDto(author);
    }

    // Add new author
    public AuthorDto addNewAuthor(AuthorDto authorDto)
    {
        final Author author = authorMapper.authorDtoToAuthor(authorDto);
        final Author authorSaved = authorRepository.save(author);
        return authorMapper.authorToAuthorDto(authorSaved);
    }

    // Update author
    public AuthorDto updateAuthor(AuthorDto authorDto)
    {
        Author authorFound = authorRepository.findById(authorDto.getId()).orElseThrow(() -> new EntityNotFoundException(authorDto.getId().toString()));
        authorMapper.authorDtoToAuthor(authorDto, authorFound);
        final Author authorSaved = authorRepository.save(authorFound);
        return authorMapper.authorToAuthorDto(authorSaved);
    }

    // Remove author
    public void removeAuthor(Long authorId)
    {
        Author authorFound = authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException(authorId.toString()));
        authorRepository.delete(authorFound);
    }
}
