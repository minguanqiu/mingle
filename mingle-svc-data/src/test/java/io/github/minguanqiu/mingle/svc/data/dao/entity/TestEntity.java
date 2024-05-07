package io.github.minguanqiu.mingle.svc.data.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author Ming
 */
@Data
@Entity
@Table(schema = "public",name = "test")
public class TestEntity {

    @Id
    private String serial;

    private String text1;

    private String text2;

}
