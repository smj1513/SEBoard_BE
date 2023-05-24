package com.seproject.admin.service;

import com.seproject.admin.domain.MainPageMenu;
import com.seproject.admin.domain.repository.MainPageMenuRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.seboard.domain.model.category.Menu;
import com.seproject.seboard.domain.repository.category.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MainPageService {

    private final MainPageMenuRepository mainPageMenuRepository;
    private final MenuRepository menuRepository;

    public List<MainPageMenu> retrieveAllMainPageMenus() {
        return mainPageMenuRepository.findAll();
    }

    public MainPageMenu createMainPageMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() ->
                new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY,null));

        MainPageMenu mainPageMenu = new MainPageMenu(menu);

        mainPageMenuRepository.save(mainPageMenu);

        return mainPageMenu;
    }

    public MainPageMenu deleteMainPageMenu(Long id) {
        MainPageMenu mainPageMenu = mainPageMenuRepository.findById(id).orElseThrow(() ->
                new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY, null));

        mainPageMenuRepository.delete(mainPageMenu);

        return mainPageMenu;
    }


}
