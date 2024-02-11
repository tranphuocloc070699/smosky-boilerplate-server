package com.loctran.demo.entity;

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
@JoinTable(name = "course_user",
                    joinColumns = @JoinColumn(name = "course_id"),
                    inverseJoinColumns = @JoinColumn(name = "user_id")
                )
private List<User> users;

}
