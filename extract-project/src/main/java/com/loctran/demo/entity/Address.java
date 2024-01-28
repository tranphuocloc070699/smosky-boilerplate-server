package com.loctran.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_address")
public class Address{
@Id
@GeneratedValue
private Integer id;

@Column
private String location;

@OneToOne(mappedBy = "address")
private User user;

}
