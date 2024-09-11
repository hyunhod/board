package com.example.demo1313.controller;


import com.example.demo1313.domain.Board;
import com.example.demo1313.domain.Comment;
import com.example.demo1313.domain.Member;
import com.example.demo1313.repository.CommentRepository;
import com.example.demo1313.service.CommentService;
import com.example.demo1313.service.Service;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    Service service;
    @Autowired
    CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("board/write")
    public String write(Model model) {

        model.addAttribute("board",new Board());
        return "board";
    }

    @PostMapping("board/write/do")
    public String store(@Valid @ModelAttribute("board") Board board, BindingResult result, Model model, HttpSession session,@RequestParam("file") MultipartFile file) throws Exception{
        if(result.hasErrors()){
            model.addAttribute("message","최소 3글자 이상 입력하세요");
            model.addAttribute("searchUrl","/board/write");
            return "message";
        }
        Member member = (Member) session.getAttribute("loginMember");
        board.setAuthor(member);
        service.store(board,file);
        model.addAttribute("message","작성이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");
        return "message";
    }

    @GetMapping("board/list")
    public String list(Model model,@PageableDefault(page =0,size = 3,sort = "id",direction = Sort.Direction.DESC) Pageable pageable,String searchK,HttpSession session) {

        Member member = (Member) session.getAttribute("loginMember");
        Page<Board> list = null;

        if (searchK==null){
            list=service.list(pageable);
        }else{
            list=service.searchlist(searchK,pageable);
        }

        int nowP =list.getPageable().getPageNumber();
        int startP=Math.max(nowP-4,1);
        int endP=Math.min(nowP+5,list.getTotalPages());


        model.addAttribute("totalCount",list.getTotalElements());
        model.addAttribute("pageSize",pageable.getPageSize());
        model.addAttribute("list",list);
        model.addAttribute("nowP",nowP);
        model.addAttribute("startP",startP);
        model.addAttribute("endP",endP);
        model.addAttribute("member",member);

        return "boardList";
    }

    @GetMapping("board/view")
    public String boardView(Model model, Long id,HttpSession session ) {

        Board board = service.find(id);

        List<Comment> parentComments = commentService.getParentCommentsByBoardId(id);
        Member member = (Member) session.getAttribute("loginMember");

        model.addAttribute("board",board);
        model.addAttribute("comments",parentComments);
        model.addAttribute("comment",new Comment());
        model.addAttribute("member", member);

        return "boardView";
    }

    @GetMapping("board/delete")
    public String delete(@RequestParam Long id) {
        service.delete(id);
        return "redirect:/board/list";
    }

    @GetMapping("board/modify/{id}")
    public String modify(@PathVariable("id") Long id, Model model) {
        model.addAttribute("board", service.modify(id));

        return "modify";
    }

    @PostMapping("board/update/{id}")
    public String update(@PathVariable("id") Long id, Board board,Model model,HttpSession session,@RequestParam("file") MultipartFile file) throws  Exception{

        Member member = (Member) session.getAttribute("loginMember");
        board.setAuthor(member);
        board.setId(id);
        service.store(board,file);
        model.addAttribute("message","수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");
        return "message";

    }

    @GetMapping("/")
    public String dwq(){
        return "login";
    }

    @PostMapping("writeComment/{boardId}")
    public String writeComment(@PathVariable Long boardId, @Valid @ModelAttribute("comment") Comment comment, BindingResult result,
                               @RequestParam(value = "parentId",required = false) Long parentId,HttpSession session
                               , Model model){

        Board board =service.find(boardId);
        if (board ==null){
            throw new IllegalStateException("게시글이 존재하지않습니다");
        }

        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        if (result.hasErrors()) {
            model.addAttribute("message","내용을 입력하세요");
            model.addAttribute("searchUrl","/board/view?id="+boardId);
            return "message"; // 기존 페이지로 돌아가기
        }

        comment.setBoard(board); //댓글이 속할 게시글 설정
        comment.setAuthor(member); //댓글 작성자 설정
        comment.setTime(LocalDateTime.now()); //댓글 작성 시간 설정

        //대댓글 처리
        if (parentId != null) {
            Comment parentComment = commentRepository.findById(parentId).orElse(null);
            if (parentComment != null) {
                comment.setParent(parentComment);
            }
        }

        commentRepository.save(comment);

        return "redirect:/board/view?id="+boardId;
    }

    @PostMapping("/likeComment/{commentId}")
    public String likeComment(@PathVariable Long commentId, HttpSession session) {
        // 로그인 검사
        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 좋아요 처리
        commentService.likeComment(commentId,member);

        // 리다이렉트: 댓글이 속한 게시글 페이지로 돌아가기
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return "redirect:/board/view?id=" + comment.getBoard().getId();
    }

    @PostMapping("/unlikeComment/{commentId}")
    public String unlikeComment(@PathVariable Long commentId, HttpSession session) {
        // 로그인 검사
        Member member = (Member) session.getAttribute("loginMember");
        if (member == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 좋아요 처리
        commentService.unlikeComment(commentId,member);

        // 리다이렉트: 댓글이 속한 게시글 페이지로 돌아가기
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return "redirect:/board/view?id=" + comment.getBoard().getId();
    }


}
