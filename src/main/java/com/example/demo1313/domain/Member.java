package com.example.demo1313.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min=4, message = "4글자이상")
    private String loginId;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private String password;

    @OneToMany(mappedBy = "author")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CommentReaction> reactions = new ArrayList<>();


    public String toString(){
        return "id= "+loginId+" name="+name+" pass="+password;
    }

}
