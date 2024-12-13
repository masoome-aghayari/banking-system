package ir.azkivaam.banking_system.controller;

/*
 * @author masoome.aghayari
 * @since 12/1/24
 */

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.azkivaam.banking_system.domain.dto.DepositRequest;
import ir.azkivaam.banking_system.domain.dto.TransactionDto;
import ir.azkivaam.banking_system.domain.dto.TransferRequest;
import ir.azkivaam.banking_system.domain.dto.WithdrawRequest;
import ir.azkivaam.banking_system.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "APIs for managing bank transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(
            summary = "Deposits money to an account",
            description = "Deposits money to a specified bank account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deposited money successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "500", description = "Server Error"),
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/deposit")
    public TransactionDto deposit(@Validated @RequestBody DepositRequest request) {
        return transactionService.deposit(request);
    }

    @Operation(
            summary = "Withdraws money from an account",
            description = "Withdraws money from a specified bank account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Withdrew money successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "500", description = "Server Error")
            },
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/withdraw")
    public TransactionDto withdraw(@Validated @RequestBody WithdrawRequest withdrawRequest) {
        return transactionService.withdraw(withdrawRequest);
    }

    @Operation(summary = "Transfers money between accounts",
            description = "Transfers funds from one account to another.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transferred money successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "500", description = "Server Error")
            },
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/transfer")
    public TransactionDto transfer(@Validated @RequestBody TransferRequest transferRequest) {
        return transactionService.transfer(transferRequest);
    }
}
