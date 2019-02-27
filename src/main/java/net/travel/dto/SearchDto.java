package net.travel .dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.travel.util.model.TourData;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDto<T> {

    List<TourData<T>> modelList;

    private int paginationLength;

    private int pageNumber;

    private int pageSize;
}