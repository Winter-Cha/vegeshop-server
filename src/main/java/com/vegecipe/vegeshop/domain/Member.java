package com.vegecipe.vegeshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private  String name;

    @Embedded
    private Address address;

    @JsonIgnore // 엔티티에 프리젠테이션 계층이 없는게 좋음...
    @OneToMany(mappedBy = "member") // mappedBy : 나는 그냥 연결된 거울일뿐이야 변경할 수 없어
    private List<Order> orders = new ArrayList<>();

}

