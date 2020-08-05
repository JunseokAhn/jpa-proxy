package proxy;

import org.hibernate.Hibernate;
import proxy.Member;
import proxy.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain2 {
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

        //@ManyToOne(fetch = FetchType.LAZY)를 설정하면, team을 프록시로 가져오게된다 == 지연로딩
        //@ManyToOne(fetch = FetchType.EAGER)를 설정하면, team을 가져오게된다 == 즉시로딩
        Member findMember = EM.find(Member.class, member.getId());
        System.out.println(findMember.getTeam().getClass());

        //너무 쿼리가 많이 나오기때문에 성능이안나옴. 가급적 지연로딩으로 가져오는게 좋다.
        //ManyToOne, OneToOne은 기본이 즉시로딩 > LAZY로 설정
        //OneToMany, ManyToMany는 기본이 지연로딩
        System.out.println("===================================");
        //이때 초기화가 일어남. 지연로딩.
        System.out.println(findMember.getTeam().getName());

        EM.flush();
        EM.clear();

        //멤버를 셀렉트해온 후 즉시로딩으로 되어있으면 팀을 다시 셀렉트해옴
        List<Member> members = EM.createQuery("select m from Member m", Member.class).getResultList();

        System.out.println("===================================");
        //즉시로딩은 비효율적이므로 지연로딩으로 해놓은다음 펫치조인을 하면 팀까지 다 가져옴.
        List<Member> fetchMembers = EM.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();

        TS.commit();
        EM.close();
        EMF.close();
    }

}
