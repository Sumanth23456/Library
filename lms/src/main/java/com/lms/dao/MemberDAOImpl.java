package com.lms.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lms.model.Member;

public class MemberDAOImpl implements MemberDAO {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sumanth1123*";

    // SQL queries
    private static final String INSERT_MEMBER_SQL = "INSERT INTO members (name, email, member_type) VALUES (?, ?, ?)";
    private static final String UPDATE_MEMBER_SQL = "UPDATE members SET name=?, email=?, member_type=? WHERE id=?";
    private static final String DELETE_MEMBER_SQL = "DELETE FROM members WHERE id=?";
    private static final String SELECT_MEMBER_BY_ID_SQL = "SELECT * FROM members WHERE id=?";
    private static final String SELECT_ALL_MEMBERS_SQL = "SELECT * FROM members";

    @Override
    public void addMember(Member member) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MEMBER_SQL, Statement.RETURN_GENERATED_KEYS)) {

            try {
				preparedStatement.setString(1, member.getName());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            preparedStatement.setString(2, member.getEmail());
            preparedStatement.setString(3, member.getMemberType());

            preparedStatement.executeUpdate();

            // Retrieve the auto-generated member ID
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                member.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void updateMember(Member member) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MEMBER_SQL)) {

            preparedStatement.setString(1, member.getName());
            preparedStatement.setString(2, member.getEmail());
            preparedStatement.setString(3, member.getMemberType());
            preparedStatement.setInt(4, member.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMember(int memberId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_MEMBER_SQL)) {

            preparedStatement.setInt(1, memberId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }
    }

    @Override
    public Member getMemberById(int memberId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MEMBER_BY_ID_SQL)) {

            preparedStatement.setInt(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractMemberFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_ALL_MEMBERS_SQL);

            while (resultSet.next()) {
                Member member = extractMemberFromResultSet(resultSet);
                members.add(member);
            }

        } catch (SQLException e) {
            System.err.println("Error executing the query.");
            e.printStackTrace();
        }

        return members;
    }

    // Helper method to extract a Member object from the ResultSet
    private Member extractMemberFromResultSet(ResultSet resultSet) throws SQLException {
        Member member = new Member();
        member.setId(resultSet.getInt("id"));
        member.setName(resultSet.getString("name"));
        member.setEmail(resultSet.getString("email"));
        member.setMemberType(resultSet.getString("member_type"));
        return member;
    }
}
