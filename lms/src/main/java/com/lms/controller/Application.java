package com.lms.controller;
import java.util.List;
import com.lms.dao.*;
import com.lms.model.*;

public class Application {

    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAOImpl();
        MemberDAO memberDAO = new MemberDAOImpl();
        BorrowingDAO borrowingDAO = new BorrowingDAOImpl();

        // Adding a new book
        Book newBook = new Book();
        newBook.setTitle("The Great Gatsby");
        newBook.setAuthor("F. Scott Fitzgerald");
        newBook.setCategory("Fiction");
        newBook.setQuantity(5);
        bookDAO.addBook(newBook);

        // Adding a new member
        Member newMember = new Member();
        newMember.setName("John Doe");
        newMember.setEmail("john.doe@example.com");
        newMember.setMemberType("Student");
        memberDAO.addMember(newMember);

        // Borrowing a book
        int bookId = newBook.getId();
        int memberId = newMember.getId();
        borrowingDAO.borrowBook(bookId, memberId);

        // Returning a book
        borrowingDAO.returnBook(bookId, memberId);

        // Getting all books
        List<Book> allBooks = bookDAO.getAllBooks();
        for (Book book : allBooks) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }

        // Getting all members
        List<Member> allMembers = memberDAO.getAllMembers();
        for (Member member : allMembers) {
            System.out.println(member.getName() + " (" + member.getMemberType() + ")");
        }
    }
}

