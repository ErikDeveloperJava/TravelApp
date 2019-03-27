package net.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SendMessageDto {

    private List<String> errorList;

    private boolean success;

    public SendMessageDto() {
        errorList = new ArrayList<>();
    }
}