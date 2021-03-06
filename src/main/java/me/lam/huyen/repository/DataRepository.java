package me.lam.huyen.repository;

import me.lam.huyen.model.Data;
import me.lam.huyen.model.ReposHealthScore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends CrudRepository<Data, String> {

    @Query(value = "SELECT DISTINCT * FROM data " +
            "WHERE key = 'full_name' " +
                "AND object_id NOT IN (SELECT DISTINCT object_id FROM data WHERE key = :excludingKey) " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Data> findProjectsWithNoKey(@Param("excludingKey") String excludingKey, @Param("limit") int limit);

    @Query(value = "SELECT * FROM repos_health_score ", nativeQuery = true)
    List<ReposHealthScore> getReport();
}
