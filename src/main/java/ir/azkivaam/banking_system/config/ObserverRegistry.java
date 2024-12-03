package ir.azkivaam.banking_system.config;

import ir.azkivaam.banking_system.observer.BankAccountObserver;
import ir.azkivaam.banking_system.observer.TransactionObserver;
import ir.azkivaam.banking_system.observer.impl.BankAccountObserverManager;
import ir.azkivaam.banking_system.observer.impl.TransactionObserverManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ObserverRegistry {

    private final ApplicationContext applicationContext;
    private final BankAccountObserverManager bankAccountObserverManager;
    private final TransactionObserverManager transactionObserverManager;

    @PostConstruct
    public void registerAllObservers() {
        Map<String, BankAccountObserver> bankAccountObservers =
                applicationContext.getBeansOfType(BankAccountObserver.class);
        bankAccountObservers.values().forEach(bankAccountObserverManager::subscribe);

        Map<String, TransactionObserver> transactionObservers =
                applicationContext.getBeansOfType(TransactionObserver.class);
        transactionObservers.values().forEach(transactionObserverManager::subscribe);
    }
}
