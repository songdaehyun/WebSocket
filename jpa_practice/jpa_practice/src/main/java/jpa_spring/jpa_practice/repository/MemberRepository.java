package jpa_spring.jpa_practice.repository;

import jpa_spring.jpa_practice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
