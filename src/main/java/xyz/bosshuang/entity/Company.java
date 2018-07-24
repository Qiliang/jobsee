package xyz.bosshuang.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by xiaoq on 2018/7/22.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 50,unique = true)
    private String siteId;
    private String name;
    @Column(length = 4000)
    private String description;
    private String address;
    private String clazz;
    private String num;
    private String domain;
    private String source;

}
