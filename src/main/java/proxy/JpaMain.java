package proxy;

import org.hibernate.Hibernate;
import proxy.Member;
import proxy.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory EMF = Persistence.createEntityManagerFactory("hello");
        EntityManager EM = EMF.createEntityManager();
        EntityTransaction TS = EM.getTransaction();

        TS.begin();
        Member member = new Member();
        Team team = new Team();
        team.setName("TeamA");
        member.setName("Junseok");
        member.setTeam(team);

        EM.persist(member);
        EM.persist(team);

        EM.flush();
        EM.clear();
        //영속성컨텍스트에 객체가남아있으면 getReference해도 프록시객체가아니라 그냥 객체가됨
        //반대로 한번 프록시를 조회하면 find로 찾아와도 프록시로찾아옴
        Member findMember = EM.getReference(Member.class, 1L);
        System.out.println("===================================");
        
        //getReference로 가져온 클래스는 가짜객체로, 당장 쿼리가 나가는것이 아니다
        System.out.println(findMember.getClass());

        System.out.println("프록시가 초기화되었는가 : "+EMF.getPersistenceUnitUtil().isLoaded(findMember));
        System.out.println(findMember.getName());
        //member를 찾아오더라도 클래스속성은 계속 프록시로 남아있다. 즉 타입체크시 ==대신에 instance of사용
        System.out.println(findMember.getClass());

        System.out.println("프록시가 초기화되었는가 : "+EMF.getPersistenceUnitUtil().isLoaded(findMember));
        System.out.println("instance of : "+ (findMember instanceof Member));

        //하이버네이트에서 제공하는 강제초기화 기능
        Hibernate.initialize(findMember);

        TS.commit();
        EM.close();
        EMF.close();
    }

}
