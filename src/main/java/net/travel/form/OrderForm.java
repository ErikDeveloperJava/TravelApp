package net.travel.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderForm {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date whenDate;

    @Min(1)
    private int daysCount;

    @Min(0)
    private int adultCount;

    @Min(0)
    private int childrenCount;

    @Min(1)
    private int hotelId;

    @Min(1)
    private int hotelRoomId;
}