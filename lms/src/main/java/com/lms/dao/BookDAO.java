package com.lms.dao;
import java.util.List;

import com.lms.model.Book;
import com.lms.model.Borrowing;

public interface BookDAO {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    Book getBookById(int bookId);
    List<Book> getAllBooks();
    void borrowBook(int bookId, int memberId);
    void returnBook(int bookId, int memberId);
    List<Borrowing> getBorrowingsByMember(int memberId);
}

