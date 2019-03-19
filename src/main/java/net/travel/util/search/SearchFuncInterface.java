package net.travel.util.search;

import net.travel.dto.PlaceDto;
import net.travel.dto.SearchDto;
import org.springframework.data.domain.Pageable;

public interface SearchFuncInterface<T> {

    SearchDto<T> getByParams(SearchParam searchParam, Pageable pageable, int userId);
}
