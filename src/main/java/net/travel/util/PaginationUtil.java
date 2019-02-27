package net.travel.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

    public int getPaginationLength(int count, int size) {
        if (count < size) {
            return 1;
        } else if (count % size == 0) {
            return count / size;
        } else {
            return count/size+1;
        }
    }

    public Pageable checkPageableObject(Pageable pageable,int length){
        if(pageable.getPageNumber() >= length){
            return PageRequest
                    .of(0,pageable.getPageSize());
        }
        return pageable;
    }
}