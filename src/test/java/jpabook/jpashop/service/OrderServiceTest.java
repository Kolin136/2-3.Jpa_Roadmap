package jpabook.jpashop.service;



import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {
  @PersistenceContext EntityManager em;
  @Autowired
  OrderService orderService;
  @Autowired
  OrderRepository orderRepository;

  @Test
  public void 상품주문() throws Exception {
    //Given
    Member member = createMember();

    Book book = createBook("시골 JPA", 10000, 10);

    int orderCount = 2;
    //When
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
    //Then
    Order getOrder = orderRepository.findOne(orderId);
    assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
    assertEquals("주문한 상품 종류 수가 정확해야 한다.",1, getOrder.getOrderItems().size());
    assertEquals("주문 가격은 가격 * 수량이다.", 10000 * 2, getOrder.getTotalPrice());
    assertEquals("주문 수량만큼 재고가 줄어야 한다.",8, book.getStockQuantity());
  }


  @Test
  @DisplayName("상품주문 재고수량 초과 테스트")
  public void 상품주문_재고수량초과() throws Exception {
    //given
    Member member = createMember();
    Item item = createBook("시골 JPA", 10000, 10);
    //창고에는 book이 10개뿐인데 주문을 11개 하면 초과해서 오류 터져야한다.
    int orderCount = 11;

    //when,then
    Exception thrown = assertThrows(NotEnoughStockException.class,()->
        orderService.order(member.getId(),item.getId(),orderCount));

  }

  @Test
  @DisplayName("주문취소 테스트")
  public void 주문취소() throws Exception {
    //given
    Member member = createMember();
    Book item = createBook("시골 JPA", 10000, 10);

    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
    //when
    orderService.cancelOrder(orderId);
    //then
    Order getOrder = orderRepository.findOne(orderId);
    assertEquals("주문 취소시 상태는 CANCLE 이다",OrderStatus.CANCEL,getOrder.getStatus());
    assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
  }

  private Book createBook(String name, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    em.persist(book);
    return book;
  }

  private Member createMember() {
    Member member = new Member();
    member.setName("회원1");
    member.setAddress(new Address("서울","강가","123-123"));
    em.persist(member);
    return member;
  }



  
}