package xyz.bosshuang.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xiaoq on 2018/7/22.
 */
public interface JobRepository extends JpaRepository<Job, Long> {
    boolean existsBySiteId(String siteId);
}
