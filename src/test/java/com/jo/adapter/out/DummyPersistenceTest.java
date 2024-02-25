package com.jo.adapter.out;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

import com.jo.domain.model.Dummy;


@DataJpaTest
@Import({DummyPersistenceAdapter.class, DummyMapper.class})
@Sql(scripts = { "schema.sql", "db.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class DummyPersistenceTest {
    
    @Autowired
    private DummyPersistenceAdapter dummyPersistenceAdapter;

    @Test
    public void findById() {
        var dummy = dummyPersistenceAdapter.findById(1);

        assertThat(dummy.getInfo()).isEqualTo("dummy 1");
    }

    @Test
    public void findAll() {
        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id")));

        var result = dummyPersistenceAdapter.findAll(pageable);

        assertThat(result.getRight().size()).isEqualTo(2);
    }

    @Test
    public void save() {
        var dummy = new Dummy("saved");
        dummy = dummyPersistenceAdapter.save(dummy);

        assertThat(dummy.getInfo()).isEqualTo("saved");
    }
}
