package ir.azkivaam.banking_system.observer.impl;

import ir.azkivaam.banking_system.config.LocaleConfig;
import ir.azkivaam.banking_system.domain.dto.BankAccountDto;
import ir.azkivaam.banking_system.domain.dto.Event;
import ir.azkivaam.banking_system.observer.BankAccountObserver;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * @author masoome.aghayari
 * @since 12/1/24
 */

@Service
@RequiredArgsConstructor
public class BankAccountLogger implements BankAccountObserver {
    private static final Logger logger = LoggerFactory.getLogger("bankAccountLogger");

    private final MessageSource messageSource;


    @Override
    public void update(Event<BankAccountDto> event) {
        BankAccountDto account = event.getData();
        if (account == null) {
            onAccountCreationFailure(event);
        } else {
            onAccountCreationSuccess(account);
        }
    }

    private void onAccountCreationSuccess(BankAccountDto account) {
        String accountInfo = account.getAccountInfo();
        logger.info(getLogMessage("account.creation.success", accountInfo));
    }

    private void onAccountCreationFailure(Event<BankAccountDto> event) {
        String reason = getLogMessage(event.getMessage(), event.getArguments());
        logger.error(getLogMessage("error.account.creation.failed", reason));
    }

    private String getLogMessage(String code, String... params) {
        return messageSource.getMessage(code, params, LocaleConfig.LOCALE);
    }
}
