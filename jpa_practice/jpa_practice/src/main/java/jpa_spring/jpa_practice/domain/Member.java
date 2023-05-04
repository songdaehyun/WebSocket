package jpa_spring.jpa_practice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    protected Member() {}

    public Member(String username) {
        this.name = username;
    }
}
