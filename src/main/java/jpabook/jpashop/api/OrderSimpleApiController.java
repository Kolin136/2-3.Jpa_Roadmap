package jpabook.jpashop.api;

import java.util.List;
import jpabook.jpashop.api.MemberApiController.MemberDto;
import jpabook.jpashop.api.MemberApiController.Result;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 *
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;

  @GetMapping("/api/v1/simple-orders")
  public List<Order> ordersV1(){
    List<Order> all = orderRepository.findAllByString(new OrderSearch());
    return all;
  }



}
