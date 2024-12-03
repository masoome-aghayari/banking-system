package ir.azkivaam.banking_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.azkivaam.banking_system.domain.annotation.Digital;
import ir.azkivaam.banking_system.domain.dto.BankAccountCreationResponse;
import ir.azkivaam.banking_system.domain.dto.BankAccountDto;
import ir.azkivaam.banking_system.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author masoome.aghayari
 * @since 12/1/24
 */

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "APIs for managing bank accounts")
public class AccountController {
    private final AccountService accountService;

    @Operation(
            summary = "Creates a new bank account",
            description = "Creates a bank account with an initial balance.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/create")
    public BankAccountCreationResponse createAccount(@Validated @RequestBody BankAccountDto request) {
        return accountService.createAccount(request);
    }

    @Operation(summary = "Get account balance",
            description = "Retrieve the balance of a specified bank account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Account not found")
            },
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/balance")
    public Long getBalance(@RequestParam @Digital String accountNumber) {
        return accountService.getBalance(accountNumber);
    }
}
