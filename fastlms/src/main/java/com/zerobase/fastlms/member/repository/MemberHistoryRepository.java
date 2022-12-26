package com.zerobase.fastlms.member.repository;

import com.zerobase.fastlms.member.domain.Member;
import com.zerobase.fastlms.member.domain.MemberHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberHistoryRepository extends JpaRepository<MemberHistory, Long> {
}
