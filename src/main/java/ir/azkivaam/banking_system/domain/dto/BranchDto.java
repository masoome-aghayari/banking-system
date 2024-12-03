package ir.azkivaam.banking_system.domain.dto;

import ir.azkivaam.banking_system.domain.entity.Branch;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link Branch}
 */

@Getter
@Setter
@Builder
public class BranchDto {
    private Long id;
    private String name;
    private String code;
}