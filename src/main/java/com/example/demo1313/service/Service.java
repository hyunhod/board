package com.example.demo1313.service;

import com.example.demo1313.domain.Board;
import com.example.demo1313.domain.Comment;
import com.example.demo1313.repository.CommentRepository;
import com.example.demo1313.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    Repository repository;
    @Autowired
    CommentRepository commentRepository;

    public void store(Board board, MultipartFile file) throws Exception{

        String projectPath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID();//식별자 랜덤으로 만들어줌 이름

        String fileName = uuid+"_" +file.getOriginalFilename();

        File saveFile = new File(projectPath,fileName);

        file.transferTo(saveFile); //저장

        board.setFilename(fileName); //저장된 파일이름
        board.setFilepath("/files/"+fileName); //저장된 파일 경로와 이름

        repository.save(board);
    }

    public Page<Board> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Board> searchlist(String searchK,Pageable pageable){
        return repository.findByTitleContaining(searchK,pageable);
    }

    public Board find(Long id){
        return repository.findById(id).get();
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public Board modify(Long id){return repository.findById(id).get();}

    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

}
