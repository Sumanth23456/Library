package com.lms.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.lms.model.*;

public class BookDAOImpl implements BookDAO {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sumanth1123*";

    // SQL queries
    private static final String INSERT_BOOK_SQL = "INSERT INTO books (title, author, category, quantity) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_BOOK_SQL = "UPDATE books SET title=?, author=?, category=?, quantity=? WHERE id=?";
    private static final String DELETE_BOOK_SQL = "DELETE FROM books WHERE id=?";
    private static final String SELECT_BOOK_BY_ID_SQL = "SELECT * FROM books WHERE id=?";
    private static final String SELECT_ALL_BOOKS_SQL = "SELECT * FROM books";
    private static final String BORROW_BOOK_SQL = "INSERT INTO borrowings (book_id, member_id, borrow_date) VALUES (?, ?, NOW())";
    private static final String RETURN_BOOK_SQL = "UPDATE borrowings SET return_date=NOW() WHERE book_id=? AND member_id=? AND return_date IS NULL";
    private static final String SELECT_BORROWINGS_BY_MEMBER_SQL = "SELECT * FROM borrowings WHERE member_id=?";

    @Override
    public void addBook(Book book) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getCategory());
            preparedStatement.setInt(4, book.getQuantity());

            preparedStatement.executeUpdate();

            // Retrieve the auto-generated book ID
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }
    }

    // Other methods (updateBook, deleteBook, getBookById, getAllBooks) are similar to previous implementation

    @Override
    public void borrowBook(int bookId, int memberId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(BORROW_BOOK_SQL)) {

            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, memberId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }
    }

    @Override
    public void returnBook(int bookId, int memberId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(RETURN_BOOK_SQL)) {

            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, memberId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Borrowing> getBorrowingsByMember(int memberId) {
        List<Borrowing> borrowings = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BORROWINGS_BY_MEMBER_SQL)) {

            preparedStatement.setInt(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Borrowing borrowing = new Borrowing();
                // Extract data from ResultSet and set values in the borrowing object
                borrowings.add(borrowing);
            }

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }

        return borrowings;
    }
    public void updateBook(Book book) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK_SQL)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getCategory());
            preparedStatement.setInt(4, book.getQuantity());
            preparedStatement.setInt(5, book.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBook(int bookId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK_SQL)) {

            preparedStatement.setInt(1, bookId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }
    }

    @Override
    public Book getBookById(int bookId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_ID_SQL)) {

            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractBookFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_ALL_BOOKS_SQL);

            while (resultSet.next()) {
                Book book = extractBookFromResultSet(resultSet);
                books.add(book);
            }

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }

        return books;
    }

    // Helper method to extract a Book object from the ResultSet
    private Book extractBookFromResultSet(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        book.setCategory(resultSet.getString("category"));
        book.setQuantity(resultSet.getInt("quantity"));
        return book;
    }
}


