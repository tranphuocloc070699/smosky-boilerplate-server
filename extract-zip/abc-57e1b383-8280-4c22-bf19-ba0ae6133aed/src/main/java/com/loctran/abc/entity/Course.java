package com.loctran.abc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_course")
public class Course{
@Id
@GeneratedValue
private Integer id;

@Column
private String name;

@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@EqualsAndHashCode.Exclude
@ToString.Exclude
@JoinTable(name = "_courseuser",
                    joinColumns = @JoinColumn(name = "_courseid"),
                    inverseJoinColumns = @JoinColumn(name = "_userid")
                )
private List<User> users;

}
