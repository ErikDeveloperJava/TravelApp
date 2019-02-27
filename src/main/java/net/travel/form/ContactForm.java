package net.travel.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactForm {

    @Length(min = 4,max = 40)
    private String phone;

    @Length(min = 10,max = 255)
    private String address;

    @Min(1)
    private int regionId;

    @Min(1)
    private int cityId;
}