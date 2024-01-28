package com.loctran.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
public class User{
@Id
@GeneratedValue
private Integer id;

@Column
private String firstName;

@Column
private String lastName;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "address_id", referencedColumnName = "id")
private Address address;

@OneToMany(mappedBy = "user")
private List<Post> posts;

@ManyToMany(mappedBy = "users")
@EqualsAndHashCode.Exclude
@ToString.Exclude
private List<Course> courses;

}
