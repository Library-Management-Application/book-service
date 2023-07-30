package com.library.management.bookservice.service;

import com.library.management.bookservice.config.JmsConfig;
import com.library.management.bookservice.domain.Author;
import com.library.management.bookservice.domain.Book;
import com.library.management.bookservice.events.NewBookEvent;
import com.library.management.bookservice.model.BookDto;
import com.library.management.bookservice.repo.AuthorRepository;
import com.library.management.bookservice.repo.BookRepository;
import com.library.management.bookservice.web.mappers.AuthorMapper;
import com.library.management.bookservice.web.mappers.BookMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final JmsTemplate jmsTemplate;


    // Get book details
    public BookDto findBook(Long bookId)
    {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(bookId.toString()));
        return bookMapper.bookToBookDto(book);
    }

    // Add new book
    public BookDto addNewBook(final BookDto bookDto)
    {
        final Book book = bookMapper.bookDtoToBook(bookDto);

        Author author = null;
        if (bookDto.getAuthor().getId() != null)
            author = authorRepository.findById(bookDto.getAuthor().getId())
                    .orElseThrow(() -> new EntityNotFoundException(bookDto.getAuthor().getId().toString()));
        else
        {
            author = authorRepository.findByName(bookDto.getAuthor().getName()).
                    orElse(null);
            if (author == null)
            {
                author = authorMapper.authorDtoToAuthor(bookDto.getAuthor());

                //persist this author
                author = authorRepository.saveAndFlush(author);
            }
        }
        book.setAuthor(author);
        final Book bookSaved = bookRepository.save(book);

        BookDto bookDtoAfterSave = bookMapper.bookToBookDto(bookSaved);

        jmsTemplate.convertAndSend(JmsConfig.NEW_BOOK_ADDED_QUEUE, new NewBookEvent(bookDtoAfterSave));

        return bookDto;
    }

    // Update book
    public BookDto updateBook(BookDto bookDto)
    {
        Book bookFound = bookRepository.findById(bookDto.getId()).
                orElseThrow(() -> new EntityNotFoundException(bookDto.getId().toString()));

        // check if we have enough in inventory
        if (bookDto.getQuantityCheckedOut() > bookFound.getQuantity())
        {
            throw new RuntimeException("Inventory for this book exceeded.");
        }

        bookMapper.bookDtoToBook(bookDto, bookFound);
        final Book bookSaved = bookRepository.save(bookFound);
        return bookMapper.bookToBookDto(bookSaved);
    }

    // Get Quantity At Hand
    // This is designed to be used by borrowing service
    public Integer getQuantityAtHand(Long bookId)
    {
        Book book = bookRepository.findById(bookId).
                orElseThrow(() -> new EntityNotFoundException(bookId.toString()));

        // check if we have enough in inventory
        int currentInventory = book.getQuantity() - book.getQuantityCheckedOut();
        return currentInventory;
    }

    // Update the Quantity At Hand
    // This is designed to be used by borrowing service
    public BookDto updateQuantityAtHand(Long bookId, int quantityToBorrow)
    {
        Book book = bookRepository.findById(bookId).
                orElseThrow(() -> new EntityNotFoundException(bookId.toString()));

        // check if we have enough in inventory
        validateQuantity(quantityToBorrow, book);
        int quantityCheckout = (null == book.getQuantityCheckedOut() ? 0: book.getQuantityCheckedOut()) + quantityToBorrow;
        book.setQuantityCheckedOut(quantityCheckout);
        book = bookRepository.save(book);
        return bookMapper.bookToBookDto(book);
    }

    private void validateQuantity(int quantityToBorrow, Book book) {
        int currentInventory = book.getQuantity() - book.getQuantityCheckedOut();
        if (currentInventory < quantityToBorrow) {
            throw new RuntimeException("there is not enough inventory for this book, currently available books: " + currentInventory);
        }
    }

    // Remove book
    public void removeBook(Long bookId)
    {
        Book bookFound = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException(bookId.toString()));
        // book cant be removed if people have it checkout
        if (bookFound.getQuantityCheckedOut() > 0)
        {
            throw new RuntimeException("Book deletion is not allowed since its been checkout by "  + bookFound.getQuantityCheckedOut() + " members");
        }
        bookRepository.delete(bookFound);
    }

    public List<BookDto> findAllBooks(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Book> allBooks = bookRepository.findAll(
                paging);

        if (allBooks.hasContent()) {
            List<Book> content = allBooks.getContent();

            return content.stream().map(book -> bookMapper.bookToBookDto(book)).collect(Collectors.toList());
        }
        return new ArrayList<BookDto>();
    }
}
