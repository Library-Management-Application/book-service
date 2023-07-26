package com.library.management.bookservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.library.management.bookservice.domain.Category;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BookDto {

    private Long id;
    @NotNull
    private String title;
    private UUID ISBN;
    private Category category;
    private String description;
    @NotNull
    private int quantity;
    @NotNull
    private int quantityCheckedOut;

    private AuthorDto author;

    @Null
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;

    @Null
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ", shape=JsonFormat.Shape.STRING)
    private OffsetDateTime lastModifiedDate;
}