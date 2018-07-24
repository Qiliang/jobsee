package xyz.bosshuang.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by xiaoq on 2018/7/22.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 50,unique = true)
    private String siteId;
    private Long companyId;
    private String companySiteId;
    private String title;
    private String publish;
    private LocalDateTime publishTime;
    private String experience;
    private String education;
    private String num;
    private String tags;
    @Column(length = 4000)
    private String description;
    private String address;
    private String area;
    private String salary;
    private String source;

}
