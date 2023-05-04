package jpa_spring.jpa_practice.service;

import jpa_spring.jpa_practice.domain.Member;
import jpa_spring.jpa_practice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원가입
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    //회원 아이디로 찾기

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }


    //회원전체조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }



}
