package com.lms.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lms.model.Borrowing;

public class BorrowingDAOImpl implements BorrowingDAO {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sumanth1123*";

    // SQL queries
    private static final String BORROW_BOOK_SQL = "INSERT INTO borrowings (book_id, member_id, borrow_date) VALUES (?, ?, NOW())";
    private static final String RETURN_BOOK_SQL = "UPDATE borrowings SET return_date=NOW() WHERE book_id=? AND member_id=? AND return_date IS NULL";
    private static final String SELECT_BORROWINGS_BY_MEMBER_SQL = "SELECT * FROM borrowings WHERE member_id=?";
    private static final String SELECT_BORROWINGS_BY_BOOK_SQL = "SELECT * FROM borrowings WHERE book_id=?";

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
                Borrowing borrowing = extractBorrowingFromResultSet(resultSet);
                borrowings.add(borrowing);
            }

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }

        return borrowings;
    }

    @Override
    public List<Borrowing> getBorrowingsByBook(int bookId) {
        List<Borrowing> borrowings = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BORROWINGS_BY_BOOK_SQL)) {

            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Borrowing borrowing = extractBorrowingFromResultSet(resultSet);
                borrowings.add(borrowing);
            }

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }

        return borrowings;
    }

    // Helper method to extract a Borrowing object from the ResultSet
    private Borrowing extractBorrowingFromResultSet(ResultSet resultSet) throws SQLException {
        Borrowing borrowing = new Borrowing();
        borrowing.setId(resultSet.getInt("id"));
        // Fetch the book and member details from their respective DAOs
        // Set the book and member objects in the borrowing
        borrowing.setBorrowDate(resultSet.getTimestamp("borrow_date"));
        borrowing.setReturnDate(resultSet.getTimestamp("return_date"));
        return borrowing;
    }
}

