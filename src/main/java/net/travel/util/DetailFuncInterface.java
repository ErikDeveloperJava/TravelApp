package net.travel.util;

import net.travel.util.model.TourDetailData;

public interface DetailFuncInterface<M,IL> {

    TourDetailData<M,IL> getDetailById(int id, int userId);
}
