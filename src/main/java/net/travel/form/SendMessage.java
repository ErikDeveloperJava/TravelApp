package net.travel.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMessage {

    @Length(min = 4)
    private String subject;

    @Length(min = 10)
    private String message;

    private int orderId;
}