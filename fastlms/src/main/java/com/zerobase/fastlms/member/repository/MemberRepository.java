package com.zerobase.fastlms.member.repository;

import com.zerobase.fastlms.member.domain.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmailAuthKey(String emailAuthKey);
    Optional<Member> findByUserIdAndUserName(String userId, String userName);
    Optional<Member> findByResetPasswordKey(String resetPasswordKey);

    @Transactional
    @Modifying // select 문이 아님을 나타낸다
    @Query(value = "UPDATE member set last_login_dt = :lastLoginDt where user_id = :userId", nativeQuery = true)
    void changeLoginDt(@Param("password") LocalDateTime lastLoginDt, @Param("userId")String userId) throws Exception;
}
