package cascade;


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

        Child child = new Child();
        Child child2 = new Child();
        Parent parent = new Parent();

        parent.addChildren(child);
        parent.addChildren(child2);

        //cascade옵션으로 parent만 영속화시켜도 자동으로 child도 인서트
        EM.persist(parent);

        EM.flush();
        EM.clear();

        //orphanRemoval옵션으로 parent에서 child를 지울수있음
        Parent findParent = EM.find(Parent.class, parent.getId());
        findParent.getChildren().remove(0);

        //cascade옵션으로 delete cascade할수있음
        EM.remove(findParent);

        TS.commit();
        EM.close();
        EMF.close();
    }

}
