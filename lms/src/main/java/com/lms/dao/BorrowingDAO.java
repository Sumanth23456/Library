package com.lms.dao;
import java.util.List;

import com.lms.model.Borrowing;

public interface BorrowingDAO {
    void borrowBook(int bookId, int memberId);
    void returnBook(int bookId, int memberId);
    List<Borrowing> getBorrowingsByMember(int memberId);
    List<Borrowing> getBorrowingsByBook(int bookId);
}
