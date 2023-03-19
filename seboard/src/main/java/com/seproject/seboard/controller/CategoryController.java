package com.seproject.seboard.controller;

import com.seproject.seboard.application.CategoryAppService;
import com.seproject.seboard.dto.CategoryDTO;
import com.seproject.seboard.dto.MessageDTO.ResponseMessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryAppService categoryAppService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseMessageDTO enrollCategory(@RequestBody CategoryDTO.CategoryRequestDTO dto, Long userId){
        categoryAppService.createCategory(dto.getSuperCategoryId(), userId, dto.getName());
        return new ResponseMessageDTO(""); //TODO : message
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{categoryId}")
    public ResponseMessageDTO updateCategory(@RequestBody CategoryDTO.CategoryRequestDTO dto,
                                             @PathVariable Long categoryId, Long userId){
        categoryAppService.updateCategory(categoryId, userId, dto.getName());
        return new ResponseMessageDTO(""); // TODO : message
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{categoryId}")
    public ResponseMessageDTO removeCategory(@PathVariable Long categoryId, Long userId){
        categoryAppService.removeCategory(categoryId, userId);
        return new ResponseMessageDTO(""); // TODO : message
    }



}
