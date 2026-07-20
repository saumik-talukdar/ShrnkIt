package bd.pro.saumik.shrnkit.domain.auth.listener;

import bd.pro.saumik.shrnkit.domain.auth.event.PasswordResetRequestedEvent;
import bd.pro.saumik.shrnkit.domain.auth.service.AuthEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PasswordResetListener {

    private final AuthEmailService authEmailService;

    @Async("mailExecutor")
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(PasswordResetRequestedEvent event) {

        authEmailService.sendResetPasswordMail(
                event.userId(),
                event.firstName(),
                event.email()
        );
    }
}