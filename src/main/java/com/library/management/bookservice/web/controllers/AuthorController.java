package com.library.management.bookservice.web.controllers;

import com.library.management.bookservice.model.AuthorDto;
import com.library.management.bookservice.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping ("/api/v1/author")
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable("authorId") Long authorId)
    {
        return new ResponseEntity<>(authorService.findAuthor(authorId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuthorDto> addNewAuthor(@RequestBody @Validated AuthorDto authorDto)
    {
        return new ResponseEntity<>(authorService.addNewAuthor(authorDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<AuthorDto> updateAuthor(@RequestBody @Validated AuthorDto authorDto)
    {
        return new ResponseEntity<>(authorService.updateAuthor(authorDto), HttpStatus.NO_CONTENT);
    }
}
