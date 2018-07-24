package xyz.bosshuang.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xiaoq on 2018/7/22.
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsBySiteId(String siteId);
}
