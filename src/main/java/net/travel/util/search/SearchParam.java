package net.travel.util.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchParam {

    private SearchModelType modelType;

    private String name;

    private String place;

    private String roomsCount;

    private String price;

    private String region;

    private String city;

    private String order;

    private int userId;
}