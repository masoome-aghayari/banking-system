package ir.azkivaam.banking_system.mapper;

/*
 * @author masoome.aghayari
 * @since 11/29/24
 */


import ir.azkivaam.banking_system.domain.dto.BankAccountCreationResponse;
import ir.azkivaam.banking_system.domain.dto.BankAccountDto;
import ir.azkivaam.banking_system.domain.entity.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Mapper(componentModel = "spring", uses = {BankAccount.class, BranchMapper.class, PersonMapper.class})
public interface BankAccountMapper {
    BankAccountDto toDto(BankAccount entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "balance", expression = "java(dto.getBalance() == null ? 1_000_000 : dto.getBalance())")
    @Mapping(target = "accountNumber",
            expression = "java(dto.getAccountNumber() == null ? generateAccountNumber() : dto.getAccountNumber())")
    BankAccount toEntity(BankAccountDto dto);

    @Mapping(target = "accountHolderName", expression = "java(dto.getPerson().getSureName() + \" \" + dto.getPerson().getLastname())")
    @Mapping(target = "accountOpenerBranch", expression = "java(dto.getBranch().getName() + \"-\" + dto.getBranch().getCode())")
    @Mapping(target = "maskedAccountNumber", expression = "java(dto.getMaskedAccountNumber())")
    @Mapping(target = "accountBalance", expression = "java(dto.getBalance())")
    BankAccountCreationResponse toResponse(BankAccountDto dto);

    default String generateAccountNumber() {
        String accountNumber;
        Random random = new Random();
        accountNumber = IntStream.range(1, 10)
                                 .mapToObj(i -> String.valueOf(random.nextInt(0, 10)))
                                 .collect(Collectors.joining("", String.valueOf(random.nextInt(0, 9) + 1), ""));
        return accountNumber;
    }
}
