package ir.azkivaam.banking_system.factory;

import ir.azkivaam.banking_system.domain.dto.TransactionDto;
import ir.azkivaam.banking_system.domain.enums.TransactionStatus;
import ir.azkivaam.banking_system.domain.enums.TransactionType;

import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TransactionDtoFactory {
    public static TransactionDto createTransactionDto(Long amount,
                                                      TransactionType type,
                                                      String sourceAccountNumber,
                                                      String destinationAccountNumber) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setDate(new Date());
        transactionDto.setAmount(amount);
        transactionDto.setTrackingCode(generateTrackingCode());
        transactionDto.setType(type);
        transactionDto.setStatus(TransactionStatus.INITIALIZED);
        transactionDto.setSourceAccountNumber(sourceAccountNumber);

        if (type.equals(TransactionType.TRANSFER)) {
            if (destinationAccountNumber == null || destinationAccountNumber.isBlank()) {
                throw new IllegalArgumentException("Destination account number is required for TRANSFER transactions.");
            } else {
                transactionDto.setDestinationAccountNumber(destinationAccountNumber);
            }
        }
        return transactionDto;
    }

    private static String generateTrackingCode() {
        String trackingCode;
        Random random = new Random();
        trackingCode = IntStream.range(1, 7)
                                .mapToObj(i -> String.valueOf(random.nextInt(0, 10)))
                                .collect(Collectors.joining("", String.valueOf(random.nextInt(0, 9) + 1), ""));
        return trackingCode;
    }
}
