package mk.ukim.finki.gbook.service;

import com.stripe.exception.*;
import com.stripe.model.Charge;
import mk.ukim.finki.gbook.models.ChargeRequest;

public interface StripeService {
     Charge charge(ChargeRequest chargeRequest) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException;
}
