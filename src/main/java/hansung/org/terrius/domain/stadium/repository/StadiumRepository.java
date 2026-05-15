package hansung.org.terrius.domain.stadium.repository;

import hansung.org.terrius.domain.stadium.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {

    @Query("""
            select s
            from Stadium s
            where (:province is null or s.province = :province)
              and (:city is null or s.city = :city)
              and (:name is null or s.name like concat('%', :name, '%'))
            order by s.id asc
            """)
    List<Stadium> findAllBySearchCondition(
            @Param("province") String province,
            @Param("city") String city,
            @Param("name") String name
    );
}
