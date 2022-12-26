package com.zerobase.fastlms.member.service.impl;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.mapper.MemberMapper;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.components.MailComponents;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.member.domain.Member;
import com.zerobase.fastlms.member.domain.MemberCode;
import com.zerobase.fastlms.member.domain.MemberHistory;
import com.zerobase.fastlms.member.exception.MemberNotEmailAuthException;
import com.zerobase.fastlms.member.exception.MemberStopUserException;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.repository.MemberRepository;
import com.zerobase.fastlms.member.service.MemberService;
import com.zerobase.fastlms.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    private final MemberMapper memberMapper;

    /**
     * 회원 가입
     */
    @Override
    public boolean register(MemberInput memberInput) {
        Optional<Member> optionalMember = memberRepository.findById(memberInput.getUserId());
        if (optionalMember.isPresent()) {
            // 현재 userId에 해당하는 데이터 존재
            return false;
        }

        // 비밀번호 암호화 및 인코딩
        String encPassword = BCrypt.hashpw(memberInput.getPassword(), BCrypt.gensalt());

        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(memberInput.getUserId())
                .userName(memberInput.getUserName())
                .phone(memberInput.getPhone())
                .password(encPassword)
                .registerDt(LocalDateTime.now())
                .emailAuthYn(false)
                // 임의의 String값 생성을 위해 UUID 사용
                .emailAuthKey(uuid)
                .userStatus(Member.MEMBER_STATUS_REQ)
                .build();

        memberRepository.save(member);

        // TODO: 이메일 정보
        // 1. DB에 저장해서 필요한 부분만 읽어오기
        // 2. 관리자 페이지에서 메일 수정을 통해 내용 변경
        String email = memberInput.getUserId();
        String subject = "Fastlms 가입을 축하드립니다.";
        String text = "<p>Fastlms 가입을 축하드립니다.</p>" +
                "<p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>" +
                "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" +
                uuid +
                "'> 가입 완료 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    /**
     * 메일 인증 및 가입 활성화
     */
    @Override
    public boolean emailAuth(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        // member 정보 가져오기
        Member member = optionalMember.get();

        // 이미 활성화됐으면 false
        if (member.isEmailAuthYn()) {
            return false;
        }

        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        member.setUserStatus(Member.MEMBER_STATUS_ING);

        memberRepository.save(member);

        return true;
    }

    /**
     * 비밀번호 초기화 메일 전송
     */
    @Override
    public boolean sendResetPassword(ResetPasswordInput resetPasswordInput) {
        Optional<Member> optionalMember = memberRepository.findByUserIdAndUserName(
                resetPasswordInput.getUserId(),
                resetPasswordInput.getUserName()
        );

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        String uuid = UUID.randomUUID().toString();

        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1)); // 초기화 유효기간 하루 추가
        memberRepository.save(member);


        // TODO: 비밀번호 초기화 이메일 정보
        // 1. DB에 저장해서 필요한 부분만 읽어오기
        // 2. 관리자 페이지에서 메일 수정을 통해 내용 변경

        // 사용자한테 메일을 보낸 후에 사용자가 해당 링크를 타고 들어오면 우리 쪽 사이트에서 해당 유저의 Id, Pwd를 입력해서
        // 해당 값을 확인 후 초기화 진행
        // 그러므로 사용자만 알 수 있는 유일한 값(key)을 같이 보내야됨.
        String email = resetPasswordInput.getUserId();
        String subject = "[Fastlms] 비밀번호 초기화 메일 입니다.";
        String text = "<p>Fastlms 비밀번호 초기화 메일 입니다.</p>" +
                "<p>아래 링크를 클릭하셔서 비밀번호를 초기화해주세요..</p>" +
                "<div><a target='_blank' href='http://localhost:8080/member/reset/password?id=" +
                uuid +
                "'> 비밀번호 초기화 링크 </a></div>";
        mailComponents.sendMail(email, subject, text);


        return true;
    }

    /**
     * 실제 비밀번호 초기화
     */
    @Override
    public boolean resetPassword(String uuid, String password) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        // 초기화 유효 기간 날짜 체크
        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        // 암호화된 password로 설정
        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        memberRepository.save(member);

        return true;
    }

    /**
     * 입력받은 uuid 값이 유효한지 확인
     */
    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);

        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        // 초기화 유효 기간 날짜 체크
        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        // 가입 인증 후 정상 진행
        if (Objects.equals(member.getUserStatus(), Member.MEMBER_STATUS_REQ)) {
            throw new MemberNotEmailAuthException("이메일 활성화 이후 로그인해주세요.");
        }

        // 회원 상태가 정지 상태일 때
        if (Objects.equals(member.getUserStatus(), Member.MEMBER_STATUS_STOP)) {
            throw new MemberStopUserException("정지된 회원 입니다..");
        }

        // 탈퇴한 회원일 때
        if (Objects.equals(member.getUserStatus(), Member.MEMBER_STATUS_WITHDRAW)) {
            throw new MemberStopUserException("탈퇴한 회원입니다.");
        }
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 회원 가입 시 관리자 계정이면 role_admin 추가
        if (member.isAdminYn()) {
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }


        // spring security한테 id, pwd, 역할을 알려준다.
        return new User(member.getUserId(), member.getPassword(), grantedAuthorityList);
    }

    @Override
    public List<MemberDto> list(MemberParam memberParam) {

        long totalCount = memberMapper.selectListCount(memberParam);

        List<MemberDto> list = memberMapper.selectList(memberParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (MemberDto memberDto : list) {
                memberDto.setTotalCount(totalCount);

                // 테이블 NO 셋팅
                memberDto.setSequential(totalCount - memberParam.getPageStart() - i);
                i++;
            }
        }
        return list;
//        return memberRepository.findAll();
    }

    @Override
    public MemberDto detail(String userId) {
        Optional<Member> optionalMember = memberRepository.findById(userId);

        if (!optionalMember.isPresent()) {
            return null;
        }
        Member member = optionalMember.get();

        return MemberDto.of(member);
    }

    @Override
    public boolean updateStatus(String userId, String userStatus) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        member.setUserStatus(userStatus);

        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        member.setPassword(encPassword);

        memberRepository.save(member);

        return true;
    }

    @Override
    public ServiceResult updateMemberPassword(MemberInput memberInput) {
        String userId = memberInput.getUserId();

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        // 기존 비밀번호와 비교
//        if(!BCrypt.checkpw(memberInput.getPassword(), member.getPassword())){
        if (!PasswordUtils.equals(memberInput.getPassword(), member.getPassword())) {
            return new ServiceResult(false, "비밀 번호가 일치하지 않습니다.");
        }

//        String encPassword = BCrypt.hashpw(memberInput.getNewPassword(), BCrypt.gensalt());
        String encPassword = PasswordUtils.encPassword(memberInput.getNewPassword());
        member.setPassword(encPassword);
        memberRepository.save(member);

        return new ServiceResult(true);
    }

    @Override
    public ServiceResult updateMember(MemberInput memberInput) {
        String userId = memberInput.getUserId();

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        member.setPhone(memberInput.getPhone());
        member.setZipCode(memberInput.getZipCode());
        member.setAddress(memberInput.getAddress());
        member.setAddressDetail(memberInput.getAddressDetail());
        member.setUpdateDt(LocalDateTime.now());
        memberRepository.save(member);

        return new ServiceResult(true);
    }

    @Override
    public ServiceResult withdraw(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (!PasswordUtils.equals(password, member.getPassword())) {
            return new ServiceResult(false, "비밀번호가 일치하지 않습니다.");
        }

        member.setUserName("삭제회원");
        member.setPhone("");
        member.setPassword("");
        member.setRegisterDt(null);
        member.setUpdateDt(null);
        member.setEmailAuthYn(false);
        member.setEmailAuthDt(null);
        member.setEmailAuthKey(null);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        member.setUserStatus(MemberCode.MEMBER_STATUS_WITHDRAW);
        member.setZipCode("");
        member.setAddressDetail("");
        member.setAddress("");

        memberRepository.save(member);

        return new ServiceResult(true);
    }

}
