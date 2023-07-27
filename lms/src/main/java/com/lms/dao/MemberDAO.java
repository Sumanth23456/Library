package com.lms.dao;
import java.util.List;

import com.lms.model.Member;

public interface MemberDAO {
    void addMember(Member member);
    void updateMember(Member member);
    void deleteMember(int memberId);
    Member getMemberById(int memberId);
    List<Member> getAllMembers();
}

