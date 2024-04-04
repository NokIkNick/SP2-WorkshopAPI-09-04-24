package groupone.dtos;

import groupone.model.Zipcode;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ZipcodeDTO {
    private String city;
    private Integer zip;

    public ZipcodeDTO(Zipcode zipcode){
        setZip(zipcode.getZip());
        setCity(zipcode.getCity());
    }
}