package ir.azkivaam.banking_system.mapper;

/*
 * @author masoome.aghayari
 * @since 11/30/24
 */

import ir.azkivaam.banking_system.domain.dto.TransactionDto;
import ir.azkivaam.banking_system.domain.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface TransactionMapper {

    TransactionDto toDto(Transaction entity);

    Transaction toEntity(TransactionDto dto);
}
