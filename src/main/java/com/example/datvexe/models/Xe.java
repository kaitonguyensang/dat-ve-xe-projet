package com.example.datvexe.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "xe")
public class Xe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "biensoxe")
    private String bienSoXe;

    @ManyToOne
    @JoinColumn(name = "nhaxe_id",referencedColumnName = "id")
    @JsonManagedReference
    private NhaXe nhaXe;

    @ManyToOne
    @JoinColumn(name = "loaixe_id",referencedColumnName = "id")
    @JsonManagedReference
    private LoaiXe loaiXe;

    @OneToMany(mappedBy="xe")
    @JsonIgnore
    private List<TuyenXe> tuyenXe;

}
