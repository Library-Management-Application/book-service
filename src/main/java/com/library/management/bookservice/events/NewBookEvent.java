package com.library.management.bookservice.events;

import com.library.management.bookservice.model.BookDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewBookEvent implements Serializable {

    private static final long serialVersionUID = 5574517713388699077L;

    private BookDto bookDto;
}
