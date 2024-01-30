package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  MemberService memberService;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  EntityManager em;

  @Test
  @DisplayName("회원가입 테스트")
  public void 회원가입() throws Exception {
    //given
    Member member = new Member();
    member.setName("kim");

    //when
    Long saveId = memberService.join(member);
    em.flush();
    //then
    assertEquals(member,memberRepository.findOne(saveId));
  }

  @Test
  @DisplayName("중복 회원 예외 테스트")
  public void 중복_회원_예외() throws Exception {
    //given
    Member member1 = new Member();
    member1.setName("kim");

    Member member2 = new Member();
    member2.setName("kim");
    //when
    memberService.join(member1);

    //then
    Exception thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
  }


  
  

}