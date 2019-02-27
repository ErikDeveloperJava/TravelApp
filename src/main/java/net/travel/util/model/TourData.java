package net.travel.util.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourData<T> {

    private boolean existsWishList;

    private int rating;

    private T tourPlace;
}