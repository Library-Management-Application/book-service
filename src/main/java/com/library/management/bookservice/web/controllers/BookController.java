package com.library.management.bookservice.web.controllers;

import com.library.management.bookservice.model.BookDto;
import com.library.management.bookservice.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping ("/api/v1/book")
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBook(@PathVariable("bookId") Long bookId)
    {
        return new ResponseEntity<>(bookService.findBook(bookId), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<BookDto> list = bookService.findAllBooks(pageNo, pageSize, sortBy);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/quantity-at-hand/{bookId}")
    public ResponseEntity<Integer> getQuantityAtHand(@PathVariable("bookId") Long bookId)
    {
        return new ResponseEntity<>(bookService.getQuantityAtHand(bookId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookDto> addNewBook(@RequestBody @Validated BookDto bookDto)
    {
        return new ResponseEntity<>(bookService.addNewBook(bookDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<BookDto> updateBook(@RequestBody @Validated BookDto bookDto)
    {
        return new ResponseEntity<>(bookService.updateBook(bookDto), HttpStatus.ACCEPTED);
    }

    @PutMapping("/update/available/{bookId}/{quantityToBorrow}")
    public ResponseEntity<BookDto> updateQuantityAtHand(@PathVariable("bookId") Long bookId, @PathVariable("quantityToBorrow") int quantityToBorrow)
    {
        return new ResponseEntity<>(bookService.updateQuantityAtHand(bookId, quantityToBorrow), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBook(@PathVariable("bookId") Long bookId, @PathVariable("bookId") int quantityToBorrow)
    {
        bookService.updateQuantityAtHand(bookId, quantityToBorrow);
    }

}
