package com.example.demo1313.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Board {

    @GeneratedValue @Id
    private Long id;

    @NotBlank
    @Size(min=3, message = "3글자이상")
    private String title;
    @NotBlank
    @Size(min = 3, message = "3글자이상")
    private String content;

    @ManyToOne
    @JoinColumn(name="member_name")
    private Member author;

    private String filename;
    private String filepath;
}
