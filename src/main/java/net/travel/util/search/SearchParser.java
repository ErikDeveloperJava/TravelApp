package net.travel.util.search;

import net.travel.repository.CityRepository;
import net.travel.repository.PlaceRepository;
import net.travel.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SearchParser {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CityRepository cityRepository;

    public String buildQuery(SearchParam searchParam,
                             Optional<Pageable> optionalPageable){
        int regionId = checkModelId(searchParam.getRegion(),regionRepository::existsById);
        int cityId = checkModelId(searchParam.getCity(),cityRepository::existsById);
        int placeId = checkModelId(searchParam.getPlace(),placeRepository::existsById);
        int userId = searchParam.getUserId();
        int roomsCount = checkInteger(searchParam.getRoomsCount());
        String name = searchParam.getName();
        double price = checkDouble(searchParam.getPrice());
        String order = searchParam.getOrder().equals("new") ? "DESC" : "ASC";
        SearchModelType modelType = searchParam.getModelType();
        StringBuilder queryBuilder = new StringBuilder();
        if(!optionalPageable.isPresent()){
            queryBuilder.append("select count(*) from ");
            queryBuilder.append(modelType.getName());
            queryBuilder.append(" m");
            queryBuilder.append(" ");
        }else {
            queryBuilder.append("select * from ");
            queryBuilder.append(modelType.getName());
            queryBuilder.append(" m");
            queryBuilder.append(" ");
        }
        if(regionId != -1 || cityId != -1){
            queryBuilder.append("inner join contact c on m.contact_id = c.id");
            queryBuilder.append(" ");
            if(regionId != -1){
                queryBuilder.append("inner join region r on c.region_id = r.id");
                queryBuilder.append(" ");
            }
            if(cityId != -1){
                queryBuilder.append("inner join city c2 on c.city_id = c2.id");
                queryBuilder.append(" ");
            }
        }
        if(placeId != -1){
            queryBuilder.append("inner join place p on m.place_id = p.id");
            queryBuilder.append(" ");
        }
        if(userId != -1){
            queryBuilder.append("inner join wish_list wl on m.id = ");
            if(searchParam.getModelType() == SearchModelType.PLACE){
                queryBuilder.append("wl.place_id ");
            }else {
                queryBuilder.append("wl.hotel_id ");
            }
            queryBuilder.append(" inner join user u on wl.user_id = u.id");
            queryBuilder.append(" ");
        }
        if(roomsCount != -1){
            queryBuilder.append("inner join hotel_room hr on m.id = hr.hotel_id inner join hotel_room_attribute hra on hr.hotel_room_attribute_id = hra.id");
            queryBuilder.append(" ");
        }
        if(regionId != -1 || cityId != -1 || placeId != -1 || roomsCount != -1
                || !name.equals("-1") || price != -1.0 || userId != -1){
            queryBuilder.append("where ");
        }
        boolean isAddFirst = false;
        if(regionId != -1){
            isAddFirst = true;
            queryBuilder.append("r.id=");
            queryBuilder.append(regionId);
            queryBuilder.append(" ");
        }
        if(cityId != -1){
            isAddFirst = addAnd(isAddFirst,queryBuilder);
            queryBuilder.append("c2.id=");
            queryBuilder.append(cityId);
            queryBuilder.append(" ");
        }
        if(userId != -1){
            isAddFirst = addAnd(isAddFirst,queryBuilder);
            queryBuilder.append("u.id=");
            queryBuilder.append(userId);
            queryBuilder.append(" ");
        }
        if(placeId != -1){
            isAddFirst = addAnd(isAddFirst,queryBuilder);
            queryBuilder.append("p.id=");
            queryBuilder.append(placeId);
            queryBuilder.append(" ");
        }
        if(roomsCount != -1){
            isAddFirst = addAnd(isAddFirst,queryBuilder);
            queryBuilder.append("hra.room_count=");
            queryBuilder.append(roomsCount);
            queryBuilder.append(" ");
        }
        if(!name.equals("-1")){
            isAddFirst = addAnd(isAddFirst,queryBuilder);
            queryBuilder.append("m.name like '%");
            queryBuilder.append(name);
            queryBuilder.append("%' ");
        }
        if(price != -1.0){
            isAddFirst = addAnd(isAddFirst,queryBuilder);
            queryBuilder.append("m.price=");
            queryBuilder.append(price);
            queryBuilder.append(" ");
        }
        if(optionalPageable.isPresent()){
            Pageable pageable = optionalPageable.get();
            queryBuilder.append("order by m.id ");
            queryBuilder.append(order);
            queryBuilder.append(" limit ");
            queryBuilder.append(pageable.getPageNumber() * pageable.getPageSize());
            queryBuilder.append(",");
            queryBuilder.append(pageable.getPageSize());
        }
        return queryBuilder.toString();
    }

    private boolean addAnd(boolean param,StringBuilder queryBuilder){
        if(!param){
            return true;
        }else {
            queryBuilder.append("and ");
            return true;
        }
    }

    private int checkModelId(String strModelId,CheckModelInterface checkModelInterface){
        try {
            int modelId = Integer.parseInt(strModelId);
            if(!checkModelInterface.existsById(modelId)){
                throw new NumberFormatException();
            }
            return modelId;
        }catch (NumberFormatException e){
            return -1;
        }
    }

    private double checkDouble(String strNumber){
        try {
            return Double.parseDouble(strNumber);
        }catch (NumberFormatException e){
            return -1;
        }
    }

    private int checkInteger(String strNumber){
        try {
            return Integer.parseInt(strNumber);
        }catch (NumberFormatException e){
            return -1;
        }
    }
}