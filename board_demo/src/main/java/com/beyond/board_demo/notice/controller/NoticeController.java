package com.beyond.board_demo.notice.controller;

import com.beyond.board_demo.notice.domain.Notice;
import com.beyond.board_demo.notice.dto.NoticeDetailDto;
import com.beyond.board_demo.notice.dto.NoticeListResDto;
import com.beyond.board_demo.notice.dto.NoticeSaveReqDto;
import com.beyond.board_demo.notice.dto.NoticeUpdateDto;
import com.beyond.board_demo.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("create")
    public String createNoticeForm() {
        return "notice/create";
    }

    @PostMapping("create")
    public String createNotice(@ModelAttribute NoticeSaveReqDto dto, Model model) {
        try {
            noticeService.createNotice(dto, dto.getEmail());
            return "redirect:/notice/list";
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "notice/create";
        }
    }

    @GetMapping("list")
    public String getAllNotices(Model model, @PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("noticeList", noticeService.noticeList(pageable));
        return "notice/list";
    }

    @GetMapping("detail/{id}")
    public String getNoticeDetail(@PathVariable Long id, Model model) {
        NoticeDetailDto noticeDetail = noticeService.getNoticeDetail(id);
        model.addAttribute("notice", noticeDetail);
        return "notice/detail";
    }

    @PostMapping("update/{id}")
    public String noticeUpdate(@PathVariable Long id, @ModelAttribute NoticeUpdateDto dto){
        noticeService.noticeUpdate(id, dto);
        return "redirect:/notice/detail/" + id;
    }

    @GetMapping("delete/{id}")
    public String noticeDelete(@PathVariable Long id, Model model){
        noticeService.noticeDelete(id);
        return "redirect:/notice/list";
    }
}
