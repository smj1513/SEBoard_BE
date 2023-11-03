package com.seproject.board.bulletin.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seproject.board.bulletin.domain.model.MainPageMenu;
import com.seproject.board.bulletin.domain.model.QMainPageMenu;
import com.seproject.board.menu.domain.BoardMenu;
import com.seproject.board.menu.domain.Category;
import com.seproject.board.menu.domain.InternalSiteMenu;
import com.seproject.board.menu.domain.Menu;
import com.seproject.board.post.domain.model.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.seproject.board.bulletin.domain.model.QMainPageMenu.mainPageMenu;
import static com.seproject.board.menu.domain.QMenu.menu;

@RequiredArgsConstructor
@Repository
public class MainPageQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<InternalSiteMenu> findMainPageableMenu() {
        List<Menu> fetch = jpaQueryFactory
                .select(menu).distinct()
                .from(mainPageMenu).rightJoin(mainPageMenu.menu, menu)
                .where(isInternalSiteMenu().and(mainPageMenu.isNull()))
                .fetch();

        List<InternalSiteMenu> collect = fetch.stream()
                .map((menu) -> (InternalSiteMenu) menu)
                .collect(Collectors.toList());

        return collect;
    }

    public List<MainPageMenu> findAllWithMenu() {

        return jpaQueryFactory
                .select(mainPageMenu)
                .from(mainPageMenu)
                .leftJoin(mainPageMenu.menu,menu).fetchJoin()
                .fetch();
    }

    private BooleanExpression isInternalSiteMenu() {
        return menu.instanceOfAny(InternalSiteMenu.class, BoardMenu.class);
    }

}
